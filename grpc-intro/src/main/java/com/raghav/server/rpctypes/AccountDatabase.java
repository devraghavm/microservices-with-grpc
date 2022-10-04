package com.raghav.server.rpctypes;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AccountDatabase {
    /*
    The is a DB
    1 => 10
    2 => 20
    3 => 30
     */
    private static final Map<Integer, Integer> MAP = IntStream
            .rangeClosed(1, 10)
            .boxed()
            .collect(Collectors.toMap(Function.identity(), v -> 100));

    public int getBalance(int accountId) {
        return MAP.get(accountId);
    }

    public int addBalance(int accountId, int amount) {
        return MAP.computeIfPresent(accountId, (k, v) -> v + amount);
    }

    public int deductBalance(int accountId, int amount) {
        return MAP.computeIfPresent(accountId, (k, v) -> v - amount);
    }

    public void printAccountDetails() {
        System.out.println(MAP);
    }
}
