package dev.gmathur.sec06;

import dev.gmathur.common.AbstractChannelTest;
import dev.gmathur.common.GrpcServer;
import dev.gmathur.models.sec06.BankServiceGrpc;
import dev.gmathur.models.sec06.BankServiceGrpc.BankServiceBlockingStub;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractTest extends AbstractChannelTest {
    // Create the gRPC server for the test; This is a singleton instance and will be shared across all tests
    private final GrpcServer grpcServer = GrpcServer.create(new BankService());
    // Create the blocking stub for the gRPC clients; New stubs are created for each test
    protected BankServiceBlockingStub blockingStub;
    // Create the async stub for the gRPC clients; New stubs are created for each test
    protected BankServiceGrpc.BankServiceStub asyncStub;

    @BeforeAll
    public void setup() {
        grpcServer.start();
        blockingStub = BankServiceGrpc.newBlockingStub(channel);
        asyncStub = BankServiceGrpc.newStub(channel);
    }

    @AfterAll
    public void teardown() {
        this.grpcServer.stop();
    }

}
