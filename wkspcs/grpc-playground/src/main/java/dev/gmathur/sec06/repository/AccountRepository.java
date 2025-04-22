package dev.gmathur.sec06.repository;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AccountRepository {
    private static final int NUMBER_OF_ACCOUNTS = 16;

    private static final Map<Integer, Integer> db = IntStream.rangeClosed(1, NUMBER_OF_ACCOUNTS)
            .boxed()
            .collect(
                    Collectors.toConcurrentMap(
                            Function.identity(),
                            v -> v * 103
                    )
            );

    public static Integer getBalance(int accountNumber) {
        return db.get(accountNumber);
    }

    public static void withdraw(int accountNumber, int amount) {
        db.computeIfPresent(accountNumber, (k, v) -> v - amount);
    }

    public static void deposit(int accountNumber, int amount) {
        db.computeIfPresent(accountNumber, (k, v) -> v + amount);
    }

    public static Map<Integer, Integer> getAllAccounts() {
        // We don't want to expose the mutable map to the client. So we return an unmodifiable view of the map.
        return Collections.unmodifiableMap(db);
    }

    public static void resetDB() {
        db.clear();
        db.putAll(IntStream.rangeClosed(1, NUMBER_OF_ACCOUNTS)
                .boxed()
                .collect(
                        Collectors.toConcurrentMap(
                                Function.identity(),
                                v -> v * 103
                        )
                ));
    }
}
