package dev.gmathur.common;

import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

public class GrpcServer {
    private final Server server;
    private static final Logger logger = LoggerFactory.getLogger(GrpcServer.class);

    public GrpcServer(Server server) {
        this.server = server;
    }

    public static GrpcServer create(BindableService... services) {
        // Create a new server instance
        return createServer(9991, services);
    }

    public static GrpcServer createServer(int port, BindableService... services) {
        // Create a new server instance
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(9991);
        Arrays.asList(services).forEach(serverBuilder::addService);
        return new GrpcServer(serverBuilder.build());
    }

    public GrpcServer start() {
        // Get the list of services for logging
        var services = server.getServices()
                .stream()
                .map(ServerServiceDefinition::getServiceDescriptor)
                .map(ServiceDescriptor::getName)
                .toList();
        try {
            server.start();
            logger.info("Server started on port: {}. Services: {}", server.getPort(), services);

            return this;
        } catch (IOException e) {
            logger.error("Failed to start server", e);
            throw new RuntimeException(e);
        }
    }

    public void await() {
        try {
            server.awaitTermination();
        } catch (InterruptedException e) {
            logger.error("Server interrupted", e);
        }
    }

    public void stop() {
        server.shutdownNow();
        logger.info("Server stopped");
        // Anything else???
    }
}
