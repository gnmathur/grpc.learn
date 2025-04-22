package dev.gmathur.sec06;

import com.google.common.util.concurrent.Uninterruptibles;
import com.google.protobuf.Empty;
import dev.gmathur.models.sec06.*;
import dev.gmathur.models.sec06.BankServiceGrpc.BankServiceImplBase;
import dev.gmathur.sec06.repository.AccountRepository;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class BankService extends BankServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(BankService.class);

    // Note that this method does not RETURN anything
    @Override
    public void getAccountBalance(BalanceCheckRequest request, StreamObserver<BalanceCheckResponse> responseObserver) {
        var accountNumber = request.getAccountNumber();
        var balance = AccountRepository.getBalance(accountNumber);

        var accountBalance = BalanceCheckResponse.newBuilder()
                .setAccountNumber(accountNumber)
                // set balance to a random value from 1 to 1000000
                .setBalance(balance)
                .build();
        // Send the response back to the client
        responseObserver.onNext(accountBalance);
        // Complete the RPC call
        responseObserver.onCompleted();
    }

    @Override
    public void getAllAccount(Empty request, StreamObserver<AllAccountsResponse> responseObserver) {
        var accounts = AccountRepository.getAllAccounts()
                .entrySet()
                .stream()
                .map(entry -> BalanceCheckResponse.newBuilder()
                        .setAccountNumber(entry.getKey())
                        .setBalance(entry.getValue())
                        .build())
                .toList();

        var response = AllAccountsResponse.newBuilder()
                .addAllAccounts(accounts)
                .build();

        // Send the response back to the client
        responseObserver.onNext(response);
        // Complete the RPC call
        responseObserver.onCompleted();
    }

    @Override
    public void withdrawMoney(WithdrawRequest request, StreamObserver<WithdrawResponse> responseObserver) {
        // We would typically check if the account exists and if the amount is valid etc. but for this example we will
        // just assume that the account exists and the amount is valid. Checks might include check if the amount
        // withdrawn needs to be multiple of $10 etc.

        var accountNumber = request.getAccountNumber();
        var amount = request.getAmount();
        var balance = AccountRepository.getBalance(accountNumber);

        // Check if the account has sufficient balance
        if (balance < amount) {
            responseObserver.onCompleted();
            //? Why not onError
            //? How to signal an error to the client
            return;
        }

        // Withdraw in multiples of 10
        //? How to make these operations atomic?
        for (int i = 0; i < amount/10; i += 1) {
            var response = WithdrawResponse.newBuilder()
                    .setAccountNumber(accountNumber)
                    .setAmount(10)
                    .build();
            responseObserver.onNext(response);
            AccountRepository.withdraw(accountNumber, 10);
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);

            log.info("Withdrawing $10 from account {}", accountNumber);
        }

        // Complete the RPC call
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<DepositRequest> depositMoney(StreamObserver<DepositResponse> responseObserver) {
        return new StreamObserver<DepositRequest>() {
            private int accountNumber;

            @Override
            public void onNext(DepositRequest depositRequest) {
                switch (depositRequest.getReqOptionsCase()) {
                    case ACCOUNT_NUMBER -> this.accountNumber = depositRequest.getAccountNumber();
                    case AMOUNT ->  AccountRepository.deposit(accountNumber, depositRequest.getAmount());
                }
            }

            // Client want to cancel the request, or something went wrong
            // A good place to roll-back the transaction
            @Override
            public void onError(Throwable throwable) {
                log.error("Client cancelled the request or something went wrong", throwable);
                responseObserver.onError(throwable);
            }

            // Can be committed to the database
            @Override
            public void onCompleted() {
                var response = DepositResponse.newBuilder()
                        .setAccountNumber(this.accountNumber)
                        .setBalance(AccountRepository.getBalance(this.accountNumber))
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
    }


}
