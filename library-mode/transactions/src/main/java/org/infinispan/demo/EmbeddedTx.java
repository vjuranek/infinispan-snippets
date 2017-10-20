package org.infinispan.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.transaction.TransactionManager;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.demo.tx.TxWriter;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.transaction.LockingMode;
import org.infinispan.transaction.TransactionMode;
import org.infinispan.transaction.lookup.GenericTransactionManagerLookup;
import org.infinispan.util.concurrent.IsolationLevel;

public class EmbeddedTx {

    private static final int writerCount = 2;
    private static final int updateCoutn = 10;
    
    public static void main(String[] args) throws Exception {
        Cache<String, String> cache = embeddedTxCache();
        runConcurentWrites(cache, "key");
    }
    
    public static void runConcurentWrites(Cache<String, String> cache, String key) throws Exception {
        TransactionManager tm = cache.getAdvancedCache().getTransactionManager();
        tm.begin();
        cache.put(key, "init value"); // avoid failed NPE in replace method
        tm.commit();
        
        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 0; i < writerCount; i++) {
            es.submit(new TxWriter(cache, key, updateCoutn));
        }
        es.shutdown();
        es.awaitTermination(10, TimeUnit.SECONDS);
    }
    
    public static Cache<String, String> embeddedTxCache() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb
        .locking()
            .isolationLevel(IsolationLevel.READ_COMMITTED)
        .transaction()
            .lockingMode(LockingMode.OPTIMISTIC)
            .autoCommit(false) //we will handle transaction boundaries manually
            .completedTxTimeout(60_000)
            .transactionMode(TransactionMode.TRANSACTIONAL)
            .transactionManagerLookup(new GenericTransactionManagerLookup()); // defualt to EmbeddedTransactionManagerLookup if no other TM lookup is found
        EmbeddedCacheManager cm = new DefaultCacheManager(cb.build());
        return cm.getCache();
    }
}
