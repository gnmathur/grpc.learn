package dev.gmathur.sec06;

import dev.gmathur.models.sec06.BalanceCheckRequest;
import dev.gmathur.models.sec06.WithdrawRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerStreamingBlockingClientTest extends AbstractTest {
    private static final Logger logger = LoggerFactory.getLogger(ServerStreamingBlockingClientTest.class);

    @Test
    public void withdrawTest() {
        // main test
        var request = WithdrawRequest.newBuilder()
                .setAmount(40)
                .setAccountNumber(7)
                .build();

        var balanceIterator = blockingStub.withdrawMoney(request);

        int count = 0;
        while (balanceIterator.hasNext()) {
            var balance = balanceIterator.next();
            count++;
        }
        Assertions.assertEquals(4, count);

        // Now, beefing up the test to check the balance
        var balanceCheckRequest = BalanceCheckRequest.newBuilder()
                .setAccountNumber(7)
                .build();
        var balance = blockingStub.getAccountBalance(balanceCheckRequest);
        logger.info("Account balance for account number {} is {}", request.getAccountNumber(), balance.getBalance());
        Assertions.assertEquals(7 * 103 - 40, balance.getBalance());
    }
}
