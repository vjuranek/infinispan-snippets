package org.infinispan.demo;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

public class StateTransferOff {

    private static final int CLUSTER_WAIT = 10;

    public static void main(String[] args) throws Exception {
        testStateTransferOff();
    }

    public static void testStateTransferOff() {
        GlobalConfiguration gc = GlobalConfigurationBuilder.defaultClusteredBuilder().clusteredDefault().build();
        EmbeddedCacheManager ecm = new DefaultCacheManager(gc, (new ConfigurationBuilder()).build());

        Configuration cacheConf = new ConfigurationBuilder().clustering().cacheMode(CacheMode.REPL_SYNC).stateTransfer()
                .fetchInMemoryState(true).awaitInitialTransfer(false).build();
        ecm.defineConfiguration("testCache", cacheConf);
        Cache<String, String> cache = ecm.getCache("testCache");

        if (ecm.getTransport().getCoordinator().equals(ecm.getTransport().getAddress())) {
            populateCache(cache);
        }
        
        waitForCluster(ecm);
        dumpCache(cache);
        ecm.stop();
    }

    private static void populateCache(Cache<String, String> cache) {
        System.out.println("Populating cache " + cache.getCacheManager().getTransport().getAddress());
        for (int i = 0; i < 10; i++) {
            cache.put("key" + i, "value" + i);
        }
        dumpCache(cache);
        System.out.println("Cache populated, now sleeping for 10 sec., start another node now!");
    }

    private static void dumpCache(Cache<String, String> cache) {
        System.out.println("Dumping cache (" + cache.getCacheManager().getTransport().getAddress() + "):");
        System.out.println("Cache size: " + cache.size());
        System.out.println("--------------");
        if (cache.size() == 0) {
            System.out.println("No entries in the cache");
        } else {
            for (String key : cache.keySet()) {
                System.out.printf("%s -> %s\n", key, cache.get(key));
            }
        }
    }

    private static void waitForCluster(EmbeddedCacheManager ecm) {
        for (int i = 0; i < CLUSTER_WAIT; i++) {
            boolean isClusterFormed = ecm.getTransport().getMembers().size() > 1;
            //always sleep at least once so that if the cluster is formed, another node knows about it as well
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isClusterFormed) {
                System.out.println("Cluster formed!");
                break;
            } else {
                System.out.println("Waiting for cluster another 10 sec.");
            }
        }
    }

}
