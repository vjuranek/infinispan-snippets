package org.infinispan.demo;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.StorageType;
import org.infinispan.demo.model.Person;
import org.infinispan.demo.model.PersonWithExternalizer;
import org.infinispan.demo.model.SerializablePerson;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

public class BasicMarshalling {
    
    public static void main(String[] args) {
        storeBinary();
        storeObject();
        storeOffHeap();
    }
    
    private static void storeBinary() {
        Configuration conf = new ConfigurationBuilder().memory().storageType(StorageType.BINARY).build();
        EmbeddedCacheManager ecm = new DefaultCacheManager(conf);
        Cache<String, PersonWithExternalizer> cache = ecm.getCache();

        cache.put("person", new PersonWithExternalizer("name", "surname"));
        System.out.println("BINARY storage");
        System.out.printf("Cache size: %d\n", cache.size());
        System.out.printf("Person %s\n", cache.get("person"));
        ecm.stop();
    }
    
    private static void storeObject() {
        Configuration conf = new ConfigurationBuilder().memory().storageType(StorageType.OBJECT).build();
        EmbeddedCacheManager ecm = new DefaultCacheManager(conf);
        Cache<String, Person> cache = ecm.getCache();

        cache.put("person", new Person("name", "surname"));
        System.out.println("OBJECT storage");
        System.out.printf("Cache size: %d\n", cache.size());
        System.out.printf("Person %s\n", cache.get("person"));
        ecm.stop();
    }
    
    private static void storeOffHeap() {
        Configuration conf = new ConfigurationBuilder().memory().storageType(StorageType.OFF_HEAP).build();
        EmbeddedCacheManager ecm = new DefaultCacheManager(conf);
        Cache<String, PersonWithExternalizer> cache = ecm.getCache();

        cache.put("person", new PersonWithExternalizer("name", "surname"));
        System.out.println("OFF-HEAP storage");
        System.out.printf("Cache size: %d\n", cache.size());
        System.out.printf("Person %s\n", cache.get("person"));
        ecm.stop();
    }
    
}
