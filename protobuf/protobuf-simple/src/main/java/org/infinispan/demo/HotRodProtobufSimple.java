package org.infinispan.demo;

import java.io.IOException;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.impl.ConfigurationProperties;
import org.infinispan.client.hotrod.marshall.ProtoStreamMarshaller;
import org.infinispan.demo.pb.Book;
import org.infinispan.protostream.FileDescriptorSource;
import org.infinispan.protostream.SerializationContext;

public class HotRodProtobufSimple {

    public static final String ISPN_IP = "127.0.0.1";

    public static void main(String[] args) throws IOException {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.addServer().host(ISPN_IP).port(ConfigurationProperties.DEFAULT_HOTROD_PORT);
        builder.marshaller(new ProtoStreamMarshaller());
        RemoteCacheManager cacheManager = new RemoteCacheManager(builder.build());

        SerializationContext serCtx = ProtoStreamMarshaller.getSerializationContext(cacheManager);
        FileDescriptorSource fds = new FileDescriptorSource();
        fds.addProtoFiles("book.pb");
        serCtx.registerProtoFiles(fds);
        serCtx.registerMarshaller(new BookMarshaller());

        String cacheName = args.length > 0 ? args[0] : "";
        RemoteCache<String, Book> cache = cacheManager.getCache(cacheName);

        Book book = new Book("title", "description", 2016);
        cache.put("book1", book);

        Book book1 = cache.get("book1");
        System.out.printf("Book1 title is %s\n", book1.getTitle());

        cacheManager.stop();
    }

}
