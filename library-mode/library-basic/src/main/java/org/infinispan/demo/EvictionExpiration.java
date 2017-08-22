package org.infinispan.demo;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.StorageType;
import org.infinispan.eviction.EvictionType;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

public class EvictionExpiration {

    public static void main(String[] args) throws Exception {
        //evictionExample();
        evictionWithListener();
        //expirationExample();
    }

    public static void evictionExample() {
        //ISPN8 or sooner
        //Configuration conf = new ConfigurationBuilder().eviction().size(5).strategy(EvictionStrategy.LRU).build();
        //ISPN9 or later
        Configuration conf = new ConfigurationBuilder().memory().storageType(StorageType.OBJECT)
                .evictionType(EvictionType.COUNT).size(5).build();
        EmbeddedCacheManager ecm = new DefaultCacheManager(conf);
        Cache<String, String> cache = ecm.getCache();

        for (int i = 0; i < 100; i++) {
            cache.put("key" + i, "value" + i);
        }
        System.out.printf("Eviction: cache size: %d\n", cache.size());
        dumpCache(cache);
        ecm.stop();
    }
    
    public static void evictionWithListener() {
        Configuration conf = new ConfigurationBuilder().memory()
                .storageType(StorageType.OBJECT)
                .evictionType(EvictionType.COUNT).size(5)
                .build();
        EmbeddedCacheManager ecm = new DefaultCacheManager(conf);
        Cache<String, String> cache = ecm.getCache();
        cache.addListener(new EntryListener());

        for (int i = 0; i < 100; i++) {
            cache.put("key" + i, "value" + i);
        }
        System.out.printf("Eviction: cache size: %d\n", cache.size());
        dumpCache(cache);
        ecm.stop();
    }

    public static void expirationExample() throws Exception {
        final long expiration = 5000L;
        Configuration conf = new ConfigurationBuilder().expiration().maxIdle(expiration).build();
        EmbeddedCacheManager ecm = new DefaultCacheManager(conf);
        Cache<String, String> cache = ecm.getCache();

        for (int i = 0; i < 100; i++) {
            cache.put("key" + i, "value" + i);
        }
        System.out.printf("Expiration: cache size: %d\n", cache.size());
        Thread.sleep(expiration);
        System.out.printf("Expiration: cache size: %d\n", cache.size());
        dumpCache(cache);
        ecm.stop();
    }

    private static void dumpCache(Cache<String, String> cache) {
        System.out.println("Dumping cache:");
        System.out.println("--------------");
        if (cache.size() == 0) {
            System.out.println("No entries in the cache");
        } else {
            for (String key : cache.keySet()) {
                System.out.printf("%s -> %s\n", key, cache.get(key));
            }
        }
    }
}
