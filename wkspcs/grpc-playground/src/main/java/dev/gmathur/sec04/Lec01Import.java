package dev.gmathur.sec04;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import dev.gmathur.models.common.Address;
import dev.gmathur.models.common.BodyType;
import dev.gmathur.models.common.Car;
import dev.gmathur.models.sec04.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

// Demonstrating how the package names in proto are used. Person proto imports Address and Car. This is where
// proto package directive comes into play. The package name in the proto file is used to refer to other proto
// files. In this case, the Person proto file imports Address and Car proto files, which are in the common package.
public class Lec01Import {
    // logger
    private static final Logger LOG = LoggerFactory.getLogger(Lec01Import.class);

    public static void main(String[] args) throws InvalidProtocolBufferException {
        var address = Address.newBuilder().setCity("Hyderabad").build();
        var carHonda = Car.newBuilder().setBodyType(BodyType.COUPE).setMake("Honda").setModel("Civic").build();
        var carToyota = Car.newBuilder().setBodyType(BodyType.HATCHBACK).setMake("Toyota").setModel("Fortuner").build();

        var person = Person.newBuilder()
                .setFirstName("John")
                .setLastName("Doe")
                .setAge(20)
                .setAddress(address)
                .addAllCar(List.of(carHonda, carToyota))
                .build();

        LOG.info("Person: {}", person);

        LOG.info("JSON Person: {}", JsonFormat.printer()
                .preservingProtoFieldNames()
                        .omittingInsignificantWhitespace()
                .print(person));
    }
}
