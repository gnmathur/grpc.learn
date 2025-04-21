package dev.gmathur.sec04;

import com.google.protobuf.Int32Value;
import com.google.protobuf.Timestamp;
import dev.gmathur.models.sec04.Sample;

import java.time.Instant;

public class Lec02WellKnownTypes {
    public static void main(String[] args) {
        var sample = Sample.newBuilder()
                .setInt32Value(Int32Value.of(123))
                .setTimestamp(Timestamp.newBuilder().setSeconds(Instant.now().getEpochSecond()).build())
                .build();

        System.out.println(sample);
    }


}
