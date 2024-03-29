package org.infinispan.demo.util;

import java.util.Map;
import java.util.function.Function;

import org.infinispan.client.hotrod.RemoteCache;

public class CacheOps {
    
    public static final String TEST_KEY = "test_key";
    public static final String TEST_VAL = "test_value";
    
    public static Function<RemoteCache<Object, Object>, RemoteCache<Object, Object>> dumpCache = cache -> {
        System.out.printf("Number of obtained entries: %d%n", cache.size());
        for (Map.Entry<Object, Object> entry : cache.entrySet()) {
            System.out.printf("%s -> %s%n", new String((byte[])entry.getKey()), new String((byte[])entry.getValue()));
        }
        return cache;
    };
    
    public static Function<RemoteCache<Object, Object>, RemoteCache<Object, Object>> cacheSize = cache -> {
        System.out.printf("Cahce size: %d%n", cache.size());
        return cache;
    };
    
    public static Function<RemoteCache<Object, Object>, RemoteCache<Object, Object>> putTestKV = cache -> {
        cache.put(TEST_KEY.getBytes(), TEST_VAL.getBytes());
        return cache;
    };
    
    public static void onCache(RemoteCache<Object, Object> cache, Function<RemoteCache<Object, Object>, RemoteCache<Object, Object>> cacheFunction) {
        cacheFunction.apply(cache);
    }

}
