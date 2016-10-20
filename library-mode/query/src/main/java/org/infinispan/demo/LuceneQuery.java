package org.infinispan.demo;

import org.apache.lucene.search.Query;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.Index;
import org.infinispan.demo.model.PersonIndexed;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.query.CacheQuery;
import org.infinispan.query.Search;
import org.infinispan.query.SearchManager;

public class LuceneQuery {
    
    public static void main(String[] args) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.indexing().index(Index.ALL).addIndexedEntity(PersonIndexed.class).addProperty("default.directory_provider",
                "ram");
        EmbeddedCacheManager cm = new DefaultCacheManager(cb.build());
        Cache<String, PersonIndexed> cache = cm.getCache();
        cache.put("person1", new PersonIndexed("Will", "Shakespeare"));
        
        SearchManager sm = Search.getSearchManager(cache);
        QueryBuilder qb = sm.buildQueryBuilderForClass(PersonIndexed.class).get();
        Query q = qb.keyword().onField("name").matching("Will").createQuery();
        CacheQuery cq = sm.getQuery(q, PersonIndexed.class);
   
        cq.forEach(person -> System.out.printf("Query result [name, surname]: [%s, %s]", ((PersonIndexed)person).getName(),
                ((PersonIndexed)person).getSurname()));

        cm.stop();
    }

}
