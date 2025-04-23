package dev.gmathur.common;

import dev.gmathur.sec06.service.BankService;
import dev.gmathur.sec06.service.TransferService;

public class Demo {
    public static void main(String[] args) {
        GrpcServer.create(new BankService(), new TransferService())
                .start()
                .await();
    }
}
