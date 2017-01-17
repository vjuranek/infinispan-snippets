package org.infinispan.demo;

import org.infinispan.filter.NamedFactory;
import org.infinispan.notifications.cachelistener.filter.CacheEventConverter;
import org.infinispan.notifications.cachelistener.filter.CacheEventConverterFactory;

@NamedFactory(name = "keyValueConverterFactory")
public class KeyValueConverterFactory implements CacheEventConverterFactory {
    public KeyValueConverterFactory() {
    }

    @Override
    public <K, V, C> CacheEventConverter<K, V, C> getConverter(Object[] params) {
       return (CacheEventConverter<K, V, C>) new KeyValueConverter();
    }
 }