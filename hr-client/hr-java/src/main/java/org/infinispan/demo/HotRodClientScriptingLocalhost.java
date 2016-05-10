package org.infinispan.demo;

import static org.infinispan.demo.util.CacheOps.dumpCache;
import static org.infinispan.demo.util.CacheOps.onCache;
import static org.infinispan.demo.util.CacheOps.putTestKV;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.impl.ConfigurationProperties;
import org.infinispan.demo.util.IOUtil;

public class HotRodClientScriptingLocalhost {
    
    public static final String ISPN_IP = "127.0.0.1";
    public static final String SCRIPT_CACHE = "___script_cache";
    
    public static void main(String[] args) throws Exception {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.addServer().host(ISPN_IP).port(ConfigurationProperties.DEFAULT_HOTROD_PORT);
        
        RemoteCacheManager cacheManager = new RemoteCacheManager(builder.build());
        RemoteCache<Object, Object> cache = cacheManager.getCache(RemoteCacheManager.DEFAULT_CACHE_NAME);

        //populate cache with some data
        onCache(cache, putTestKV.andThen(dumpCache));
        
        RemoteCache scriptCache = cacheManager.getCache(SCRIPT_CACHE);
        scriptCache.put("cacheSize.js", IOUtil.getScript("script/cacheSize.js"));
        
        int cacheSize = cache.execute("cacheSize.js", null);
        System.out.println("!!Cache size: " + cacheSize);

        cacheManager.stop();
        System.exit(0);
    }

}
