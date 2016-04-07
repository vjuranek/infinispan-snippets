package org.infinispan.demo;

import static org.infinispan.demo.util.CacheOps.dumpCache;
import static org.infinispan.demo.util.CacheOps.onCache;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.impl.ConfigurationProperties;
import org.infinispan.demo.util.SaslUtils;

public class HotRodClientSSL {

    public static final String ISPN_IP = "127.0.0.1";
    public static final String SERVER_NAME = "node0";
    public static final String SASL_MECH = "EXTERNAL";

    private static final String KEYSTORE_PATH = "./keystore_client.jks";
    private static final String KEYSTORE_PASSWORD = "secret";
    private static final String TRUSTSTORE_PATH = "./truststore_client.jks";
    private static final String TRUSTSTORE_PASSWORD = "secret";

    public static void main(String[] args) {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.addServer().host(ISPN_IP).port(ConfigurationProperties.DEFAULT_HOTROD_PORT);
        //setup auth
        builder.security().authentication().serverName(SERVER_NAME).saslMechanism(SASL_MECH).enable()
                .callbackHandler(new SaslUtils.VoidCallbackHandler());
        //setup encrypt
        builder.security().ssl().enable().keyStoreFileName(KEYSTORE_PATH)
                .keyStorePassword(KEYSTORE_PASSWORD.toCharArray()).trustStoreFileName(TRUSTSTORE_PATH)
                .trustStorePassword(TRUSTSTORE_PASSWORD.toCharArray());

        RemoteCacheManager cacheManager = new RemoteCacheManager(builder.build());
        RemoteCache<String, String> cache = cacheManager.getCache(RemoteCacheManager.DEFAULT_CACHE_NAME);

        onCache(cache, dumpCache);

        cacheManager.stop();
        System.exit(0);
    }
}
