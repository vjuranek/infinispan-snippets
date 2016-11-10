package org.infinispan.demo;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;

public class RocksDBCacheStore {

    public static void main(String[] args) throws Exception { 
        DefaultCacheManager cacheManager = new DefaultCacheManager("rocksdb-config.xml");
        /*ConfigurationBuilder cfg = new ConfigurationBuilder();
        cfg.persistence().addStore(RocksDBStoreConfigurationBuilder.class)
            .location("/tmp/leveldb/")
            .expiredLocation("/tmp/leveldb/expired/");
        DefaultCacheManager cacheManager = new DefaultCacheManager(cfg.build());*/
        Cache<String, String> cache = cacheManager.getCache("test");
        
        for (int i = 0; i < 100; i++) {
            cache.put("key" + i, "value" + i);
        }
        System.out.printf("Cache size: %d\n", cache.size());
        
        cache.stop();
        cache.start();
        
        System.out.printf("Cache size: %d\n", cache.size());
        cacheManager.stop();
    }
}
