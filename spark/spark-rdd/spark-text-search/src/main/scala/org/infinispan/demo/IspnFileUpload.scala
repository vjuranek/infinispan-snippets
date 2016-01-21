package org.infinispan.demo

import scala.io.Source

import org.infinispan.client.hotrod.RemoteCacheManager
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder
import org.infinispan.client.hotrod.impl.ConfigurationProperties

object IspnFileUpload {

    val ISPN_IP = "127.0.0.1";

    def main(args: Array[String]) {
        val builder = new ConfigurationBuilder()
        builder.addServer().host(ISPN_IP).port(ConfigurationProperties.DEFAULT_HOTROD_PORT)
        val cacheManager = new RemoteCacheManager(builder.build())
        val cache = cacheManager.getCache[String, String]()

        val input = Source.fromFile(TextSearch.filePath)
        try {
            var i = 0
            input.getLines().foreach { line => { i = i+1; cache.put(i.toString(), line) } }
            println("Uploaded lines: " + i)
        } finally {
            input.close()
        }

    }

}