package org.infinispan.demo;

import static org.infinispan.demo.util.CacheOps.dumpCache;
import static org.infinispan.demo.util.CacheOps.onCache;
import static org.infinispan.demo.util.CacheOps.putTestKV;
import static org.infinispan.demo.util.CmdArgs.CACHE_NAME_KEY;
import static org.infinispan.demo.util.CmdArgs.LOGIN_KEY;
import static org.infinispan.demo.util.CmdArgs.PASS_KEY;
import static org.infinispan.demo.util.CmdArgs.getCredentials;

import java.util.Map;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.impl.ConfigurationProperties;
import org.infinispan.demo.util.SaslUtils.SimpleLoginHandler;

public class HotRodClientSecured {

    public static final String ISPN_IP = "127.0.0.1";
    public static final String SERVER_NAME = "node0";
    public static final String SASL_MECH = "DIGEST-MD5";

    private static final String SECURITY_REALM = "ApplicationRealm";

    public static void main(String[] args) {
        Map<String, String> userArgs = null;
        try {
            userArgs = getCredentials(args);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.err.println(
                    "Invalid credentials format, plase provide credentials (and optionally cache name) with --cache=<cache> --user=<user> --password=<password>");
            System.exit(1);
        }

        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.addServer().host(ISPN_IP).port(ConfigurationProperties.DEFAULT_HOTROD_PORT);
        builder.security().authentication().serverName(SERVER_NAME).saslMechanism(SASL_MECH).enable().callbackHandler(
                new SimpleLoginHandler(userArgs.get(LOGIN_KEY), userArgs.get(PASS_KEY), SECURITY_REALM));

        RemoteCacheManager cacheManager = new RemoteCacheManager(builder.build());
        RemoteCache<Object, Object> cache = cacheManager.getCache(userArgs.containsKey(CACHE_NAME_KEY)
                ? userArgs.get(CACHE_NAME_KEY) : "respCache");

        onCache(cache, putTestKV.andThen(dumpCache));

        cacheManager.stop();
        System.exit(0);
    }

}
