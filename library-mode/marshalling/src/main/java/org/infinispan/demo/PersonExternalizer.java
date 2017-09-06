package org.infinispan.demo;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashSet;
import java.util.Set;

import org.infinispan.commons.marshall.AdvancedExternalizer;
import org.infinispan.demo.model.Person;

public class PersonExternalizer implements AdvancedExternalizer<Person> {

    public void writeObject(ObjectOutput output, Person person) throws IOException {
        output.writeObject(person.getName());
        output.writeObject(person.getSurname());
    }

    public Person readObject(ObjectInput input) throws IOException, ClassNotFoundException {
        String name = (String)input.readObject();
        String surname = (String)input.readObject();
        return new Person(name, surname);
    }

    public Set<Class<? extends Person>> getTypeClasses() {
        Set<Class<? extends Person>> classes = new HashSet<>();
        classes.add(Person.class);
        return classes;
    }

    public Integer getId() {
        // see http://infinispan.org/docs/dev/user_guide/user_guide.html#preassigned_externalizer_id_ranges
        return 2201;
    }
    

}
