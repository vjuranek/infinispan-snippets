package org.infinispan.demo;

import java.util.List;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.Index;
import org.infinispan.demo.model.PersonIndexed;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.query.Search;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;

public class IndexedQuery {
    public static void main(String[] args) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.indexing().index(Index.ALL).addIndexedEntity(PersonIndexed.class).addProperty("default.directory_provider",
                "ram");
        EmbeddedCacheManager cm = new DefaultCacheManager(cb.build());
        Cache<String, PersonIndexed> cache = cm.getCache();
        cache.put("person1", new PersonIndexed("Will", "Shakespeare"));

        QueryFactory queryFactory = Search.getQueryFactory(cache);
        Query query = queryFactory.from(PersonIndexed.class).having("name").eq("Will").toBuilder().build();
        List<PersonIndexed> matches = query.list();
        
        matches.forEach(person -> System.out.printf("Query result [name, surname]: [%s, %s]", ((PersonIndexed)person).getName(),
                ((PersonIndexed)person).getSurname()));

        cm.stop();
    }
}
