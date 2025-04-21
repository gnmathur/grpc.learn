package dev.gmathur.sec03;

import dev.gmathur.models.sec03.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class Lec06Map {
    private static final Logger LOG = LoggerFactory.getLogger(Lec06Map.class);

    public static void main(String[] args) {
        var car1 = Car.newBuilder() .setMake("Toyota") .setModel("Camry") .setYear(2021) .build();
        var car2 = Car.newBuilder() .setMake("Honda") .setModel("Accord") .setYear(2020).setBodyType(BodyType.HATCHBACK).build();
        var car3 = Car.newBuilder() .setMake("Ford") .setModel("Fusion") .setYear(2019).setBodyType(BodyType.COUPE).build();
        var car4 = Car.newBuilder() .setMake("Chevrolet") .setModel("Malibu") .setYear(2018) .build();
        var car5 = car1.toBuilder().setModel("Corolla").setYear(2020).build();

        var dealer = Dealer.newBuilder()
                .putInventory("Toyota", car1)
                .putInventory("Honda", car2)
                .putInventory("Ford", car3)
                .putInventory("Chevrolet", car4)
                .build();

        LOG.info("Dealer: {}", dealer);

        LOG.info("Dealer: {}", dealer.getInventoryMap());
        LOG.info("Dealer: {}", dealer.getInventoryCount());
        LOG.info("Dealer: {}", dealer.getInventoryOrThrow("Toyota"));
    }
}
