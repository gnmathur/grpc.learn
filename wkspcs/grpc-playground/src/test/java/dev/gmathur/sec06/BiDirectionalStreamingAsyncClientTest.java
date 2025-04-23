package dev.gmathur.sec06;

import dev.gmathur.common.ResponseObserverForTests;
import dev.gmathur.models.sec06.TransferRequest;
import dev.gmathur.models.sec06.TransferResponse;
import dev.gmathur.models.sec06.TransferStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BiDirectionalStreamingAsyncClientTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(BiDirectionalStreamingAsyncClientTest.class);

    @Test
    public void transferTest() {
        var responseObserver = ResponseObserverForTests.<TransferResponse>create();
        var requestObserver = transferServiceAsyncStub.transferMoney(responseObserver);
        var requests = List.of(
                TransferRequest.newBuilder() .setFromAccountNumber(1) .setToAccountNumber(2) .setAmount(10) .build(),
                TransferRequest.newBuilder() .setFromAccountNumber(2) .setToAccountNumber(3) .setAmount(20) .build(),
                TransferRequest.newBuilder() .setFromAccountNumber(3) .setToAccountNumber(4) .setAmount(30) .build(),
                TransferRequest.newBuilder() .setFromAccountNumber(3) .setToAccountNumber(4) .setAmount(300) .build(),
                TransferRequest.newBuilder() .setFromAccountNumber(1) .setToAccountNumber(2) .setAmount(200) .build()
        );

        requests.forEach(requestObserver::onNext);

        requestObserver.onCompleted();

        responseObserver.await();

        var response = responseObserver.getResponseList();

        var throwable = responseObserver.getThrowable();

        log.info(response.toString());
        // Check the response
        Assertions.assertEquals(5, response.size());
        Assertions.assertNull(throwable);

        validate(response.get(0), 1, 2, 1 * 103 - 10, 2 * 103 + 10, TransferStatus.SUCCESS);
        validate(response.get(1), 2, 3, 2 * 103 + 10 - 20, 3 * 103 + 20, TransferStatus.SUCCESS);
        validate(response.get(2), 3, 4, 3 * 103 + 20 - 30, 4 * 103 + 30, TransferStatus.SUCCESS);
        ///  etc...
    }

    private void validate(TransferResponse response, int fromAccountNumber, int toAccountNumber, int fromBalance, int toBalance, TransferStatus status) {
        Assertions.assertEquals(fromAccountNumber, response.getFromAccountNumber());
        Assertions.assertEquals(toAccountNumber, response.getToAccountNumber());
        Assertions.assertEquals(fromBalance, response.getFromBalance());
        Assertions.assertEquals(toBalance, response.getToBalance());
        Assertions.assertEquals(status, response.getStatus());
    }
}
