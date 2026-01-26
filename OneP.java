import java.util.*;
import java.util.stream.Collectors;

public class OneP {
    static void main() {
        List<Integer> list = Arrays.asList(2, 0, 9, 8, 11, -9, 0, 8, 11);

        System.out.print("Given list: ");
        list.forEach(item -> System.out.print(item + " "));

        System.out.print("\n1. Largest: ");
        list.stream()
                .max(Integer::compareTo)
                .ifPresent(System.out::println);

        System.out.print("\n6. No. of even numbers: ");
        long count = list.stream()
                .filter(n -> n % 2 == 0)
                .count();
        System.out.print(count);

        System.out.print("\n8. Even numbers saving/storing in a variable: ");
        List<Integer> evenNums = list.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
        System.out.print(evenNums);

        System.out.print("\n9. Even numbers in ascending order: ");
        List<Integer> evenNumsAscOrder = list.stream()
                .filter(n -> n % 2 == 0)
                .sorted()
                .collect(Collectors.toList());
        System.out.print(evenNumsAscOrder);

        System.out.print("\n12. Remove duplicates: ");
        List<Integer> uniqueNums = list.stream()
                .distinct()
                .collect(Collectors.toList());
        System.out.print(uniqueNums);

        List<String> words = Arrays.asList("apple", "banana", "kiwi", "strawberry", "mango");

        System.out.print("\n14. Find longest string by length: ");
        Optional<String> longestString = words.stream()
                .max((a, b) -> a.length() - b.length());
        System.out.print(longestString.get());

        System.out.println("\n16. Group Elements by Even and Odd using partitioningBy");
        Map<Boolean, List<Integer>> evenOddGroup = list.stream()
                .collect(Collectors.partitioningBy(n -> n % 2 == 0));
        System.out.println("Evens: " + evenOddGroup.get(true));
        System.out.println("Odds: " + evenOddGroup.get(false));

        System.out.print("\n16. Group Elements by Even and Odd using groupingBy");
        Map<Boolean, List<Integer>> evenOddGroup1 = list.stream()
                .collect(Collectors.groupingBy(n -> n % 2 == 0, Collectors.toList()));
        System.out.println("Evens: " + evenOddGroup1.get(true));
        System.out.println("Odds: " + evenOddGroup1.get(false));

        String input = "hello world";

        System.out.print("20. First Non-Repeating Character in a String: ");
        Optional<String> firstNonRepeatingCharacter = input.chars()
                .mapToObj(c -> (char) c)
                .map(String::valueOf)
                .collect(Collectors.groupingBy(str -> str, LinkedHashMap::new, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(item -> item.getValue() == 1)
                .map(item -> item.getKey())
                .findFirst();
        System.out.print(firstNonRepeatingCharacter.get());

        System.out.println("22. Count frequency of character in a string and also maintain the insertion order");
        LinkedHashMap<String, Long> charFrequency = input.chars()
                .mapToObj(c -> (char) c)
                .map(String::valueOf)
                // .filter(str -> !str.equals(" "))
                .collect(Collectors.groupingBy(str -> str, LinkedHashMap::new, Collectors.counting()));

        charFrequency.forEach((k, v) -> System.out.println("Key: " + k + " | Value: " + v));
        System.out.println();
        LinkedHashMap<String, Long> charFrequencySortedByValuesInDesc =
                charFrequency.entrySet()
                        .stream()
                        .sorted((o1, o2) ->
                                Integer.compare(Math.toIntExact(o2.getValue()), Math.toIntExact(o1.getValue())))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1,
                                LinkedHashMap::new
                        ));
        charFrequencySortedByValuesInDesc.forEach((k, v) -> System.out.println("Key: " + k + " | Value: " + v));

        Optional<Map.Entry<String, Long>> max = charFrequency.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());
//                .max((o1, o2) ->
//                        Integer.compare(Math.toIntExact(o1.getValue()), Math.toIntExact(o2.getValue())));

        System.out.println(max.get());

        System.out.println("23. Given a list of strings, find all the string that contains all unique characters. ");
        List<String> input1 = List.of("abc", "aabb", "defg", "xyzx", "mnop");

        List<String> collect = input1.stream()
                .filter(s -> s.length() == s.chars().distinct().count())
                .collect(Collectors.toList());
//                .collect(Collectors.joining(", "));

        System.out.println(collect);
    }
}
