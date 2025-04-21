package dev.gmathur.sec03;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import dev.gmathur.models.sec03.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.IntStream;

// Compare JSON and ProtoBuf serialization performance
public class Lec03PerformanceTest {
    private static final Logger LOG = LoggerFactory.getLogger(Lec03PerformanceTest.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static void main(String[] args) {
        Person person = Person.newBuilder()
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("john.doe@gmail.com")
                .setAge(20)
                .setHeight(5.9f)
                .setBankAccountNumber(111012324545L)
                .setIsDeveloper(true)
                .setBankBalance(-100)
                .build();

        LOG.info("Person: {}", person);
        LOG.info("Person: {}", person.toString());

        var jsonPerson = new JsonPerson(
                person.getFirstName(),
                person.getLastName(),
                person.getAge(),
                person.getEmail(),
                person.getIsDeveloper(),
                person.getSalary(),
                person.getBankAccountNumber(),
                person.getBankBalance(),
                person.getHeight());


        var iterations = 10;
        // Run tests 10 times and do the average
        long protoTime = (long) IntStream.range(0, iterations)
                .mapToLong(i -> runTest("Proto", () -> proto(person, false)))
                .average()
                .orElseThrow();

        long jsonTime = (long) IntStream.range(0, iterations)
                .mapToLong(i -> runTest("Json", () -> json(jsonPerson, false)))
                .average()
                .orElseThrow();

        LOG.info("Proto average: {} ms", protoTime);
        LOG.info("Json average: {} ms", jsonTime);

        // Sample result
        // 21:24:50.891 INFO  [           main] d.g.sec03.Lec03PerformanceTest : Proto: 124 ms
        //21:24:50.891 INFO  [           main] d.g.sec03.Lec03PerformanceTest : Json: 550 ms

        // Proto is faster than Json!


        // Compare the size of the serialized data
        proto(person, true);
        json(jsonPerson, true);

        // Sample result
        // 21:31:17.948 INFO  [           main] d.g.sec03.Lec03PerformanceTest : Proto bytes: 50
        //21:31:17.948 INFO  [           main] d.g.sec03.Lec03PerformanceTest : Json bytes: 178

        // Proto is more compact than Json!

    }

    // we will serialize and deserialize the person object using both json and proto
    // and compare the performance of both
    private static void proto(Person person, boolean printByte) {
        var bytes = person.toByteArray();
        if (printByte) {
            LOG.info("Proto bytes: {}", bytes.length);
        }
        try {
            Person.parseFrom(bytes);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException("Error parsing person", e);
        }
    }

    // we do the exact same thing in the json method
    private static void json(JsonPerson person, boolean printByte) {
        try {
            var bytes = MAPPER.writeValueAsBytes(person);
            if (printByte) {
                LOG.info("Json bytes: {}", bytes.length);
            }
            MAPPER.readValue(bytes, JsonPerson.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing person", e);
        }
    }

    private static long runTest(String testName, Runnable runnable) {
        long start = System.nanoTime();
        for (int i = 0; i < 5_000_000; i++) {
            runnable.run();
        }
        long end = System.nanoTime();
        LOG.info("{} took: {} ms", testName, (end - start) / 1_000_000);

        return (end - start) / 1_000_000;
    }
}
