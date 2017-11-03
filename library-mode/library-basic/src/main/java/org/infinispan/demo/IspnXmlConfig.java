package org.infinispan.demo;

import java.io.IOException;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

public class IspnXmlConfig {
    
    public static void main(String[] args) throws Exception {
        //basicXmlConfig();
        evictionXmlConfig();
    }
    
    public static void basicXmlConfig() throws IOException {
        EmbeddedCacheManager cm = new DefaultCacheManager("ispn-basic.xml");
        Cache<String, String> cache = cm.getCache();
        
        cache.put("key", "value");
        System.out.printf("%s -> %s", "key", cache.get("key"));
        
        cm.stop();
    }
    
    public static void evictionXmlConfig() throws IOException {
        EmbeddedCacheManager ecm = new DefaultCacheManager("ispn-eviction.xml");
        Cache<String, String> cache = ecm.getCache();

        for (int i = 0; i < 100; i++) {
            cache.put("key" + i, "value" + i);
        }
        System.out.printf("Eviction: cache size: %d\n", cache.size());
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
