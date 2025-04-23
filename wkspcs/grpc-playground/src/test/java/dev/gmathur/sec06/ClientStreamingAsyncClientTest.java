package dev.gmathur.sec06;

import dev.gmathur.common.ResponseObserverForTests;
import dev.gmathur.models.sec06.DepositRequest;
import dev.gmathur.models.sec06.DepositResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

public class ClientStreamingAsyncClientTest extends AbstractTest {
    private static final Logger logger = LoggerFactory.getLogger(ClientStreamingAsyncClientTest.class);

    @Test
    public void depositTest() {
        var responseObserver = ResponseObserverForTests.<DepositResponse>create();

        var requestObserver = bankServiceAsyncStub.depositMoney(responseObserver);

        // First request is the account number only
        var request = DepositRequest.newBuilder()
                .setAccountNumber(7)
                .build();
        // Now send 4 more requests to deposit a total of $40
        requestObserver.onNext(request);
        /*
        for (int i = 0; i < 4; i++) {
            request = DepositRequest.newBuilder()
                    .setAmount(10)
                    .build();
            requestObserver.onNext(request);
        }
         */
        // This is a functional way of doing what the above for loop does
        IntStream.rangeClosed(1, 4)
                .mapToObj(i -> DepositRequest.newBuilder().setAmount(10).build())
                .forEach(requestObserver::onNext);

        // Mark the end of the client stream
        requestObserver.onCompleted();

        // At this point the response observer should receive the response
        responseObserver.await();
        var response = responseObserver.getResponseList();
        var throwable = responseObserver.getThrowable();

        logger.info("Deposit response: {}", response);

        // Check the response
        Assertions.assertEquals(1, response.size());
        Assertions.assertEquals( 7 * 103 + 40, response.getFirst().getBalance());
        Assertions.assertNull(throwable);
    }

    @Test
    public void depositCancelledRequestTest() {
        var responseObserver = ResponseObserverForTests.<DepositResponse>create();

        var requestObserver = bankServiceAsyncStub.depositMoney(responseObserver);

        // First request is the account number only
        var request = DepositRequest.newBuilder()
                .setAccountNumber(7)
                .build();
        requestObserver.onNext(request);
        // Send a request to deposit $10
        var depositRequest = DepositRequest.newBuilder() .setAmount(10) .build();
        requestObserver.onNext(depositRequest);
        // Now cancel the request simulating a client initiated cancel or client error condition prompting the client
        // to cancel the request
        requestObserver.onError(new RuntimeException("Client wanted to cancel the request"));

        // At this point the response observer should receive the response
        responseObserver.await();
        var response = responseObserver.getResponseList();
        var throwable = responseObserver.getThrowable();

        // Check the response
        Assertions.assertEquals(0, response.size());
        Assertions.assertNotNull(throwable);
        Assertions.assertEquals("CANCELLED: Cancelled by client with StreamObserver.onError()", throwable.getMessage());
    }
}
