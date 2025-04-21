package dev.gmathur.sec01;

import dev.gmathur.models.sec01.PersonOuterClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleProtoDemo {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleProtoDemo.class);

    public static void main(String[] args) {
        // Create a Person object
        PersonOuterClass.Person person = PersonOuterClass.Person.newBuilder().setName("John").setAge(20).build();

        // Print the Person object
        LOG.info("Person: {}", person);
    }
}
