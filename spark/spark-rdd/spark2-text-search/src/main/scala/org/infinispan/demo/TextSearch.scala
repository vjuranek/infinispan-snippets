package org.infinispan.demo

import java.util.Properties

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType
import org.infinispan.spark.rdd.InfinispanRDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.Row
import org.apache.parquet.filter2.compat.FilterCompat.Filter
import org.apache.spark.sql.Dataset

object TextSearch {
    
    val filePath = "/tmp/test.log" 
    val searchedText = "ERROR"
    
    def main(args: Array[String]) {
        val sparkConf = new SparkConf().setAppName("TextSearch")
        val sc = new SparkContext(sparkConf)

        //val errors = searchFromFile(sc)
        val errors = searchFromIspn(sc)
        //val errors = searchFromIspnAsDF(sc)
        printf("Number of lines containing '%s': %d \n", searchedText, errors.count())
    }

    def searchFromFile(sc: SparkContext): RDD[String] = {
        val rdd = sc.textFile(filePath)
        rdd.filter(line => line.contains(searchedText))
    }

    def searchFromIspn(sc: SparkContext): RDD[(String, String)] = {
        val config = new Properties
        config.put("infinispan.rdd.cacheName", "default")
        config.put("infinispan.client.hotrod.server_list", "127.0.0.1:11222")
        val rdd = new InfinispanRDD[String, String](sc, configuration = config)
        rdd.filter(keyVal => keyVal._2.contains(searchedText))
    }
    
    def searchFromIspnAsDF(sc: SparkContext): Dataset[Row] = {
        val config = new Properties
        config.put("infinispan.rdd.cacheName", "default")
        config.put("infinispan.client.hotrod.server_list", "127.0.0.1:11222")
        val rdd = new InfinispanRDD[String, String](sc, configuration = config)
        
        val spark = SparkSession.builder().appName("TextSearch").getOrCreate()
        val keyField = new StructField("key", StringType, nullable = false)
        val valueField = new StructField("value", StringType, nullable = true)
        val schema = StructType(Seq(keyField, valueField))
        val rowRdd = rdd.map(keyVal => Row(keyVal._1, keyVal._2))
        val df = spark.createDataFrame(rowRdd, schema)
        df.filter(df("value").contains(searchedText))
    }

}
