package dev.gmathur.sec06.service;

import dev.gmathur.models.sec06.TransferRequest;
import dev.gmathur.models.sec06.TransferResponse;
import dev.gmathur.models.sec06.TransferServiceGrpc.TransferServiceImplBase;
import dev.gmathur.models.sec06.TransferStatus;
import dev.gmathur.sec06.repository.AccountRepository;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransferService extends TransferServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(TransferService.class);

    @Override
    public StreamObserver<TransferRequest> transferMoney(StreamObserver<TransferResponse> responseObserver) {
        int MAX_TRANSFER_AMOUNT = 100; // Example limit, adjust as needed

        return new StreamObserver<TransferRequest>() {
            @Override
            public void onNext(TransferRequest transferRequest) {
                // Process the transfer request
                var fromAccount = transferRequest.getFromAccountNumber();
                var toAccount = transferRequest.getToAccountNumber();
                var amount = transferRequest.getAmount();

                var fromBalance = AccountRepository.getBalance(fromAccount);
                var toBalance = AccountRepository.getBalance(toAccount);

                // This clause demonstrates how we can send an "error" code back to the client
                if (fromBalance < amount) {
                    var errorResponse = TransferResponse.newBuilder()
                            .setFromAccountNumber(fromAccount)
                            .setToAccountNumber(toAccount)
                            .setFromBalance(fromBalance)
                            .setToBalance(toBalance)
                            .setStatus(TransferStatus.INSUFFICIENT_FUNDS)
                            .build();
                    responseObserver.onNext(errorResponse);
                    return;
                }

                // This clause demonstrates that not every request needs to be paired with a response
                if (amount > MAX_TRANSFER_AMOUNT) {
                    logger.error("Ignoring request. Transfer amount exceeds maximum allowed amount");
                    return;
                }

                // Here you would typically perform the transfer logic, such as updating account balances
                AccountRepository.deposit(toAccount, amount);
                AccountRepository.withdraw(fromAccount, amount);

                // Create a response
                var response = TransferResponse.newBuilder()
                        .setFromAccountNumber(fromAccount)
                        .setToAccountNumber(toAccount)
                        .setFromBalance(AccountRepository.getBalance(fromAccount))
                        .setToBalance(AccountRepository.getBalance(toAccount))
                        .setStatus(TransferStatus.SUCCESS)
                        .build();

                // Send the response back to the client
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable t) {
                // Handle error
                logger.error("Client wanted to cancel the request", t);
            }

            @Override
            public void onCompleted() {
                // Complete the RPC call
                responseObserver.onCompleted();
            }
        };
    }
}