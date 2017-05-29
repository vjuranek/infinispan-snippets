package org.infinispan.demo;

import java.util.Properties;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

/**
 * Simple app, which accesses cache via JCahce API and use EXTERNAL SASL auth to authneticate itself to the server.
 * All important setup is done via properties, see src/main/resources/hotrod-client.properties.
 * Appropriate ISPN server config is conf/sasl_external_auth.xml and certificates in cert dir.
 * Please note you need copy both keystore_server.jks and truststore_server.jks in server standalone/configuration
 * dir have fully working ISPN server. 
 *
 * @author vjuranek
 *
 */
public class JCacheExternalAuth {
    
    private static final String CACHE_NAME = "default";
    
    public static void main(String[] args) {
        CachingProvider jcacheProvider = Caching.getCachingProvider();
        CacheManager cm = jcacheProvider.getCacheManager();

        Properties prop = cm.getProperties();
        System.out.println("CACHE MANAGER PROPERTIES:");
        System.out.println("-------------------------");
        for (Object key : prop.keySet()) {
            System.out.println(String.format("%s -> %s", key, prop.get(key)));
        }
        System.out.println("-------------------------");
        
        Cache<String, String> cache = cm.getCache(CACHE_NAME);
        cache.put("key", "value");
        System.out.println("Key read from cache: " + cache.get("key"));
        
        cm.close();
        System.exit(0);
    }

}
