package org.infinispan.demo.util;

import java.util.Map;
import java.util.function.Function;

import org.infinispan.client.hotrod.RemoteCache;

public class CacheOps {
    
    public static final String TEST_KEY = "test_key";
    public static final String TEST_VAL = "test_value";
    
    public static Function<RemoteCache<Object, Object>, RemoteCache<Object, Object>> dumpCache = cache -> {
        Map<?, ?> entries = cache.getBulk();
        System.out.printf("Number of obtained entries: %d%n", entries.size());
        for (Object key : entries.keySet()) {
            System.out.printf("[%s -> %s]%n", key, entries.get(key));
        }
        return cache;
    };
    
    public static Function<RemoteCache<Object, Object>, RemoteCache<Object, Object>> cacheSize = cache -> {
        System.out.printf("Cahce size: %d%n", cache.size());
        return cache;
    };
    
    public static Function<RemoteCache<Object, Object>, RemoteCache<Object, Object>> putTestKV = cache -> {
        cache.put(TEST_KEY, TEST_VAL);
        return cache;
    };
    
    public static void onCache(RemoteCache<Object, Object> cache, Function<RemoteCache<Object, Object>, RemoteCache<Object, Object>> cacheFunction) {
        cacheFunction.apply(cache);
    }

}
