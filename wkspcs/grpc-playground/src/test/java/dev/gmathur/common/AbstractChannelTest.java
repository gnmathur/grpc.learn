package dev.gmathur.common;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractChannelTest {
    protected ManagedChannel channel;

    // Want to execute this before all test classes. Tried to setting the lifecycle to PER_CLASS
    @BeforeAll
    public void setupChannel() {
        this.channel = ManagedChannelBuilder.forAddress("localhost", 9991).usePlaintext().build();
    }

    @AfterAll
    public void shutdownChannel() throws InterruptedException {
        if (channel != null) {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
