package com.github.budgerigar;

import java.util.HashMap;
import java.util.Map;

public class Test2 {

    public static void main(String[] args) {
        String a = "efk mnb lk12 haa 09112ukl";
        String b = "mnb ekf k21l aha 0ukl9211";
        System.out.println(compare(a, b));
    }

    private static boolean compare(String a, String b) {
        if (a.length() != b.length()) {
            return false;
        }
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < a.length(); i++) {
            char c = a.charAt(i);
            Integer count = map.get(c);
            if (count == null) {
                count = 1;
            } else {
                count++;
            }
            map.put(c, count);
        }
        for (int i = 0; i < b.length(); i++) {
            char c = b.charAt(i);
            Integer count = map.get(c);
            if (count == null) {
                return false;
            }
            if (count > 0) {
                count--;
                map.put(c, count);
            } else {
                return false;
            }
        }
        return map.values().stream().allMatch(i -> i.intValue() == 0);
    }
}
