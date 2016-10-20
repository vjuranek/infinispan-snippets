package org.infinispan.demo;

import javax.enterprise.inject.Produces;

import org.infinispan.cdi.embedded.ConfigureCache;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;

public class TestCacheConfig {
    
    @ConfigureCache("testcache")
    @TestCache
    @Produces
    public Configuration greetingCacheConfiguration() {
        return new ConfigurationBuilder().eviction().strategy(EvictionStrategy.LRU).size(5).build();
    }

}
