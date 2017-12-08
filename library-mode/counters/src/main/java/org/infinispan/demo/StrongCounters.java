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

public class StrongCounters {

    
    public static void main(String[] args) throws Exception {
        StrongCounter cnt = createCounter();
        for (int i = 0; i < 1_000; i++) {
            System.out.println(" -> " + cnt.incrementAndGet().get());
        }
    }
    
    public static StrongCounter createCounter() {
        GlobalConfigurationBuilder gcb = new GlobalConfigurationBuilder();
        gcb.clusteredDefault();
        
        CounterManagerConfigurationBuilder cntBuilder = gcb.addModule(CounterManagerConfigurationBuilder.class);
        cntBuilder.numOwner(2).reliability(Reliability.CONSISTENT);
        cntBuilder.addStrongCounter().name("cnt").initialValue(0).storage(Storage.VOLATILE);
        
        EmbeddedCacheManager cm = new DefaultCacheManager(gcb.build());
        CounterManager cntManager = EmbeddedCounterManagerFactory.asCounterManager(cm);
        return cntManager.getStrongCounter("cnt");
    }

}
