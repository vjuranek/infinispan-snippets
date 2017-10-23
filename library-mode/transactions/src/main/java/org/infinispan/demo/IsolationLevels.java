package org.infinispan.demo;

import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.TransactionManager;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.transaction.LockingMode;
import org.infinispan.transaction.TransactionMode;
import org.infinispan.util.concurrent.IsolationLevel;

public class IsolationLevels {
    
    private static final Logger LOG = Logger.getLogger(IsolationLevels.class.getName());
    private static final Level LOG_LEVEL = Level.INFO;
    private static final String KEY = "key";
    
    public static void main(String[] args) throws Exception {
        System.out.println("OPTIMISTIC, READ COMMITED");
        runConcurentReadCommited();
        System.out.println("PESSIMISTIC, REPEATABLE READ");
        runConcurentRepeatableRead();
    }
    
    public static void runConcurentReadCommited() throws Exception {
        EmbeddedCacheManager cm = getTxCache(IsolationLevel.READ_COMMITTED);
        Cache<String, String> cache = cm.getCache();
        runTxs(cache);
        cm.stop();
    }
    
    public static void runConcurentRepeatableRead() throws Exception {
        EmbeddedCacheManager cm = getTxCache(IsolationLevel.REPEATABLE_READ);
        Cache<String, String> cache = cm.getCache();
        runTxs(cache);
        cm.stop();
    }
    
    public static void runTxs(Cache<String, String> cache) throws Exception {
        CyclicBarrier otherTxStart = new CyclicBarrier(2);
        CyclicBarrier otherTxEnd = new CyclicBarrier(2);
        
        Runnable tx1Runnable = () -> {
            try {
                TransactionManager tm = cache.getAdvancedCache().getTransactionManager();
                tm.begin();
                cache.put(KEY, "AAA");
                tm.commit();
                
                LOG.log(LOG_LEVEL, "Starting TX1");
                tm.begin();
                LOG.log(LOG_LEVEL, String.format("TX1 before TX2 begins sees %s",  cache.get(KEY)));
                otherTxStart.await();
                otherTxEnd.await();
                LOG.log(LOG_LEVEL, String.format("TX1 after TX2 finished sees %s",  cache.get(KEY)));
                tm.commit();
                LOG.log(LOG_LEVEL, "TX1 finished");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        };
        
        Runnable tx2Runnable = () -> {
            try {
                TransactionManager tm = cache.getAdvancedCache().getTransactionManager();
                
                otherTxStart.await();
                LOG.log(LOG_LEVEL, "Starting TX2");
                tm.begin();
                LOG.log(LOG_LEVEL, String.format("TX2 on the beginning sees %s",  cache.get(KEY)));
                cache.put(KEY, "BBB");
                LOG.log(LOG_LEVEL, String.format("TX2 at the end sees %s",  cache.get(KEY)));
                tm.commit();
                otherTxEnd.await();
                LOG.log(LOG_LEVEL, "TX2 finished");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        };
        
        Thread t1 = new Thread(tx1Runnable);
        Thread t2 = new Thread(tx2Runnable);
        t1.start();
        t2.start();
        
        t1.join(1000);
        t2.join(1000);
    }
    
    public static EmbeddedCacheManager getTxCache(IsolationLevel isolation) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb
        .locking()
            .isolationLevel(isolation)
        .transaction()
            .lockingMode(LockingMode.OPTIMISTIC)
            .autoCommit(false) //we will handle transaction boundaries manually
            .transactionMode(TransactionMode.TRANSACTIONAL);
        return new DefaultCacheManager(cb.build());
    }

}
