package dev.gmathur.common;

import dev.gmathur.sec06.service.BankService;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServerBasic {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Starting gRPC server...");

        var server = ServerBuilder.forPort(9991)
                .addService(new BankService())
                .build();

        server.start();

        server.awaitTermination();

        System.out.println("gRPC server started on port 6565");
    }
}
