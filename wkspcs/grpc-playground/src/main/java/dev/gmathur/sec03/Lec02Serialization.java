package dev.gmathur.sec03;

import dev.gmathur.models.sec03.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Lec02Serialization {
    private static final Logger LOG = LoggerFactory.getLogger(Lec02Serialization.class);
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
        LOG.info("Person: {}", person.toString());

        try {
            serialize(person);
            Person deserializedPerson = deserialize();

            // check equals
            LOG.info("equals: {}", person.equals(deserializedPerson)); // should be true

            LOG.info("Deserialized Person: {}", deserializedPerson);

            // get size of serialized person
            LOG.info("Serialized size: {}", person.getSerializedSize());
            // should be same as below
            LOG.info("compared to person bytes: {} bytes", person.toByteArray().length);
        } catch (IOException e) {
            LOG.error("Error serializing/deserializing person", e);
        }

    }

    private static void serialize(Person person) throws IOException {
        try (var stream = Files.newOutputStream(PATH)) {
            person.writeTo(stream);
        }
    }

    private static Person deserialize() throws IOException {
        try (var stream = Files.newInputStream(PATH)) {
            return Person.parseFrom(stream);
        }
    }
}
