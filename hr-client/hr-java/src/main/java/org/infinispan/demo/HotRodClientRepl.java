package org.infinispan.demo;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.impl.ConfigurationProperties;

public class HotRodClientRepl {

    public static final String ISPN_IP = "127.0.0.1";
    public static final String CACHE_NAME = "replicated";

    public static void main(String[] args) throws Exception {
        
        // run cluster of two servers with replicated cache using predefined replicated confing
        // ./standalone.sh -c clustered.xml
        // ./standalone.sh -c clustered.xml -Djboss.socket.binding.port-offset=100
        // later on kill the first server, listening on default HR port 11222
        
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.addServer().host(ISPN_IP).port(ConfigurationProperties.DEFAULT_HOTROD_PORT);
        RemoteCacheManager cacheManager = new RemoteCacheManager(builder.build());
        
        RemoteCache<Object, Object> cache = cacheManager.getCache();
        cache.put("key", "value");
        
        System.out.println("key pushed into cluter, kill server 1 listening on 127.0.0.1:11222");
        System.out.println("sleeping for 15s");
        
        Thread.sleep(15_000);
        
        System.out.println("getting key from cluster");
        System.out.printf("got value '%s'\n", cache.get("key"));
        
        cacheManager.stop();
    }
    
}
