package dev.gmathur.sec05;

import dev.gmathur.models.sec05.v2.SupportedStandards;
import dev.gmathur.models.sec05.v2.Television;
import dev.gmathur.sec05.parsers.TelevisionV2Parser;

import java.util.List;


// A V2 client (using a V2 parser) can parse both V1 and V2 Television objects.
public class TelevisionV2Client {
    private static dev.gmathur.models.sec05.v1.Television buildV1Television() {
        return dev.gmathur.models.sec05.v1.Television.newBuilder()
                .setBrand("Samsung")
                .setModel("QLED")
                .setScreenSize(55)
                .setSmart(true)
                .build();
    }

    private static Television buildV2Television() {
        return Television.newBuilder()
                .setBrand("Samsung")
                .setModel("QLED")
                .setScreenSize(55)
                .setSmart(false)
                // new v2 fields
                .addAllSupportedStandards(List.of(SupportedStandards.HDMI, SupportedStandards.BLUETOOTH))
                .setYearOfManufacture(2020)
                .build();
    }

    public static void testV1TelevisionFromV2Message() {
        dev.gmathur.models.sec05.v1.Television television = buildV1Television();

        // Serialize the Television object to a byte array
        byte[] data = television.toByteArray();

        // Deserialize the byte array back to a Television object
        Television parsedTelevision = TelevisionV2Parser.parse(data);
    }

    public static void testV2TelevisionFromV2Message() {
        // Now try to parse the V2 Television object with the V1 parser. This is to demonstrate that the V1 parser can
        // handle V2 data.
        Television v2Television = buildV2Television();
        // Serialize the V2 Television object to a byte array
        byte[] v2Data = v2Television.toByteArray();
        // Deserialize the byte array to a V1 Television object
        Television parsedV2Television = TelevisionV2Parser.parse(v2Data);
    }

    // A V1 client (using a V1 parser) can parse both V1 and V2 Television objects.
    public static void main(String[] args) {
        testV1TelevisionFromV2Message();
        System.out.println("====================================");
        testV2TelevisionFromV2Message();
    }
}
