package com.github.budgerigar;

import java.util.Locale;

public class TestMain {

    public static class A {
        protected static String formatId(String id) {
            return id + " 11";
        }
    }


    public static class B extends A {
        public static String formatId(String id) {
            return id + " 22";
        }
    }

    static {
        Locale.setDefault(Locale.US);
    }

    public static void main(String[] args) {
        System.out.println(A.formatId("123"));
        System.out.println(String.format("%h", 1));
        System.out.println(Locale.US.getDisplayCountry());
    }

}
