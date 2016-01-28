package org.infinispan.demo

import java.util.Properties

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.State
import org.apache.spark.streaming.StateSpec
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.DStream.toPairDStreamFunctions
import org.infinispan.spark.stream.InfinispanDStream
import org.infinispan.spark.stream.InfinispanInputDStream

object TemperatureAnalysis {

  def main(args: Array[String]) {

    val sparkConf = new SparkConf().setAppName("TemperatureAnalysis")
    val ssc = new StreamingContext(sparkConf, Seconds(1))
    ssc.checkpoint("/tmp/spark-temperature")

    val configIn = new Properties
    configIn.put("infinispan.rdd.cacheName", "default")
    configIn.put("infinispan.client.hotrod.server_list", "127.0.0.1:11222")
    val ispnStream = new InfinispanInputDStream[String, Double](ssc, StorageLevel.MEMORY_ONLY, configIn)

    val mapFunc = (key: String, temps: Option[Iterable[Double]], state: State[Map[String, (Double, Long)]]) => {
      val sumMap: Map[String, (Double, Long)] = state.getOption().getOrElse(Map())
      val sums: (Double, Long) = sumMap.getOrElse(key, (0f, 0L))
      val curTemps: Iterable[Double] = temps.getOrElse(List())
      val sumUp = sums._1 + curTemps.sum
      val countUp = sums._2 + curTemps.size
      state.update(sumMap.updated(key, (sumUp, countUp)))
      (key, sumUp/countUp)
    }
    
    val measurementStream = ispnStream.map[Tuple2[String, Double]](rdd => (rdd._1, rdd._2))
    val measurementGrouped = measurementStream.groupByKey()
    val avgTemperatures = measurementGrouped.mapWithState(StateSpec.function(mapFunc))
    
    avgTemperatures.foreachRDD(rdd => {
      printf("# items in DStream: %d\n", rdd.count())
      rdd.foreach(item => println("Averages:" + item._1 + " -> " + item._2))
    })
    
    val configOut = new Properties
    configOut.put("infinispan.rdd.cacheName", "avg-temperatures")
    configOut.put("infinispan.client.hotrod.server_list", "127.0.0.1:11222")
    InfinispanDStream[String, Double](avgTemperatures).writeToInfinispan(configOut)
    
    ssc.start()
    ssc.awaitTermination()
  }
}