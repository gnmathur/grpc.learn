package dev.gmathur.sec05;

import dev.gmathur.models.sec05.v3.SupportedStandards;
import dev.gmathur.models.sec05.v3.Television;
import dev.gmathur.sec05.parsers.TelevisionV1Parser;
import dev.gmathur.sec05.parsers.TelevisionV2Parser;
import dev.gmathur.sec05.parsers.TelevisionV3Parser;

import java.util.List;

public class TelevisionV3Client {
    private static Television buildV3Television() {
        return Television.newBuilder()
                .setBrand("Samsung")
                // new v2 fields
                .addAllSupportedStandards(List.of(SupportedStandards.HDMI, SupportedStandards.BLUETOOTH))
                .setYearOfManufacture(2020)
                .build();
    }

    // V3 objects can be parsed by V1, V2, and V3 parsers.
    public static void main(String[] args) {
        Television television = buildV3Television();
        // Serialize the Television object to a byte array
        byte[] data = television.toByteArray();

        TelevisionV1Parser.parse(data); // will show default values for new fields; defaults for removed fields
        System.out.println("====================================");
        TelevisionV2Parser.parse(data); // will show defaults for removed fields
        System.out.println("====================================");
        TelevisionV3Parser.parse(data);
    }
}
