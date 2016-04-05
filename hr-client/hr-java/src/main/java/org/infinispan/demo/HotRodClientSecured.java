package org.infinispan.demo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.RealmCallback;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.impl.ConfigurationProperties;

public class HotRodClientSecured {

    public static final String ISPN_IP = "127.0.0.1";
    public static final String SERVER_NAME = "node0";
    public static final String SASL_MECH = "DIGEST-MD5";

    private static final String SECURITY_REALM = "ApplicationRealm";
    private static final String CACHE_NAME_KEY = "cache";
    private static final String LOGIN_KEY = "user";
    private static final String PASS_KEY = "password";
    private static final String PARAM_PREFIX = "--";
    private static final String PARAM_SEP = "=";
    
    private static final String KEYSTORE_PATH = "./keystore_client.jks";
    private static final String KEYSTORE_PASSWORD = "secret";
    private static final String TRUSTSTORE_PATH = "./truststore_client.jks";
    private static final String TRUSTSTORE_PASSWORD = "secret";

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
        //setup auth
        builder.security().authentication().serverName(SERVER_NAME).saslMechanism(SASL_MECH).enable().callbackHandler(
                new SimpleLoginHandler(userArgs.get(LOGIN_KEY), userArgs.get(PASS_KEY), SECURITY_REALM));
        //setup encrypt
        builder.security().ssl().enable().keyStoreFileName(KEYSTORE_PATH)
                .keyStorePassword(KEYSTORE_PASSWORD.toCharArray()).trustStoreFileName(TRUSTSTORE_PATH)
                .trustStorePassword(TRUSTSTORE_PASSWORD.toCharArray());

        RemoteCacheManager cacheManager = new RemoteCacheManager(builder.build());
        RemoteCache<String, String> cache = cacheManager.getCache(userArgs.containsKey(CACHE_NAME_KEY)
                ? userArgs.get(CACHE_NAME_KEY) : RemoteCacheManager.DEFAULT_CACHE_NAME);

        //cacheSize.andThen(dumpCache).apply(cache);
        //onCache(cache, cacheSize.andThen(dumpCache));
        onCache(cache, dumpCache);

        cacheManager.stop();
        System.exit(0);
    }

    private static Function<RemoteCache<?, ?>, RemoteCache<?, ?>> dumpCache = cache -> {
        Map<?, ?> entries = cache.getBulk();
        System.out.printf("Number of obtained entries: %d%n", entries.size());
        for (Object key : entries.keySet()) {
            System.out.printf("[%s -> %s]%n", key, entries.get(key));
        }
        return cache;
    };

    private static Function<RemoteCache<?, ?>, RemoteCache<?, ?>> cacheSize = cache -> {
        System.out.printf("Cahce size: %d%n", cache.size());
        return cache;
    };

    private static void onCache(RemoteCache<?, ?> cache, Function<RemoteCache<?, ?>, RemoteCache<?, ?>> cacheFunction) {
        cacheFunction.apply(cache);
    }

    private static Map<String, String> getCredentials(String[] args) {
        if (args.length < 2)
            throw new IllegalArgumentException("At least login and password required!");

        Map<String, String> userArgs = new HashMap<>();
        for (String arg : args) {
            String[] argArray = extractParam(arg);
            switch (argArray[0]) {
            case PARAM_PREFIX + CACHE_NAME_KEY:
                userArgs.put(CACHE_NAME_KEY, argArray[1]);
                break;
            case PARAM_PREFIX + LOGIN_KEY:
                userArgs.put(LOGIN_KEY, argArray[1]);
                break;
            case PARAM_PREFIX + PASS_KEY:
                userArgs.put(PASS_KEY, argArray[1]);
                break;
            default:
                throw new IllegalArgumentException("Unknown argument " + argArray[0]);
            }
        }
        return userArgs;
    }

    private static String[] extractParam(String paramStr) {
        String[] param = paramStr.split(PARAM_SEP, 2);
        if (param.length != 2)
            throw new IllegalArgumentException("Specify arguments as --key=value");
        return new String[] { param[0], param[1] };
    }

    public static class SimpleLoginHandler implements CallbackHandler {
        final private String login;
        final private String password;
        final private String realm;

        public SimpleLoginHandler(String login, String password) {
            this.login = login;
            this.password = password;
            this.realm = null;
        }

        public SimpleLoginHandler(String login, String password, String realm) {
            this.login = login;
            this.password = password;
            this.realm = realm;
        }

        @Override
        public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
            for (Callback callback : callbacks) {
                if (callback instanceof NameCallback) {
                    ((NameCallback) callback).setName(login);
                } else if (callback instanceof PasswordCallback) {
                    ((PasswordCallback) callback).setPassword(password.toCharArray());
                } else if (callback instanceof RealmCallback) {
                    ((RealmCallback) callback).setText(realm);
                } else {
                    throw new UnsupportedCallbackException(callback);
                }
            }
        }
    }

}
