import java.util.*;
import java.util.stream.Collectors;

public class PS1 {
    static void main() {
        List<String> input = List.of("abc", "aabb", "defg", "xyzx", "mnop");

        List<String> list = input.stream()
                .filter(str -> str.chars().distinct().count() == str.length())
                .toList();
        System.out.println(list);

        String str1 = "hello how are you doing";
        System.out.println("22. Count frequency of character in a string and also maintain the insertion order");
        LinkedHashMap<Character, Long> charFreq = str1.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(c -> c, LinkedHashMap::new, Collectors.counting()));

        charFreq.forEach((k, v) -> System.out.println(k + " : " + v));

        List<String> fruits = Arrays.asList("apple", "banana", "apple", "orange", "banana", "apple");

        System.out.println("21. Create a Map of Word Frequencies");
        Map<String, Long> collect = fruits.stream()
                .collect(Collectors.groupingBy(fruit -> fruit, Collectors.counting()));

        collect .forEach((k, v) -> System.out.println(k + " : " + v));
    }
}
