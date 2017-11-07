package org.infinispan.demo;

import org.infinispan.client.hotrod.ProtocolVersion;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;

public class Compatibility {

    public static final String ISPN_IP = "127.0.0.1";

    public static void main(String[] args) {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.addServer().host(ISPN_IP).port(11222).version(ProtocolVersion.PROTOCOL_VERSION_24);
        RemoteCacheManager cacheManager = new RemoteCacheManager(builder.build());
        RemoteCache<String, String> cache = cacheManager.getCache();


	cache.put("java-key", "java-value");
	System.out.println("Java value from cache: " + cache.get("java-key"));
	String cppVal = cache.get("cpp-key");
	if (cppVal != null) {
	    System.out.println("C++ value from cache: " + cppVal);
	}
	
        cacheManager.stop();
    }

}
