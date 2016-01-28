package org.infinispan.demo;

import java.util.HashSet;
import java.util.Set;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.annotation.ClientCacheEntryCreated;
import org.infinispan.client.hotrod.annotation.ClientCacheEntryModified;
import org.infinispan.client.hotrod.annotation.ClientListener;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.event.ClientCacheEntryCreatedEvent;
import org.infinispan.client.hotrod.event.ClientCacheEntryModifiedEvent;
import org.infinispan.client.hotrod.impl.ConfigurationProperties;

public class TemperatureClient {

    public static final String ISPN_IP = "127.0.0.1";
    public static final String CACHE_NAME = "avg-temperatures";

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("You have to provide list of places to watch, at least one!");
            System.exit(1);
        }
        Set<String> placesToWatch = new HashSet<>(args.length);
        for (String place : args) {
            placesToWatch.add(place);
        }

        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.addServer().host(ISPN_IP).port(ConfigurationProperties.DEFAULT_HOTROD_PORT);
        RemoteCacheManager cacheManager = new RemoteCacheManager(builder.build());
        RemoteCache<String, Double> cache = cacheManager.getCache(CACHE_NAME);

        AvgTemperatureListner avgTempListener = new AvgTemperatureListner(cache, placesToWatch);
        cache.addClientListener(avgTempListener);
        System.out.println("Client will be listening to avg. temperature updates for 5 minutes");
        Thread.sleep(5 * 60 * 1000);

        System.out.println("Stopping client");
        cache.removeClientListener(avgTempListener);
        cacheManager.stop();
        System.exit(0);
    }

    @ClientListener
    public static class AvgTemperatureListner {
        private final RemoteCache<String, Double> cache;
        private final Set<String> watchedPlaces;

        public AvgTemperatureListner(RemoteCache<String, Double> cache, Set<String> watchedPlaces) {
            this.cache = cache;
            this.watchedPlaces = watchedPlaces;
        }

        @ClientCacheEntryCreated
        public void entryCreated(ClientCacheEntryCreatedEvent<String> event) {
            if (watchedPlaces.contains(event.getKey()))
                updateAction(event.getKey());
        }

        @ClientCacheEntryModified
        public void entryModified(ClientCacheEntryModifiedEvent<String> event) {
            if (watchedPlaces.contains(event.getKey()))
                updateAction(event.getKey());
        }

        private void updateAction(String key) {
            System.out.printf("[%s] avg. temperature is now %.1f \u00B0C%n", key, cache.get(key));
        }
    }
}
