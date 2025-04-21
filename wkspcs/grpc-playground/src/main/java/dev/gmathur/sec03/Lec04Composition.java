package dev.gmathur.sec03;

import dev.gmathur.models.sec03.Address;
import dev.gmathur.models.sec03.School;
import dev.gmathur.models.sec03.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec04Composition {
    private static final Logger LOG = LoggerFactory.getLogger(Lec04Composition.class);

    public static void main(String[] args) {
        var address = Address.newBuilder()
                .setStreet("123 Main St")
                .setCity("Springfield")
                .setState("IL")
                .setZip("62701")
                .build();

        var student = Student.newBuilder()
                .setName("John")
                .setAddress(address)
                .build();

        var school = School.newBuilder()
                .setId(1)
                .setName("Springfield High School")
                .setAddress(address.toBuilder().setStreet("987 Blank St").build())
                // this also works - passing just the builder
                // .setAddress(address.toBuilder().setStreet("987 Blank St"))
                .build();
    }
}
