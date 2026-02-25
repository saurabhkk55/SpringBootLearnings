import java.util.*;
import java.util.stream.Collectors;

public class PS1 {
    static void main() {
        List<Integer> list = Arrays.asList(2, 0, 9, 8, 11, -9, 0, 8, 11);

        System.out.print("Given list: ");
        list.forEach(item -> System.out.print(item + " "));

        System.out.print("\n1. Largest: ");
        list.stream()
                .max(Integer::compareTo)
                .ifPresent(System.out::println);

        System.out.print("3. Smallest: ");
        list.stream()
                .min(Integer::compareTo)
                .ifPresent(System.out::println);

        System.out.print("4. Second largest: ");
        list.stream()
                .distinct()
                .sorted((n1, n2) -> n2 - n1)
                .skip(1)
                .limit(1)
                .forEach(System.out::println);

        System.out.print("5. Third smallest: ");
        list.stream()
                .distinct()
                .sorted()
                .skip(2)
                .limit(1)
                .forEach(System.out::println);

        System.out.print ("6. No. of even numbers: ");
        long evenNumCount = list.stream()
                .filter(n -> n % 2 == 0)
                .count();
        System.out.println(evenNumCount);

        System.out.print("8. Even numbers saving/storing in a variable: ");
        List<Integer> evenNumList = list.stream()
                .filter(n -> n % 2 == 0)
                .toList();
        System.out.println(evenNumList);

        System.out.print("9. Even numbers in ascending order: ");
        List<Integer> ascSortedEvenNumList = list.stream()
                .filter(n -> n % 2 == 0)
                .sorted()
                .toList();
        System.out.println(ascSortedEvenNumList);

        System.out.print("10. Even numbers in descending order: ");
        List<Integer> descSortedEvenNumList = list.stream()
                .filter(n -> n % 2 == 0)
                .sorted((a, b) -> b - a)
                .toList();
        System.out.println(descSortedEvenNumList);

        List<String> words = Arrays.asList("appleeeeeeeeeeeeeeeee", "banana", "kiwi", "strawberry", "mango");

        System.out.print("14. Find longest string by length: ");
        words.stream()
                .max(String::compareTo)
                .ifPresent(System.out::println);

        System.out.print("15. Length of the longest string by length: ");
        words.stream()
                .max((a, b) -> a.length() - b.length())
                .ifPresent(System.out::println);

        System.out.println("16. Group Elements by Even and Odd");
        Map<Boolean, List<Integer>> groupedByEvenOdd = list.stream()
                .collect(Collectors.partitioningBy(n -> n % 2 == 0));
        System.out.println("\tEven numbers: " + groupedByEvenOdd.get(true));
        System.out.println("\tOdd numbers: " + groupedByEvenOdd.get(false));

        System.out.print("17. Concatenate Strings with a Delimiter: ");
        String concatenatedString = words.stream()
                .collect(Collectors.joining(", "));
        System.out.println(concatenatedString);

        String input = "hello world";

        System.out.print("18. Count Distinct Characters in a String: ");
        long distinctCharacterCount = input.chars()
                .distinct()
                .count();
        System.out.println(distinctCharacterCount);

        System.out.print("19. Remove duplicates: ");
        String uniqueString = input.chars()
                .distinct()
                .mapToObj(c -> (char) c)
                .map(String::valueOf)
                .collect(Collectors.joining(""));
        System.out.println(uniqueString);

        System.out.print("20. Find the First Non-Repeating Character in a String: ");
        input.chars()
                .mapToObj(c -> (char) c)
                .map(String::valueOf)
                .collect(Collectors.groupingBy(str -> str, LinkedHashMap::new, Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() == 1)
                .map(Map.Entry::getKey)
                .limit(1)
                .forEach(System.out::println);

        List<String> fruits = Arrays.asList("apple", "banana", "apple", "orange", "banana", "apple");

        System.out.print("21. Create a Map of Word Frequencies: ");
        Map<String, Long> wordFreq = fruits.stream()
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()));
        System.out.println(wordFreq);

        String str1 = "hello how are you doing";

        System.out.print("22. Count frequency of character in a string and also maintain the insertion order: ");
        LinkedHashMap<Character, Long> characterFreq = str1.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(str -> str, LinkedHashMap::new, Collectors.counting()));
        System.out.println(characterFreq);

        List<String> input1 = List.of("abc", "aabb", "defg", "xyzx", "mnop");

        System.out.print("23. Given a list of strings, find all the string that contains all unique characters: ");
        List<String> list1 = input1.stream()
                .filter(str -> str.chars().distinct().count() == str.length())
                .toList();
        System.out.println(list1);

//        List<String> words1 = Arrays.asList("apple", "bat", "cat", "dog", "elephant");
//
//
//        words1.stream()
//                .collect(Collectors.groupingBy(
//                        String::length,
//                        Collectors.collectingAndThen(
//                                Collectors.counting(),   // returns Long
//                                //Long::intValue           // convert to Integer
//                                w -> String.valueOf(w)
//                        )
//                ))
//
//        System.out.println(result);
    }
}
