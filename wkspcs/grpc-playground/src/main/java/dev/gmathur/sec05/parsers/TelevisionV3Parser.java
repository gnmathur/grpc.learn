package dev.gmathur.sec05.parsers;


import dev.gmathur.models.sec05.v3.Television;

public class TelevisionV3Parser {
    public static Television parse(byte[] data) {
        // Parse the byte array into a Television object
        try {
            var parsedTelevision = Television.parseFrom(data);

            System.out.println("Brand: " + parsedTelevision.getBrand());
            System.out.println("Supported Standards: " + parsedTelevision.getSupportedStandardsList());
            System.out.println("Year of Manufacture: " + parsedTelevision.getYearOfManufacture());
            System.out.println("Unknown fields: " + parsedTelevision.getUnknownFields());

            return parsedTelevision;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Television data", e);
        }
    }
}
