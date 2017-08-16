package org.infinispan.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.impl.ConfigurationProperties;

public class Metadata {

    public static final String ISPN_IP = "127.0.0.1";
    public static final String KEY = "key";
    public static final String VALUE_PREXIF = "value";
    public static final String CACHE_NAME = "default";

    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(10);
        Replacer r1 = new Replacer(CACHE_NAME);
        Replacer r2 = new Replacer(CACHE_NAME);
        es.execute(r1);
        es.execute(r2);
        es.shutdown();
    }

    private static class Replacer implements Runnable {

        private final String threadName = Thread.currentThread().getName();
        private final String cacheName;
        private final RemoteCacheManager cacheManager;

        public Replacer(String cacheName) {
            this.cacheName = cacheName;

            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.addServer().host(ISPN_IP).port(ConfigurationProperties.DEFAULT_HOTROD_PORT);
            cacheManager = new RemoteCacheManager(builder.build());
        }

        public void run() {
            RemoteCache<Object, Object> cache = cacheManager.getCache(cacheName);

            for (int i = 0; i < 10; i++) {
                boolean replaced = cache.replaceWithVersion(KEY, VALUE_PREXIF + i, i);
                if (replaced) {
                    System.out.println(String.format("[%s] %d: replacing with result %b", threadName, i, replaced));
                } else {
                    System.out.println(String.format("[%s] Failed to replace, using just put", threadName));
                    cache.put(KEY, VALUE_PREXIF + i);
                    System.out.println(String.format("[%s] Stored with version %d", threadName,
                            cache.getWithMetadata(KEY).getVersion()));
                }
            }

            cacheManager.stop();
        }
    }

}
