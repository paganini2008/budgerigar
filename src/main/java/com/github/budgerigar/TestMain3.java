package com.github.budgerigar;

import java.util.List;
import java.util.stream.Collectors;

public class TestMain3 {

    public static void main(String[] args) {
        List<Character> list =
                "3312123324101".chars().mapToObj(c -> (char) c).distinct().sorted().toList();
        System.out.println(list);

        String str = list.stream().map(String::valueOf).collect(Collectors.joining("|"));
        System.out.println(str);

        StringBuilder sb = "3312123324101".chars().collect(StringBuilder::new,
                StringBuilder::append, StringBuilder::append);
        System.out.println(sb);


    }

}
