import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class One {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(2, 0, 9, 8, 11, -9, 0, 8, 11);

        System.out.print("Given list: ");
        list.forEach(item -> System.out.print(item + " "));

        // Largest
        System.out.print("\n1. Largest: ");
        list.stream()
                .sorted((a, b) -> b - a)
//                .skip(list.size()-1)
                .limit(1)
                .forEach(n -> System.out.println(n));

        // Largest
        System.out.print("2. Largest: ");
        list.stream()
                .max(Integer::compareTo)
                .ifPresent(n -> System.out.println(n));

        // Smallest
        System.out.print("3. Smallest: ");
        list.stream()
                .min(Integer::compareTo)
                .ifPresent(n -> System.out.println(n));

        // Second largest
        System.out.print("4. Second largest: ");
        list.stream()
                .distinct()
                .sorted((a, b) -> b - a)
                .skip(1)
                .limit(1)
                .forEach(System.out::println);

        // Third smallest
        System.out.print("5. Third smallest: ");
        list.stream()
                .distinct()
                .sorted()
                .skip(2)
                .limit(1)
                .forEach(System.out::println);

        // No. of even numbers
        System.out.println("\n6. No. of even numbers");
        long count = list.stream()
                .filter(n -> n % 2 == 0)
                .count();
        System.out.println(count);

        System.out.println("\n7. Even numbers w/o saving/storing result");
        list.stream()
                .distinct()
                .filter(n -> n % 2 == 0)
                .forEach(n -> System.out.print(n + " "));

        System.out.println("\n8. Even numbers saving/storing in a variable");
        List<Integer> evenList = list.stream()
                .distinct()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());

        evenList.stream()
                .forEach(n -> System.out.print(n + " "));


        System.out.println("\n9. Even numbers in ascending order");
        list.stream()
                .distinct()
                .filter(n -> n % 2 == 0)
                .sorted()
                .forEach(n -> System.out.print(n + " "));

        System.out.println("\n10. Even numbers in descending order");
        list.stream()
                .distinct()
                .filter(n -> n % 2 == 0)
                .sorted((a, b) -> b - a)
                .forEach(n -> System.out.print(n + " "));

        System.out.println("\n12. Remove duplicates");
        list.stream()
                .distinct()
                .forEach(n -> System.out.print(n + " "));

        System.out.println("\n13. Sum of square of odd numbers");
        Integer sum_of_sqr_of_odd_nums = list.stream()
                .filter(n -> n % 2 != 0)
                // .peek(n -> System.out.println("odd no: " + n))
                .map(n -> n * n)
                .reduce(0, Integer::sum);
        System.out.println(sum_of_sqr_of_odd_nums);

        List<String> words = Arrays.asList("apple", "banana", "kiwi", "strawberry", "mango");

        System.out.println("14. Find longest string by length");
        words.stream()
                .max((a, b) -> a.length() - b.length())
                .ifPresent(System.out::println);

        System.out.println("15. Length of the longest string by length");
        words.stream()
                .max((a, b) -> a.length() - b.length())
                .ifPresent(str -> System.out.println(str + " => " + str.length()));

        System.out.println("16. Group Elements by Even and Odd");
        Map<Boolean, List<Integer>> groupedByEvenOdd = list.stream()
                .collect(Collectors.partitioningBy(n -> n % 2 == 0));

        groupedByEvenOdd.forEach((x, y) -> {
            if(x) System.out.println("evens => " + y);
            else System.out.println("odds => " + y);
        });
        // OR (better)
        System.out.println("Even numbers: " + groupedByEvenOdd.get(true));
        System.out.println("Odd numbers: " + groupedByEvenOdd.get(false));

        System.out.println("17. Concatenate Strings with a Delimiter");
        String str = words.stream()
                .collect(Collectors.joining(", "));
        System.out.println(str);

        String input = "hello world";

        System.out.println("18. Count Distinct Characters in a String");
        long count1 = input.chars()
                .distinct()
                .count();
        System.out.println(count1);

        System.out.println("19. Remove duplicates");
        String collect = input.chars()
                .distinct()
                .mapToObj(c -> (char) c)
                .map(c -> String.valueOf(c))
                .collect(Collectors.joining());
        System.out.println(collect);

        System.out.println("20. Find the First Non-Repeating Character in a String");
        input.chars()
                .mapToObj(c -> (char) c)
                .map(String::valueOf)
                .collect(Collectors.groupingBy(c -> c, LinkedHashMap::new, Collectors.counting()))
                .entrySet().stream()
                .filter(x -> x.getValue() == 1)
                .map(x -> x.getKey())
                .limit(1)
                .forEach(x -> System.out.println(x));

        List<String> fruits = Arrays.asList("apple", "banana", "apple", "orange", "banana", "apple");

        System.out.println("21. Create a Map of Word Frequencies");
        fruits.stream()
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()))
                .forEach((x, y) -> {
                    System.out.println(x + " => " + y);
                });

        String str1 = "hello how are you doing";

        System.out.println("22. Count frequency of character in a string and also maintain the insertion order");
        str1.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(c -> c, LinkedHashMap::new, Collectors.counting()))
                .forEach((x, y) -> System.out.println(x + " => " + y));

        System.out.println("23. Given a list of strings, find all the string that contains all unique characters. ");
        List<String> input1 = List.of("abc", "aabb", "defg", "xyzx", "mnop");

        List<String> list1 = input1.stream()
                .filter(x -> x.chars().distinct().count() == x.length())
                .collect(Collectors.toList());

        list1.forEach(System.out::println);
    }
}
