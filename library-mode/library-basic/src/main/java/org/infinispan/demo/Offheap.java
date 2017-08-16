package org.infinispan.demo;

import java.util.Random;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.StorageType;
import org.infinispan.eviction.EvictionType;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

public class Offheap {

    public static void main(String[] args) throws Exception {
        offheapEvictionLargeEntries();
    }

    public static void offheapEvictionExample() {
        Configuration conf = new ConfigurationBuilder().memory().storageType(StorageType.OFF_HEAP)
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
    
    public static void offheapEvictionLargeEntries() {
        Configuration conf = new ConfigurationBuilder().memory().storageType(StorageType.OFF_HEAP)
                .evictionType(EvictionType.COUNT).size(5).build();
        EmbeddedCacheManager ecm = new DefaultCacheManager(conf);
        Cache<String, byte[]> cache = ecm.getCache();
        Random rand = new Random();
        byte[] bytes = new byte[8192000]; //8MiB

        for (int i = 0; i < 100; i++) {
            rand.nextBytes(bytes);
            cache.put("key" + i, bytes);
        }
        System.out.printf("Eviction: cache size: %d\n", cache.size());
        ecm.stop();
    }

    private static void dumpKeys(Cache<String, String> cache) {
        System.out.println("Dumping cache keys:");
        System.out.println("-------------------");
        if (cache.size() == 0) {
            System.out.println("No entries in the cache");
        } else {
            for (String key : cache.keySet()) {
                System.out.printf("%s\n", key);
            }
        }
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
