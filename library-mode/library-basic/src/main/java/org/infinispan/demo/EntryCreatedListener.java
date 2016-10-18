package org.infinispan.demo;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;

@Listener
public class EntryCreatedListener {

    private static Logger LOG = Logger.getLogger(EntryCreatedListener.class.getName());
    private static Level LOG_LEVEL = Level.INFO;
    
    @CacheEntryCreated
    public void onCreated(CacheEntryCreatedEvent<String, String> e) {
        if (!e.isPre()) {
            LOG.log(LOG_LEVEL, String.format("Created %s -> %s", e.getKey(), e.getValue()));
        }
    }
}
