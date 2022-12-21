package org.infinispan.demo;

import static org.infinispan.demo.util.CacheOps.dumpCache;
import static org.infinispan.demo.util.CacheOps.onCache;
import static org.infinispan.demo.util.CacheOps.putTestKV;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.impl.ConfigurationProperties;
import org.infinispan.demo.util.SaslUtils;

/**
 * Example HR client app, which use SSL only for encryption communication between client and server, and also for HR
 * client authentication, using SASL EXTERANL mechanism. See conf/sasl_external_auth.xml how to configure ISPN server
 * for this use case.
 * 
 * @author vjuranek
 *
 */
public class HotRodClientSSL {

    public static final String ISPN_IP = "127.0.0.1";
    public static final String SASL_MECH = "EXTERNAL";

    private static final String KEYSTORE_PATH = "./certs/keystore_client.jks";
    private static final String KEYSTORE_PASSWORD = "secret";
    private static final String KEY_PASSWORD = "changeme";
    private static final String TRUSTSTORE_PATH = "./certs/truststore_client.jks";
    private static final String TRUSTSTORE_PASSWORD = "secret";

    public static void main(String[] args) {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.addServer().host(ISPN_IP).port(ConfigurationProperties.DEFAULT_HOTROD_PORT);
        //setup auth
        builder.security().authentication().saslMechanism(SASL_MECH).enable()
                .callbackHandler(new SaslUtils.VoidCallbackHandler());
        //setup encrypt
        builder.security().ssl().enable()
                .keyStoreFileName(KEYSTORE_PATH)
                .keyStorePassword(KEYSTORE_PASSWORD.toCharArray())
                .keyStoreCertificatePassword(KEY_PASSWORD.toCharArray())
                .trustStoreFileName(TRUSTSTORE_PATH)
                .trustStorePassword(TRUSTSTORE_PASSWORD.toCharArray());

        RemoteCacheManager cacheManager = new RemoteCacheManager(builder.build());
        RemoteCache<Object, Object> cache = cacheManager.getCache("respCache");

        onCache(cache, putTestKV.andThen(dumpCache));

        cacheManager.stop();
        System.exit(0);
    }
}
