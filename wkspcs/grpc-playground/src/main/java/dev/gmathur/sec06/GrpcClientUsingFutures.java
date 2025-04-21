package dev.gmathur.sec06;

import com.google.common.util.concurrent.ListenableFuture;
import dev.gmathur.models.sec06.BalanceCheckRequest;
import dev.gmathur.models.sec06.BalanceCheckResponse;
import dev.gmathur.models.sec06.BankServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class GrpcClientUsingFutures {
    private static final Logger logger = LoggerFactory.getLogger(GrpcClientUsingFutures.class);

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 9991)
                .usePlaintext()
                .build();

        BankServiceGrpc.BankServiceFutureStub futureStub =
                BankServiceGrpc.newFutureStub(channel);
        // Can specify the executor while creating the stub too
        // BankServiceGrpc.newFutureStub(channel).withExecutor(executor);

        // Direct executor that just runs the task on the calling thread
        Executor directExecutor = new Executor() {
            @Override
            public void execute(Runnable command) {
                command.run();
            }
        };
        // or
        // Executor directExecutor = Runnable::run;

        ExecutorService fixedThreadPoolExecutor = Executors.newFixedThreadPool(10);
        // Collect all the futures
        // and wait for them to complete
        List<Future<BalanceCheckResponse>> futures = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            final BalanceCheckRequest request = BalanceCheckRequest.newBuilder()
                    .setAccountNumber(i)
                    .build();

            ListenableFuture<BalanceCheckResponse> future = futureStub.getAccountBalance(request);
            futures.add(future);

            future.addListener(new Runnable() {
                @Override
                public void run() {
                    try {
                        BalanceCheckResponse bal = future.get();
                        logger.info(
                                "Account #{} → balance = {}",
                                bal.getAccountNumber(),
                                bal.getBalance()
                        );
                    } catch (Exception e) {
                        logger.error("Error fetching balance for request {}", request.getAccountNumber(), e);
                    }
                }
            }, fixedThreadPoolExecutor);
        }

        // Wait for all futures to complete
        for (Future<BalanceCheckResponse> future : futures) {
            try {
                future.get(); // we don't care about the result here
            } catch (InterruptedException e) {
                logger.error("Error waiting for future to complete", e);
            } catch (ExecutionException e) {
                logger.error("Error executing future", e);
            }
        }

        // orderly shutdown
        channel.shutdown();

        try {
            if (!channel.awaitTermination(5, TimeUnit.SECONDS)) {
                logger.warn("Channel did not terminate in time; forcing shutdown now.");
                channel.shutdownNow();
            }
        } catch (InterruptedException e) {
            logger.error("Interrupted while waiting for channel shutdown", e);
            channel.shutdownNow();
        }

        fixedThreadPoolExecutor.shutdown();
    }

}
