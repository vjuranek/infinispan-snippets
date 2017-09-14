package org.infinispan.demo;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

public class CacheListener {

    public static void main(String[] args) {
        Configuration conf = new ConfigurationBuilder().build();
        EmbeddedCacheManager cm = new DefaultCacheManager(conf);
        Cache<String, String> cache = cm.getCache();
        cache.addListener(new EntryListener());

        for (int i = 0; i < 100; i++) {
            cache.put("key" + i, "value" + i);
        }
        cm.stop();
    }
}
