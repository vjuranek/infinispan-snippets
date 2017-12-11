package org.infinispan.demo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.lock.EmbeddedClusteredLockManagerFactory;
import org.infinispan.lock.api.ClusteredLock;
import org.infinispan.lock.api.ClusteredLockManager;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

public class NodeLock {

    private static final String LOCK_NAME = "testLock";

    public static void main(String[] args) throws Exception {
        EmbeddedCacheManager cm = configureCluster();
        ClusteredLock lock = createLock(LOCK_NAME, cm);

        for (int i = 0; i < 1_000; i++) {
            System.out.println(" ... " + i);
            lock.tryLock().thenCompose(res -> {
                if (res) {
                    System.out.println(" -> LOCKED!");
                    return lock.unlock();
                } else {
                    System.out.println(" x> failed to get lock");
                    return CompletableFuture.completedFuture(null);
                }
            });
        }

        cm.stop();
    }

    public static EmbeddedCacheManager configureCluster() {
        GlobalConfigurationBuilder gcb = new GlobalConfigurationBuilder();
        gcb.clusteredDefault();
        gcb.transport().addProperty("configurationFile", "default-configs/default-jgroups-tcp.xml")
                .initialClusterSize(2).initialClusterTimeout(10, TimeUnit.SECONDS);

        return new DefaultCacheManager(gcb.build());
    }

    public static ClusteredLock createLock(String lockName, EmbeddedCacheManager cm) {
        ClusteredLockManager lm = EmbeddedClusteredLockManagerFactory.from(cm);
        ClusteredLock lock = null;
        if (lm.isDefined(lockName)) {
            lock = lm.get(lockName);
        } else {
            if (lm.defineLock(lockName)) {
                lock = lm.get(lockName);
            } else {
                throw new IllegalStateException("cannot create lock " + lockName);
            }
        }
        return lock;
    }

}
