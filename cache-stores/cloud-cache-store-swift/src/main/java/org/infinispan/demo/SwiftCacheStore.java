package org.infinispan.demo;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.persistence.cloud.configuration.CloudStoreConfigurationBuilder;

public class SwiftCacheStore {

    public static void main(String[] args) throws Exception { 
        ConfigurationBuilder cfg = new ConfigurationBuilder();
        cfg.persistence().addStore(CloudStoreConfigurationBuilder.class).provider("openstack-swift")
                .endpoint("http://10.8.188.11:5000/v2.0")
                .identity("tenant_name:user_name")
                .credential("password")
                .container("ispn-store");

        DefaultCacheManager cacheManager = new DefaultCacheManager(cfg.build());
        Cache<String, String> cache = cacheManager.getCache("test");
        cache.put("key", "value");
        System.out.printf("key = %s\n", cache.get("key"));
        
        cache.stop();
        cache.start();
        
        System.out.printf("key = %s\n", cache.get("key"));
        cacheManager.stop();
    }
}
