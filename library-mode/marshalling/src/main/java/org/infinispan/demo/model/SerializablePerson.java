package org.infinispan.demo.model;

import java.io.Serializable;

public class SerializablePerson implements Serializable {
    
    private static final long serialVersionUID = -4598819620147061207L;
    
    String name;
    String surname;
    
    public SerializablePerson(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    @Override
    public String toString() {
        return String.format("{name: '%s', surename: '%s'}", getName(), getSurname());
    }

    
}
