package org.infinispan.demo.model;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.infinispan.commons.marshall.Externalizer;
import org.infinispan.commons.marshall.SerializeWith;

@SerializeWith(PersonWithExternalizer.PersonExternalizer.class)
public class PersonWithExternalizer extends Person {

    public PersonWithExternalizer(String name, String surname) {
        super(name, surname);
    }

    public static class PersonExternalizer implements Externalizer<PersonWithExternalizer> {
        
        public void writeObject(ObjectOutput output, PersonWithExternalizer person) throws IOException {
            output.writeObject(person.getName());
            output.writeObject(person.getSurname());
        }

        public PersonWithExternalizer readObject(ObjectInput input) throws IOException, ClassNotFoundException {
            return new PersonWithExternalizer((String) input.readObject(), (String) input.readObject());
        }
    }

}
