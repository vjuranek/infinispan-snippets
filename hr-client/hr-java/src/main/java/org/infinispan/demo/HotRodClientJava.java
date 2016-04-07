package org.infinispan.demo;

import static org.infinispan.demo.util.CacheOps.cacheSize;
import static org.infinispan.demo.util.CacheOps.dumpCache;
import static org.infinispan.demo.util.CacheOps.onCache;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.impl.ConfigurationProperties;

public class HotRodClientJava {

    public static final String ISPN_IP = "127.0.0.1";

    public static void main(String[] args) {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.addServer().host(ISPN_IP).port(ConfigurationProperties.DEFAULT_HOTROD_PORT);
        RemoteCacheManager cacheManager = new RemoteCacheManager(builder.build());
        String cacheName = args.length > 0 ? args[0] : "";
        RemoteCache<String, String> cache = cacheManager.getCache(cacheName);

        //cacheSize.andThen(dumpCache).apply(cache);
        onCache(cache, cacheSize.andThen(dumpCache));
        
        cacheManager.stop();
    }

}
