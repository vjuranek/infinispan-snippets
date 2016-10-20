package org.infinispan.demo;

import java.util.List;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.demo.model.Person;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.query.Search;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;

public class BasicQuery {
    public static void main(String[] args) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        EmbeddedCacheManager cm = new DefaultCacheManager(cb.build());
        Cache<String, Person> cache = cm.getCache();
        cache.put("person1", new Person("Will", "Shakespeare"));

        QueryFactory queryFactory = Search.getQueryFactory(cache);
        Query query = queryFactory.from(Person.class).having("name").eq("Will").toBuilder().build();
        List<Person> matches = query.list();
        matches.forEach(person -> System.out.printf("Query result [name, surname]: [%s, %s]", person.getName(),
                person.getSurname()));

        cm.stop();
    }
}
