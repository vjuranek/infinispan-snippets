package org.infinispan.demo

import java.util.Properties

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.infinispan.spark.rdd.InfinispanRDD

object TextSearch {
  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setAppName("TextSearch")
    val sc = new SparkContext(sparkConf)

    val rdd = getRDDFromFile(sc, "/tmp/test.log")
    val errors = rdd.filter(line => line.contains("ERROR"))
    println("Number of lines containing 'ERROR': " + errors.count())
  }

  def getRDDFromFile(sc: SparkContext, path: String): RDD[String] = {
    sc.textFile(path)
  }

  def getRDDFromIspn(sc: SparkContext): RDD[(String, String)] = {
    val config = new Properties
    config.put("infinispan.rdd.cacheName", "default")
    config.put("infinispan.client.hotrod.server_list", "127.0.0.1:11222")
    new InfinispanRDD[String, String](sc, configuration = config)
  }

}