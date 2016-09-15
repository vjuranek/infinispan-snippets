package org.infinispan.demo;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.persistence.cloud.configuration.CloudStoreConfigurationBuilder;

public class S3CacheStore {

    public static void main(String[] args) throws Exception { 
        ConfigurationBuilder cfg = new ConfigurationBuilder();
        cfg.persistence().addStore(CloudStoreConfigurationBuilder.class)
            .provider("aws-s3")
            .endpoint("http://s3.amazonaws.com")
            .identity("Access Key ID")
            .credential("Secret Access Key")
            .container("ispn-store")
            .location("eu-central-1");

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
