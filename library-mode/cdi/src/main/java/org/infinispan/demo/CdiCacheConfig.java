package org.infinispan.demo;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.infinispan.Cache;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

@ApplicationScoped
public class CdiCacheConfig {
    
    @Inject
    @TestCache
    private Cache<String, String> cache;
    
    public static void main(String[] args) throws Exception {
        Weld weld = new Weld();
        try(WeldContainer container = weld.initialize()) {
            CdiCacheConfig app = container.instance().select(CdiCacheConfig.class).get();
            app.run();
        }
    }
    
    public void run() {
        for (int i = 0; i < 100; i++) {
            cache.put("key" + i, "value" + i);
        }
        System.out.printf("Eviction: cache size: %d\n", cache.size());
    }

}
