package dev.gmathur.sec02;

import dev.gmathur.models.sec02.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtoDemo {
    private static final Logger LOG = LoggerFactory.getLogger(ProtoDemo.class);

    private static Person createPerson() {

        Person person = Person.newBuilder()
            .setName("John")
            .setAge(20)
            .build();

        return person;
    }

    public static void main(String[] args) {
        var person1 = createPerson();
        var person2 = createPerson();
        LOG.info("person1: {}", person1);
        LOG.info("person2: {}", person2);


        // Shows that the equals method is overridden in the generated code for Person.
        LOG.info("equals {}", person1.equals(person2));
        LOG.info("== {}", person1 == person2);

        // mutable?
        // no setters, so no mutation

        // create the same person again with some changes
        var person3 = person1.toBuilder().setAge(40).build();
        LOG.info("equals {}", person1.equals(person3)); // should be false
        LOG.info("== {}", person1 == person3); // should be false

        // null?
        // generated source code has null checks for required fields

        // 	var person4 = Person.newBuilder().setName(null).build();
        // LOG.info("person4 {}", person4);
        //
        // causes
        //
        // Exception in thread "main" java.lang.NullPointerException
        //	at dev.gmathur.models.sec02.Person$Builder.setName(Person.java:483)
        //	at dev.gmathur.sec02.ProtoDemo.main(ProtoDemo.java:41)
        //
        // There's a check in the generated code for null values for required fields

        // use clear method to create a new object with default values
        var person5 = person1.toBuilder().clearName().build();
        LOG.info("person5 {}", person5);
    }
}
