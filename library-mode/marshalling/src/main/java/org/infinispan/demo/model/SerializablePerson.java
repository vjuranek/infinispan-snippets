package org.infinispan.demo.model;

import java.io.Serializable;

public class SerializablePerson extends Person implements Serializable {
    
    private static final long serialVersionUID = -4598819620147061207L;

    public SerializablePerson(String name, String surname) {
        super(name, surname);
    }

}
