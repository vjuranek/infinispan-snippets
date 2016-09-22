package org.infinispan.demo;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.persistence.ceph.configuration.CephStoreConfigurationBuilder;

public class CephCacheStore {

    public static void main(String[] args) throws Exception { 
        /*ConfigurationBuilder cfg = new ConfigurationBuilder();
        cfg.persistence().addStore(CephStoreConfigurationBuilder.class)
            .userName("admin")
            .key("AQCY2sdXyDIcJxAAK1edRJ8xOJ2NkkiXzAuq5A==")
            .monitorHost("192.168.122.145:6789")
            .poolNamePrefix("ispn_store");
        DefaultCacheManager cacheManager = new DefaultCacheManager(cfg.build());*/
        
        DefaultCacheManager cacheManager = new DefaultCacheManager("ispn-ceph.xml");
        Cache<String, String> cache = cacheManager.getCache("test");
        cache.put("key", "value");
        System.out.printf("key = %s\n", cache.get("key"));
        
        cache.stop();
        cache.start();
        
        System.out.printf("key = %s\n", cache.get("key"));
        cacheManager.stop();
    }
}
