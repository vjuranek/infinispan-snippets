package org.infinispan.demo

import org.infinispan.spark.rdd.InfinispanRDD
import java.util.Properties
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.SparkConf
import org.infinispan.spark.stream.InfinispanInputDStream
import org.apache.spark.storage.StorageLevel

object TemperatureAnalysis {
  def main(args: Array[String]) {

    val sparkConf = new SparkConf().setAppName("TemperatureAnalysis")
    val ssc = new StreamingContext(sparkConf, Seconds(1))

    val config = new Properties
    config.put("infinispan.rdd.cacheName", "default")
    config.put("infinispan.client.hotrod.server_list", "127.0.0.1:11222")
    val ispnStream = new InfinispanInputDStream[String, Object](ssc, StorageLevel.MEMORY_ONLY, config)

    ispnStream.count().foreachRDD(rdd => println("RDD count (from scala):" + rdd.first()))

    ssc.start()
    ssc.awaitTermination()
  }
}