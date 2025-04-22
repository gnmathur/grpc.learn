package dev.gmathur.sec06;

import dev.gmathur.models.sec06.BankServiceGrpc;
import dev.gmathur.models.sec06.WithdrawRequest;
import dev.gmathur.models.sec06.WithdrawResponse;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrpcClientClientStreamingUsingAsync {
    private static final Logger logger = LoggerFactory.getLogger(GrpcClientClientStreamingUsingAsync.class);

    public static void main(String[] args) {

        var channel = ManagedChannelBuilder.forAddress("localhost", 9991)
                .usePlaintext()
                .build();
        var stub = BankServiceGrpc.newStub(channel);

        var request = WithdrawRequest.newBuilder()
                .setAccountNumber(4)
                .setAmount(100)
                .build();
        stub.withdrawMoney(request, new StreamObserver<WithdrawResponse>() {
            @Override
            public void onNext(WithdrawResponse withdrawResponse) {
                logger.info(withdrawResponse.toString());
            }

            @Override
            public void onError(Throwable throwable) {
                logger.error("Error occurred while withdrawing money", throwable);
            }

            @Override
            public void onCompleted() {
                logger.info("Completed");
            }
        });

        // wait for a while to get the response
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            logger.error("Error occurred while waiting for response", e);
        }
    }
}
