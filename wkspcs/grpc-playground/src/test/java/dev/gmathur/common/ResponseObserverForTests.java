package dev.gmathur.common;


import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ResponseObserverForTests<T> implements StreamObserver<T> {
    private static final Logger logger = LoggerFactory.getLogger(ResponseObserverForTests.class);

    // Not entirely sure if we need to make the response list synchronized but just in case we do, we will wrap
    // it in a synchronized list to make the responseList thread-safe.
    private final List<T> responseList = Collections.synchronizedList(new ArrayList<>());
    private Throwable throwable;
    private CountDownLatch latch;

    private ResponseObserverForTests(int countDown) {
        this.latch = new CountDownLatch(countDown);
    }

    public static <T> ResponseObserverForTests<T> create() {
        return new ResponseObserverForTests<>(1);
    }

    public static <T> ResponseObserverForTests<T> create(int countDown) {
        return new ResponseObserverForTests<>(countDown);
    }

    @Override
    public void onNext(T t) {
        logger.info("onNext: {}", t);
        this.responseList.add(t);
    }

    @Override
    public void onError(Throwable throwable) {
        this.throwable = throwable;
        // info just because we are debugging and want to see what's going on
        logger.info("Error occurred while getting account balance", throwable);
        this.latch.countDown();

    }

    @Override
    public void onCompleted() {
        this.latch.countDown();
    }

    public void await() {
        try {
            latch.await();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // getters
    public List<T> getResponseList() {
        return responseList;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
