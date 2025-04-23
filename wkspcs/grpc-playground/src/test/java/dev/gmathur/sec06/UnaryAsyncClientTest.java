package dev.gmathur.sec06;

import dev.gmathur.models.sec06.BalanceCheckRequest;
import dev.gmathur.models.sec06.BalanceCheckResponse;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * Firstly this test needed a latch otherwise it does not even get to execute the async call
 *
 * Secondly, the test needs the try catch because the failed assertion can cause the test to wait indefinitely as
 * the latch is never decremented because of the assert failure exception
 *
 * Thirdly, the test in its final form still does not work, because with the failure, the exception is caught and
 * the test succeeds
 */
public class UnaryAsyncClientTest extends AbstractTest {
    private static final Logger logger = LoggerFactory.getLogger(UnaryAsyncClientTest.class);

    @Test
    public void getBalanceTest() {
        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(7)
                .build();

        var latch = new CountDownLatch(1);
        bankServiceAsyncStub.getAccountBalance(request, new StreamObserver<BalanceCheckResponse>() {
            @Override
            public void onNext(BalanceCheckResponse accountBalance) {
                logger.info("Account balance for account number {} is {}",
                        accountBalance.getAccountNumber(), accountBalance.getBalance());
                try {
                    // Testing again a wrong value to trigger an assertion failure
                    Assertions.assertEquals(request.getAccountNumber() * 104, accountBalance.getBalance());
                } finally {
                    latch.countDown();
                }
            }

            @Override
            public void onError(Throwable throwable) { }

            @Override
            public void onCompleted() { } });

        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.error("Error occurred while waiting for response", e);
        }
    }

}
