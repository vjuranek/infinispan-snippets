package org.infinispan.demo;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

public class CacheListener {

    public static void main(String[] args) {
        EmbeddedCacheManager cm = new DefaultCacheManager();
        Cache<String, String> cache = cm.getCache();
        cache.addListener(new EntryCreatedListener());

        for (int i = 0; i < 100; i++) {
            cache.put("key" + i, "value" + i);
        }
        cm.stop();
    }
}
