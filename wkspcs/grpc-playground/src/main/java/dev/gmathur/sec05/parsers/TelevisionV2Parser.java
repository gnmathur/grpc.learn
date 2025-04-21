package dev.gmathur.sec05.parsers;

import dev.gmathur.models.sec05.v2.Television;

public class TelevisionV2Parser {
    public static Television parse(byte[] data) {
        // Parse the byte array into a Television object
        try {
            var parsedTelevision = Television.parseFrom(data);

            System.out.println("Brand: " + parsedTelevision.getBrand());
            System.out.println("Model: " + parsedTelevision.getModel());
            System.out.println("Screen Size: " + parsedTelevision.getScreenSize());
            System.out.println("Smart: " + parsedTelevision.getSmart());
            System.out.println("Supported Standards: " + parsedTelevision.getSupportedStandardsList());
            System.out.println("Year of Manufacture: " + parsedTelevision.getYearOfManufacture());

            return parsedTelevision;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Television data", e);
        }
    }
}
