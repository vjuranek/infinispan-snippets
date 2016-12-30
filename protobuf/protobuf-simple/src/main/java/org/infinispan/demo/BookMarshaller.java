package org.infinispan.demo;

import java.io.IOException;

import org.infinispan.demo.pb.Book;
import org.infinispan.protostream.MessageMarshaller;

public class BookMarshaller implements MessageMarshaller<Book> {

    @Override
    public String getTypeName() {
       return "org.infinispan.demo.pb.Book";
    }

    @Override
    public Class<? extends Book> getJavaClass() {
       return Book.class;
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, Book book) throws IOException {
       writer.writeString("title", book.getTitle());
       writer.writeString("description", book.getDescription());
       writer.writeInt("publicationYear", book.getPublicationYear());
    }

    @Override
    public Book readFrom(ProtoStreamReader reader) throws IOException {
       String title = reader.readString("title");
       String description = reader.readString("description");
       int publicationYear = reader.readInt("publicationYear");
       return new Book(title, description, publicationYear);
    }

}
