package dev.gmathur.sec06;

import dev.gmathur.common.ResponseObserverForTests;
import dev.gmathur.models.sec06.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerStreamingAsyncClientTest extends AbstractTest {
    private static final Logger logger = LoggerFactory.getLogger(ServerStreamingAsyncClientTest.class);

    @Test
    public void withdrawTest() {
        int DENOMINATION = 10;
        int NUMBER_OF_BILLS = 4;

        var request = WithdrawRequest.newBuilder()
                .setAmount(NUMBER_OF_BILLS * DENOMINATION)
                .setAccountNumber(7)
                .build();
        var observer = ResponseObserverForTests.<WithdrawResponse>create();
        bankServiceAsyncStub.withdrawMoney(request, observer);

        observer.await();

        var response = observer.getResponseList();
        var throwable = observer.getThrowable();

        Assertions.assertEquals(NUMBER_OF_BILLS, response.size());
        Assertions.assertEquals(DENOMINATION, response.getFirst().getAmount());
        Assertions.assertNull(throwable);
    }
}
