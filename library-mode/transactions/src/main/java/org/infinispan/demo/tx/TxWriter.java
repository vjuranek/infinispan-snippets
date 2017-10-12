package org.infinispan.demo.tx;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.TransactionManager;

import org.infinispan.Cache;

public class TxWriter implements Runnable {

    private static final Logger LOG = Logger.getLogger(TxWriter.class.getName());
    private static final Level LOG_LEVEL = Level.INFO;

    private final Cache<String, String> cache;
    private final TransactionManager tm;
    private final String key;
    private final int updates;

    public TxWriter(Cache<String, String> cache, String key, int updates)  {
        this.cache = cache;
        this.tm = cache.getAdvancedCache().getTransactionManager();
        this.key = key;
        this.updates = updates;
    }

    public void run() {
        String threadName = Thread.currentThread().getName();
        for (int i = 0; i < updates; i++) {
            try {
                tm.begin();
                String curValue = cache.get(key);
                LOG.log(LOG_LEVEL, String.format("Thread name %s count %d see value %s", threadName, i, curValue));
                String newValue = threadName + "---" + i;
                boolean replaced = cache.replace(key, curValue, newValue);
                if (!replaced) {
                    LOG.log(Level.SEVERE, String.format("Thread %s failed to replace %s with %s", threadName, curValue, newValue));
                }
                tm.commit();
                LOG.log(LOG_LEVEL, String.format("Thread name %s count %d updated to %s", threadName, i, newValue));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
