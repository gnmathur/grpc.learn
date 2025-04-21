package dev.gmathur.sec05;

import dev.gmathur.models.sec05.v1.Television;
import dev.gmathur.models.sec05.v2.SupportedStandards;
import dev.gmathur.sec05.parsers.TelevisionV1Parser;

import java.util.List;

// A V1 client (using a V1 parser) can parse both V1 and V2 Television objects.
public class TelevisionV1Client {
    private static dev.gmathur.models.sec05.v2.Television buildV2Television() {
        return dev.gmathur.models.sec05.v2.Television.newBuilder()
                .setBrand("Samsung")
                .setModel("QLED")
                .setScreenSize(55)
                .setSmart(true)
                // new v2 fields
                .addAllSupportedStandards(List.of(SupportedStandards.HDMI, SupportedStandards.BLUETOOTH))
                .setYearOfManufacture(2020)
                .build();
    }

    private static Television buildV1Television() {
        return Television.newBuilder()
                .setBrand("Samsung")
                .setModel("QLED")
                .setScreenSize(55)
                .setSmart(false)
                .build();
    }

    public static void testV1TelevisionFromV1Message() {
        Television television = buildV1Television();

        // Serialize the Television object to a byte array
        byte[] data = television.toByteArray();

        // Deserialize the byte array back to a Television object
        Television parsedTelevision = TelevisionV1Parser.parse(data);
    }

    public static void testV2TelevisionFromV1Message() {
        // Now try to parse the V2 Television object with the V1 parser. This is to demonstrate that the V1 parser can
        // handle V2 data.
        dev.gmathur.models.sec05.v2.Television v2Television = buildV2Television();
        // Serialize the V2 Television object to a byte array
        byte[] v2Data = v2Television.toByteArray();
        // Deserialize the byte array to a V1 Television object
        Television parsedV2Television = TelevisionV1Parser.parse(v2Data);
    }

    // A V1 client (using a V1 parser) can parse both V1 and V2 Television objects.
    public static void main(String[] args) {
        testV1TelevisionFromV1Message();
        System.out.println("====================================");
        testV2TelevisionFromV1Message();
    }
}
