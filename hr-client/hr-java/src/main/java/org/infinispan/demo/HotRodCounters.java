package org.infinispan.demo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.RemoteCounterManagerFactory;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.impl.ConfigurationProperties;
import org.infinispan.counter.api.CounterManager;
import org.infinispan.counter.api.StrongCounter;

public class HotRodCounters {
    
    private static final String ISPN_IP = "127.0.0.1";
    private static final String DEFAULT_COUNTER_NAME = "test-counter";

    public static void main(String[] args) {
        String counterName = args.length > 0 ? args[0] : DEFAULT_COUNTER_NAME;
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.addServer().host(ISPN_IP).port(ConfigurationProperties.DEFAULT_HOTROD_PORT);
        RemoteCacheManager cacheManager = new RemoteCacheManager(builder.build());
        CounterManager rcm = RemoteCounterManagerFactory.asCounterManager(cacheManager);
        StrongCounter cnt = rcm.getStrongCounter(counterName);
        
        futureGet(cnt.reset());
        System.out.printf("[Start] Counter value: %d\n", futureGet(cnt.getValue()));
        for (long i = 0; i < 100; i++) {
            System.out.printf("[Loop] Counter value: %d\n", futureGet(cnt.incrementAndGet()));
        }
        System.out.printf("[Stop] Counter value: %d\n", futureGet(cnt.getValue()));
        
        cacheManager.stop();
    }
    
    public static Object futureGet(CompletableFuture<?> f) {
        Object result = null;
        try {
            result = f.get(5, TimeUnit.SECONDS);
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
