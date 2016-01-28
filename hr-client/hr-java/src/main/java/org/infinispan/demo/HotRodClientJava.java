package org.infinispan.demo;

import java.util.Map;
import java.util.function.Function;

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

    private static Function<RemoteCache<?, ?>, RemoteCache<?, ?>> dumpCache = cache -> {
        Map<?, ?> entries = cache.getBulk();
        System.out.printf("Number of obtained entries: %d%n", entries.size());
        for (Object key : entries.keySet()) {
            System.out.printf("[%s -> %s]%n", key, entries.get(key));
        }
        return cache;
    };
    
    private static Function<RemoteCache<?, ?>, RemoteCache<?, ?>> cacheSize = cache -> {
        System.out.printf("Cahce size: %d%n", cache.size());
        return cache;
    };
    
    private static void onCache(RemoteCache<?, ?> cache, Function<RemoteCache<?, ?>, RemoteCache<?, ?>> cacheFunction) {
        cacheFunction.apply(cache);
    }

}
