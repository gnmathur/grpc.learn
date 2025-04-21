package dev.gmathur.sec03;

import dev.gmathur.models.sec03.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Path;

public class Lec01Scalar {
    private static final Logger LOG = LoggerFactory.getLogger(Lec01Scalar.class);
    private static final Path PATH = Path.of("person.out");

    public static void main(String[] args) {
        Person person = Person.newBuilder()
                .setFirstName("John")
                .setLastName("Doe")
                .setAge(20)
                .setHeight(5.9f)
                .setBankAccountNumber(111012324545L)
                .setIsDeveloper(true)
                .setBankBalance(-100)
                .build();

        LOG.info("Person: {}", person);
    }
}
