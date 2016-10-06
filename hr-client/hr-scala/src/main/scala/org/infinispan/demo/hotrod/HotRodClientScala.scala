package org.infinispan.demo.hotrod

import scala.io.Source

import org.infinispan.client.hotrod.RemoteCacheManager
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder
import org.infinispan.client.hotrod.impl.ConfigurationProperties
import org.infinispan.client.hotrod.RemoteCache
import java.io.File
import java.util.UUID

object HotRodClientScala {

    val ISPN_IP = "127.0.0.1";

    def main(args: Array[String]) {
        val builder = new ConfigurationBuilder()
        builder.addServer().host(ISPN_IP).port(ConfigurationProperties.DEFAULT_HOTROD_PORT)
        val cacheManager = new RemoteCacheManager(builder.build())
        val cache = cacheManager.getCache[String, String]()
        generate(cache, 500000)
        cacheManager.stop()
    }

    def generate(cache: RemoteCache[String, String], numberOfEntries: Int) {
        for (i <- 1 to numberOfEntries) {
            val key = "k%09d".format(i)
            val value = UUID.randomUUID().toString()
            //println("uploading " + (key, value))
            cache.put(key, value)
            if (i%1000 == 0)
                printf("Uploaded %d entries\n", i)
        }
        
    }
    
    def uploadFromFile(cache: RemoteCache[String, String], args: Array[String]) {
        if (args.length < 2)
            throw new IllegalArgumentException("At least one arument (path to file) required when loading data from file!")
        
        val input = Source.fromFile(new File(args(1)))
        try {
            var i = 0
            input.getLines().foreach { line => { i = i+1; cache.put(i.toString(), line) } }
            println("Uploaded lines: " + i)
        } finally {
            input.close()
        }
    }
    
}
