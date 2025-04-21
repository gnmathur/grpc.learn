package dev.gmathur.sec06;

import dev.gmathur.models.sec06.BalanceCheckRequest;
import dev.gmathur.models.sec06.BalanceCheckResponse;
import dev.gmathur.models.sec06.BankServiceGrpc;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class GrpcClientUsingAsync {
    static class BalanceCheckResponseObserver implements StreamObserver<BalanceCheckResponse> {
        private final int requestId;

        public BalanceCheckResponseObserver(int requestId) {
            this.requestId = requestId;
        }
        @Override
        public void onNext(BalanceCheckResponse accountBalance) {
            logger.info("Account balance for account number {} is {}", accountBalance.getAccountNumber(), accountBalance.getBalance());
        }

        @Override
        public void onError(Throwable throwable) {
            logger.error("Error occurred while getting account balance", throwable);
        }

        @Override
        public void onCompleted() {
            logger.info("Completed getting account balance for request {}", requestId);
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(GrpcClientUsingAsync.class);

    public static void main(String[] args) {
        var channel = ManagedChannelBuilder.forAddress("localhost", 9991)
                .usePlaintext()
                .build();

        // Create a stub for the service. You can create multiple stubs for the same service. They are thread-safe.
        var stub = BankServiceGrpc.newStub(channel);

        // try 10 random account numbers
        for (int i = 1; i <= 10; i++) {
            var request = BalanceCheckRequest
                    .newBuilder()
                    .setAccountNumber(i)
                    .build();

            // Since this is a blocking stub, we can call the method directly and get the response. We don't need to
            // use a StreamObserver to get the response.
            stub.getAccountBalance(request, new BalanceCheckResponseObserver(i));
        }

        // wait for a while to get the response
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            logger.error("Error occurred while waiting for response", e);
        } finally {
            channel.shutdown();
        }
    }
}
