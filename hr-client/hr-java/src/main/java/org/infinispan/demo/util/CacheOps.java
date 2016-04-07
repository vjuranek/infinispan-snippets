package org.infinispan.demo.util;

import java.util.Map;
import java.util.function.Function;

import org.infinispan.client.hotrod.RemoteCache;

public class CacheOps {
    
    public static Function<RemoteCache<?, ?>, RemoteCache<?, ?>> dumpCache = cache -> {
        Map<?, ?> entries = cache.getBulk();
        System.out.printf("Number of obtained entries: %d%n", entries.size());
        for (Object key : entries.keySet()) {
            System.out.printf("[%s -> %s]%n", key, entries.get(key));
        }
        return cache;
    };
    
    public static Function<RemoteCache<?, ?>, RemoteCache<?, ?>> cacheSize = cache -> {
        System.out.printf("Cahce size: %d%n", cache.size());
        return cache;
    };
    
    public static void onCache(RemoteCache<?, ?> cache, Function<RemoteCache<?, ?>, RemoteCache<?, ?>> cacheFunction) {
        cacheFunction.apply(cache);
    }

}
