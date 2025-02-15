package com.github.budgerigar;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class TestMain2 {

    private static boolean isPrimeNumber(int value) {
        if (value <= 1) {
            return false;
        }
        for (int i = 2; i < Math.sqrt(value); i++) {
            if (value % i == 0) {
                return false;
            }
        }
        return true;
    }

    private static int randomInt() {
        return ThreadLocalRandom.current().nextInt(1, 10000);
    }

    public static void main(String[] args) {
        // int n = 0;
        // for (int i = 1; i < 10000; i++) {
        // if (isPrimeNumber(i)) {
        // System.out.println(i);
        // if (n++ == 100) {
        // break;
        // }
        // } else {
        //
        // }
        // }
        int n = 0;
        int v;
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            v = randomInt();
            if (isPrimeNumber(v)) {
                // System.out.println(v);
                set.add(v);
                if (set.size() == 100) {
                    break;
                }
            }
        }

        System.out.println(set);
    }

}
