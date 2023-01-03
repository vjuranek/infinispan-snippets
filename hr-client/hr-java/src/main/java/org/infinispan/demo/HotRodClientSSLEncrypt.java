package org.infinispan.demo;

import static org.infinispan.demo.util.CacheOps.dumpCache;
import static org.infinispan.demo.util.CacheOps.putTestKV;
import static org.infinispan.demo.util.CacheOps.onCache;

import javax.net.ssl.SSLContext;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.impl.ConfigurationProperties;
import org.infinispan.commons.util.SslContextFactory;

/**
 * Example HR client app, which use SSL only for encryption communication between client and server, not for client
 * authentication. Example ISPN server configuration can be found in conf/sasl_encryption_only.xml. 
 * 
 * @author vjuranek
 *
 */
public class HotRodClientSSLEncrypt {

    public static final String ISPN_IP = "127.0.0.1";

    private static final String TRUSTSTORE_PATH = "./truststore_client.jks";
    private static final String TRUSTSTORE_PASSWORD = "secret";

    public static void main(String[] args) {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.addServer().host(ISPN_IP).port(ConfigurationProperties.DEFAULT_HOTROD_PORT);

        // SSLContext cont = SslContextFactory.getContext(null, null, TRUSTSTORE_PATH, TRUSTSTORE_PASSWORD.toCharArray());
        // builder.security().ssl().sslContext(cont).enable();
        builder.security().ssl().trustStoreFileName(TRUSTSTORE_PATH).trustStorePassword(TRUSTSTORE_PASSWORD.toCharArray());

        RemoteCacheManager cacheManager = new RemoteCacheManager(builder.build());
        RemoteCache<Object, Object> cache = cacheManager.getCache("respCache");

        onCache(cache, putTestKV.andThen(dumpCache));

        cacheManager.stop();
        System.exit(0);
    }

}
