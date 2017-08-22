package org.infinispan.demo;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntriesEvicted;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.event.CacheEntriesEvictedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;

@Listener
public class EntryListener {

    private static Logger LOG = Logger.getLogger(EntryListener.class.getName());
    private static Level LOG_LEVEL = Level.INFO;
    
    @CacheEntryCreated
    public void onCreated(CacheEntryCreatedEvent<String, String> e) {
        if (!e.isPre()) {
            LOG.log(LOG_LEVEL, String.format("Created %s -> %s", e.getKey(), e.getValue()));
        }
    }
    
    @CacheEntriesEvicted
    public void onEviction(CacheEntriesEvictedEvent<String, String> events) {
        Map<String, String> entires = events.getEntries();
        for (String key : entires.keySet()) {
            LOG.log(LOG_LEVEL, String.format("Evicted %s -> %s", key, entires.get(key)));
        }
    }
}
