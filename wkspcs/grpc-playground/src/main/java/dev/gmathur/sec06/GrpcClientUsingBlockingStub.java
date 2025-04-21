package dev.gmathur.sec06;

import dev.gmathur.models.sec06.BalanceCheckRequest;
import dev.gmathur.models.sec06.BankServiceGrpc;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class GrpcClientUsingBlockingStub {
    private static final Logger logger = LoggerFactory.getLogger(GrpcClientUsingBlockingStub.class);

    public static void main(String[] args) {
        var channel = ManagedChannelBuilder.forAddress("localhost", 9991)
                .usePlaintext()
                .build();

        // Create a stub for the service. You can create multiple stubs for the same service. They are thread-safe.
        var stub = BankServiceGrpc.newBlockingStub(channel);
        var rand = new Random();
        // try 10 random account numbers
        for (int i = 1; i <= 10; i++) {
            var accountNumber = rand.nextInt(100) + 1;

            var request = BalanceCheckRequest
                    .newBuilder()
                    .setAccountNumber(accountNumber)
                    .build();

            // Since this is a blocking stub, we can call the method directly and get the response. We don't need to
            // use a StreamObserver to get the response.
            var balance = stub.getAccountBalance(request);

            logger.info("Account balance for account number {} is {}", request.getAccountNumber(), balance.getBalance());
        }
    }
}
