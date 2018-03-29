package org.infinispan.demo;

import java.io.InputStream;
import java.util.Properties;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;

public class HotRodPropertiesConf {
    
private static final String CACHE_NAME = "default";
    
    public static void main(String[] args) throws Exception {
        
        Properties hrConf = new Properties();
        InputStream is = HotRodPropertiesConf.class.getResourceAsStream("/hotrod-client-simple.properties");
        hrConf.load(is);
        is.close();
        
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.withProperties(hrConf);
        RemoteCacheManager cm = new RemoteCacheManager(builder.build());
        
        RemoteCache<String, String> cache = cm.getCache(CACHE_NAME);
        cache.put("key", "value");
        System.out.println("Key read from cache: " + cache.get("key"));
        
        cm.stop();
        System.exit(0);
    }

}
