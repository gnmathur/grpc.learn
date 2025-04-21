package dev.gmathur.sec03;

import dev.gmathur.models.sec03.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec07DefaultValues {
    private static final Logger LOG = LoggerFactory.getLogger(Lec07DefaultValues.class);

    public static void main(String[] args) {
        var school = School.newBuilder().build();

        LOG.info("{}", school.getId()); // 0 as its default scalar field value
        LOG.info("{}", school.getName());
        LOG.info("{}", school.getAddress()); // No null, but Address.defaultInstance
        LOG.info("{}", school.getAddress().getCity()); // Can get city (default) even though address is not set

        LOG.info("{}", school.getAddress().equals(Address.getDefaultInstance())); // true

        // Check if there's a value - hasXXX method
        if (school.hasAddress()) {
            LOG.info("{}", school.getAddress().getCity());
        } else {
            LOG.info("No address");
        }

        // but not that hasId and hasName methods are not generated for scalar fields

        // collection
        var library = Library.newBuilder().build();
        LOG.info("{}", library.getBooksList()); // empty list

        // map
        var dealer = Dealer.newBuilder().build();
        LOG.info("{}", dealer.getInventoryMap()); // empty map

        // 23:29:36.352 INFO  [           main] d.g.sec03.Lec07DefaultValues   : []
        //23:29:36.357 INFO  [           main] d.g.sec03.Lec07DefaultValues   : {}

        // enum
        var car = Car.newBuilder().build();
        LOG.info("{}", car.getBodyType()); // SEDAN!

        // After adding UNKNOWN to BodyType enum in Car.proto at index 0

        // 23:34:59.954 INFO  [           main] d.g.sec03.Lec07DefaultValues   : UNKNOWN_BODY_TYPE
    }
}
