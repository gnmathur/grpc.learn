package dev.gmathur.sec06;

import com.google.protobuf.Empty;
import dev.gmathur.common.ResponseObserverForTests;
import dev.gmathur.models.sec06.AllAccountsResponse;
import dev.gmathur.models.sec06.BalanceCheckRequest;
import dev.gmathur.models.sec06.BalanceCheckResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a much neater version of the async client test. We have essentially wrapper the StreamObserver
 * and made it return either a list of responses or a throwable. It DOES NOT NEED TO TAKE A TEST ASSERTION
 * DECISION ANYMORE.
 */
public class UnaryAsyncClientRefactoredTest extends AbstractTest {
    private static final Logger logger = LoggerFactory.getLogger(UnaryAsyncClientRefactoredTest.class);

    @Test
    public void getBalanceTest() {
        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(7)
                .build();

        var observer = ResponseObserverForTests.<BalanceCheckResponse>create();
        asyncStub.getAccountBalance(request, observer);
        observer.await();

        Assertions.assertEquals(1, observer.getResponseList().size());
        var accountBalance = observer.getResponseList().getFirst();
        Assertions.assertEquals(request.getAccountNumber() * 103, accountBalance.getBalance());
        Assertions.assertNull(observer.getThrowable());
    }

    @Test
    public void allAccountsTest() {
        var request = Empty.getDefaultInstance();
        var observer = ResponseObserverForTests.<AllAccountsResponse>create();
        asyncStub.getAllAccount(request, observer);
        observer.await();
        // Note that the response is a list of accounts but the response itself is a single object since this is a
        // unary call
        var response = observer.getResponseList();
        Assertions.assertEquals(1, response.size());
        Assertions.assertEquals(16, response.getFirst().getAccountsCount());
        response.getFirst().getAccountsList()
                .forEach(a -> Assertions.assertEquals(a.getAccountNumber() * 103, a.getBalance()));
        Assertions.assertNull(observer.getThrowable());
    }
}
