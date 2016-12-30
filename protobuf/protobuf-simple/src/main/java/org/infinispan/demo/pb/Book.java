package org.infinispan.demo.pb;

public class Book {

    private String title;
    private String description;
    private int publicationYear;

    public Book(String title, String description, int publicationYear) {
        this.title = title;
        this.description = description;
        this.publicationYear = publicationYear;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

}
