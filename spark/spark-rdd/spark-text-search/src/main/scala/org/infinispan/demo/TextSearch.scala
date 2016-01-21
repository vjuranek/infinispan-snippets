package org.infinispan.demo

import java.util.Properties

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.infinispan.spark.rdd.InfinispanRDD

object TextSearch {
    
    val filePath = "/tmp/test.log" 
    val searchedText = "ERROR"
    
    def main(args: Array[String]) {
        val sparkConf = new SparkConf().setAppName("TextSearch")
        val sc = new SparkContext(sparkConf)

        val errors = searchFromFile(sc)
        //val errors = searchFromIspn(sc)
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

}