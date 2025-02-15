package com.github.budgerigar;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ArrayUtils;

public class Test3 {

    public static String[] array1 = {"a", "b", "c", "d", "e"};
    public static String[] array2 = {"f", "g", "h", "i"};

    public static void test3(String[] args) {
        String[] total = new String[9];
        System.arraycopy(array1, 0, total, 0, array1.length);
        System.arraycopy(array2, 0, total, 5, array2.length);
        System.out.println(Arrays.toString(total));
    }

    public static void test4(String[] args) {
        String str = "a,1,3,b,,100,,0,c,,-99,,d . y 1000";
        List<String> list = str.chars().filter(i -> (char) i != ',')
                .mapToObj(i -> String.valueOf((char) i).trim()).toList();
        System.out.println(String.join(",", list.toArray(new String[0])));
    }

    public static void test5(String[] args) {
        String str = "a,1,3,b,,100,,0,c,,-99,,d . y,1000";

        int sum = Arrays.stream(str.split(",")).mapToInt(s -> {
            try {
                return Integer.parseInt(s);
            } catch (Exception e) {
                return 0;
            }
        }).filter(i -> i > 0).peek(i -> {
            System.out.println(i);
        }).reduce(1, (a, b) -> a * b);

        // long sum = str.chars().filter(i -> Character.isDigit((char) i)).asLongStream().sum();

        // long sum = str.chars().filter(i -> Character.isDigit((char) i))
        // .mapToLong(i -> Long.valueOf(String.valueOf((char) i))).reduce(0L, (a, b) -> a + b);
        System.out.println(sum);
    }

    public static void test6(String[] args) {
        String a = "efkmnblk12haa09112ukl";
        String b = "mnbekfk21laha0ukl9211";
        a = a.chars().mapToObj(i -> String.valueOf((char) i)).sorted()
                .collect(Collectors.joining());
        b = b.chars().mapToObj(i -> String.valueOf((char) i)).sorted()
                .collect(Collectors.joining());
        System.out.println(a.equals(b));
    }

    public static void test7(String[] args) {
        String a = "efk mnb lk12 haa 09112ukl";
        String b = "mbn ekf k21l aha 0ukl9211";
        List<String> original = Arrays.stream(a.split("( )")).map(s -> s.chars()
                .mapToObj(i -> String.valueOf((char) i)).sorted().collect(Collectors.joining()))
                .sorted().toList();
        System.out.println(original);
        List<String> target = Arrays.stream(b.split("( )")).map(s -> s.chars()
                .mapToObj(i -> String.valueOf((char) i)).sorted().collect(Collectors.joining()))
                .sorted().toList();
        System.out.println(target);
        if (original.size() != target.size()) {
            System.out.println("No.");
        }
        if (Arrays.deepEquals(original.toArray(), target.toArray())) {
            System.out.println("Yes.");
        }
    }

    public static void main(String[] args) {
        // test7(args);
        String a = "efk mnb lk12 haa 09112ukl";
        String b = "mbn ekf k21l aha 0ukl9211";
        Map<Character, Long> mapA = a.chars().mapToObj(i -> (char) i)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println(mapA);
        Map<Character, Long> mapB = b.chars().mapToObj(i -> (char) i)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println(mapB);
        if (mapA.size() != mapB.size()) {
            return;
        }
        boolean flag = true;
        for (Map.Entry<Character, Long> en : mapA.entrySet()) {
            Long value = mapB.get(en.getKey());
            if (value == null || !value.equals(en.getValue())) {
                flag = false;
                break;
            }
        }
        if (flag) {
            System.out.println("same");
        }

        // IntStream.range(0, a.length())
        // .filter(i -> map.containsKey(a.charAt(i)) && map.get(a.charAt(i)) == 1)
        // .forEach(i -> {
        // System.out.println(i + "\t" + a.charAt(i));
        // });

    }

    public static void test2(String[] args) {
        // Given a string, ”a,,b,,,c,,,,d “, trim the string to a,b,c,d

        String test = "a,,b,,,c,,,,d ";

        Character[] chs = ArrayUtils.toObject(test.toCharArray());
        Set<Character> set = new LinkedHashSet<>();
        for (Character c : chs) {
            if (c == (char) ',') {
                continue;
            }
            set.add(c);
        }
        String str = set.toString();
        System.out.println(str.substring(1, str.lastIndexOf(",")).replace(" ", ""));

        String str2 = Arrays.stream(chs).filter(c -> c != ',').map(c -> String.valueOf(c))
                .collect(Collectors.joining(","));
        System.out.println(str2);
    }

}
