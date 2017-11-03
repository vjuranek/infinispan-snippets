package org.infinispan.demo;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.StorageType;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.stats.Stats;

public class Statistics {
    
    public static void main(String[] args) throws Exception {
        printStatsDefault();
    }
    
    public static void printStatsDefault() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.jmxStatistics().enable();
        cb.memory().storageType(StorageType.OFF_HEAP);
        EmbeddedCacheManager cm =  new DefaultCacheManager(cb.build());
        Cache<String, String> cache = cm.getCache();
        
        doCacheOps(cache);
        
        Stats stats = cache.getAdvancedCache().getStats();
        printStats(stats);
    }
    
    
    private static void doCacheOps(Cache<String, String> cache) {
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");
        cache.put("key4", "value4");
        cache.put("key5", "value5");
        
        cache.evict("key2");
        cache.remove("key2");
        cache.remove("key5");
        
        cache.get("key5");
        cache.get("key2");
        
    }
    
    private static void printStats(Stats stats) {
        System.out.println("AVG read time: " + stats.getAverageReadTime());
        System.out.println("AVG remove time: " + stats.getAverageRemoveTime());
        System.out.println("AVG write time: " + stats.getAverageWriteTime());
        System.out.println("Curr. # of entries: " + stats.getCurrentNumberOfEntries());
        System.out.println("Curr. # of entries in memory: " + stats.getCurrentNumberOfEntriesInMemory());
        System.out.println("Evictions: " + stats.getEvictions());
        System.out.println("Hits: " + stats.getHits());
        System.out.println("Misses: " + stats.getMisses());
        System.out.println("Remove misses: " + stats.getRemoveMisses());
        System.out.println("Off-heap memory used: " + stats.getOffHeapMemoryUsed());
    }

}
