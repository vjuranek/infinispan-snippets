package org.infinispan.demo;

import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.infinispan.client.hotrod.event.ClientEvent;
import org.infinispan.spark.stream.InfinispanJavaDStream;

import scala.Tuple3;

public class TemperatureAnalysis {
    public static final String ISPN_ADDRESS = "127.0.0.1:11222";
    public static final String CACHE_NAME = "default";

    public static void main(String[] args) throws UnknownHostException {
        // Create java spark streaming context
        SparkConf conf = new SparkConf().setAppName("temperature-analysis");
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1)); // batch interval of 1 second

        // Create Infinispan stream
        Properties properties = new Properties();
        properties.put("infinispan.client.hotrod.server_list", ISPN_ADDRESS);
        properties.put("infinispan.rdd.cacheName", CACHE_NAME);
        
        JavaDStream<Tuple3<String, Object, ClientEvent.Type>> ispnStream = InfinispanJavaDStream.createInfinispanInputDStream(jssc,
                StorageLevel.MEMORY_ONLY(), properties);

        ispnStream.count().foreach(new Function<JavaRDD<Long>, Void>() {
            public Void call(JavaRDD<Long> rdd) throws Exception {
                System.out.printf("Spark temperature analysis: processed %d data items%n", rdd.first());
                return null;
            }
        });
        
        jssc.start();
        jssc.awaitTermination();
    }
}
