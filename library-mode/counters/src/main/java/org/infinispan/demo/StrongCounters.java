package org.infinispan.demo;

import java.util.concurrent.TimeUnit;

import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.counter.EmbeddedCounterManagerFactory;
import org.infinispan.counter.api.CounterManager;
import org.infinispan.counter.api.Storage;
import org.infinispan.counter.api.StrongCounter;
import org.infinispan.counter.configuration.CounterManagerConfigurationBuilder;
import org.infinispan.counter.configuration.Reliability;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

public class StrongCounters {

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
        gcb.transport()
            .addProperty("configurationFile", "default-configs/default-jgroups-tcp.xml")
            .initialClusterSize(2)
            .initialClusterTimeout(10, TimeUnit.SECONDS);

        CounterManagerConfigurationBuilder cntBuilder = gcb.addModule(CounterManagerConfigurationBuilder.class);
        cntBuilder.numOwner(2).reliability(Reliability.CONSISTENT);
        cntBuilder.addStrongCounter().name("cnt").initialValue(0).storage(Storage.VOLATILE);

        return new DefaultCacheManager(gcb.build());
    }

    public static StrongCounter createCounter(EmbeddedCacheManager cm) {
        CounterManager cntManager = EmbeddedCounterManagerFactory.asCounterManager(cm);
        return cntManager.getStrongCounter("cnt");
    }

}
