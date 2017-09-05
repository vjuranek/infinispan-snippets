package org.infinispan.demo;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.StorageType;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.demo.model.Person;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

public class AdvancdedMarshalling {
    
    public static void main(String[] args) {
        storeBinary();
        storeObject();
        storeOffHeap();
    }
    
    private static void storeBinary() {
        putAndGet(StorageType.BINARY);
    }
    
    private static void storeObject() {
        putAndGet(StorageType.OBJECT);
    }
    
    private static void storeOffHeap() {
        putAndGet(StorageType.OFF_HEAP);
    }
    
    private static void putAndGet(StorageType storageType) {
        GlobalConfigurationBuilder gcb = new GlobalConfigurationBuilder();
        gcb.serialization().addAdvancedExternalizer(new PersonExternalizer());
        Configuration conf = new ConfigurationBuilder().memory().storageType(storageType).build();
        EmbeddedCacheManager ecm = new DefaultCacheManager(gcb.build(), conf);
        Cache<String, Person> cache = ecm.getCache();

        cache.put("person", new Person("name", "surname"));
        System.out.printf("%s storage\n", storageType);
        System.out.printf("Cache size: %d\n", cache.size());
        System.out.printf("Person %s\n", cache.get("person"));
        ecm.stop();
    }
}
