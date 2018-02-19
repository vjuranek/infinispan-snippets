package org.infinispan.demo;

import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.counter.EmbeddedCounterManagerFactory;
import org.infinispan.counter.api.CounterManager;
import org.infinispan.counter.api.Storage;
import org.infinispan.counter.api.StrongCounter;
import org.infinispan.counter.configuration.CounterManagerConfigurationBuilder;
import org.infinispan.counter.configuration.Reliability;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

/**
 * Local counter is counter without configure transport layer, i.e. ISPN likely runs only on one node.
 * 
 * @author vjuranek
 *
 */
public class LocalCounter {

    public static void main(String[] args) throws Exception {
        EmbeddedCacheManager cm = configureCluster();
        StrongCounter cnt = createCounter(cm);

        for (int i = 0; i < 1_000; i++) {
            System.out.println(" -> " + cnt.incrementAndGet().get());
        }

        cm.stop();
    }

    public static EmbeddedCacheManager configureCluster() {
        GlobalConfigurationBuilder gcb = new GlobalConfigurationBuilder();
        gcb.clusteredDefault();

        CounterManagerConfigurationBuilder cntBuilder = gcb.addModule(CounterManagerConfigurationBuilder.class);
        cntBuilder.reliability(Reliability.CONSISTENT);
        cntBuilder.addStrongCounter().name("cnt").initialValue(0).storage(Storage.VOLATILE);

        return new DefaultCacheManager(gcb.build());
    }

    public static StrongCounter createCounter(EmbeddedCacheManager cm) {
        CounterManager cntManager = EmbeddedCounterManagerFactory.asCounterManager(cm);
        return cntManager.getStrongCounter("cnt");
    }
}
