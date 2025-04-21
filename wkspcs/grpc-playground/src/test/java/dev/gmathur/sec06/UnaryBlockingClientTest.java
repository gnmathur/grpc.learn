package dev.gmathur.sec06;

import com.google.protobuf.Empty;
import dev.gmathur.models.sec06.BalanceCheckRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnaryBlockingClientTest extends AbstractTest {
    private static final Logger logger = LoggerFactory.getLogger(UnaryBlockingClientTest.class);

    @Test
    public void getBalanceTest() {
        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(7)
                .build();

        var balance = blockingStub.getAccountBalance(request);
        logger.info("Account balance for account number {} is {}", request.getAccountNumber(), balance.getBalance());

        Assertions.assertEquals(request.getAccountNumber() * 103, balance.getBalance());
    }

    @Test
    public void getAllAccountsTest() {
        // var request = Empty.newBuilder().build();
        // or better still
        var request = Empty.getDefaultInstance();

        var response = blockingStub.getAllAccount(request);
        logger.info("All accounts: {}", response.getAccountsList());

        Assertions.assertEquals(16, response.getAccountsCount());
        response.getAccountsList()
                .forEach(a -> Assertions.assertEquals(a.getAccountNumber() * 103, a.getBalance()));
    }
}
