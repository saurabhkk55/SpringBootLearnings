import java.util.*;
import java.util.stream.Collectors;

public class prac12Dec {
    static void main() {
        List<Integer> list = Arrays.asList(2, 0, 9, 8, 11, -9, 0, 8, 11);

        list.stream()
                .max(Integer::compareTo)
                .ifPresent(System.out::println);

        list.stream()
                .min(Integer::compareTo)
                .ifPresent(System.out::println);

        list.stream()
                .distinct()
                .sorted()
                .skip(1)
                .limit(1)
                .forEach(System.out::println);

        Map<Boolean, List<Integer>> oddEvenGroupingBy = list.stream()
                .collect(Collectors.groupingBy(obj -> obj % 2 == 0));
        System.out.println("Even nums: " + oddEvenGroupingBy.get(true));
        System.out.println("Odd nums: " + oddEvenGroupingBy.get(false));

        List<String> words = Arrays.asList("apple", "banana", "kiwi", "strawberry", "mango");

        words.stream()
                .max(String::compareTo)
                .ifPresent(System.out::println);

        words.stream()
                .max((w1, w2) -> w1.length() - w2.length())
                .ifPresent(System.out::println);

        words.stream()
                .max((w1, w2) -> w2.length() - w1.length())
                .ifPresent(System.out::println);

        words.stream()
                .max((w1, w2) -> w2.length() - w1.length())
                .ifPresent(obj -> System.out.println(obj + " & its length is: " + obj.length()));

        String input = "hello world";

        Map<String, List<String>> collect2 = input.chars()
                .mapToObj(c -> (char) c)
                .map(c -> String.valueOf(c))
                .collect(Collectors.groupingBy(c -> c));

        collect2.forEach((k, v) -> System.out.println(k + " <> " + v));

        Map<String, Long> collect = input.chars()
                .mapToObj(c -> (char) c)
                .map(c -> String.valueOf(c))
                .collect(Collectors.groupingBy(c -> c, LinkedHashMap::new, Collectors.counting()));

        collect.forEach((k, v) -> System.out.println(k + " -> " + v));

        Set<String> hs = new HashSet<>();
        Set<String> collect1 = input.chars()
                .mapToObj(c -> (char) c)
                .map(c -> String.valueOf(c))
                .filter(s -> !hs.add(s))
                .distinct()
                .collect(Collectors.toSet());

        System.out.print("Duplicates: ");
        for (String s : collect1) {
            System.out.print(s + ", ");
        }

        input.chars()
                .mapToObj(c -> (char) c)
                .map(c -> String.valueOf(c))
                .collect(Collectors.groupingBy(c -> c, LinkedHashMap::new, Collectors.counting()))
                .forEach((k, v) -> System.out.println(k + " -> " + v));

        words.stream()
                .min((a, b) -> a.length() - b.length())
                .ifPresent(System.out::println);

        words.stream()
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                .forEach((k, v) -> System.out.println(k + " -> " + v));
    }
}
