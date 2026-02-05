import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FlatMap {
    static void main() {
        // 1. Flatten a List of Lists
        List<List<Integer>> list = List.of(
                List.of(1, 2),
                List.of(3, 4),
                List.of(5)
        );

        List<Integer> list1 = list.stream()
                .flatMap(tempList -> tempList.stream())
                .toList();

        System.out.println(list1);

        // 2. Count total elements in nested collection
        long count = list.stream()
                .flatMap(List::stream)
                .count();
        System.out.println(count);

        // 2. Find All Unique Characters from Words
        // Approach 1: maintains insertion order
        List<String> words = List.of("java", "stream");
        List<Character> list2 = words.stream()
                .flatMap(str -> str.chars()
                        .mapToObj(c -> (char) c)
                        .distinct()
                )
                .distinct()
                .toList();
        System.out.println(list2);

        // Approach 1: doesn't maintain insertion order
        Set<String> collect = words.stream()
                .flatMap(str -> Arrays.stream(str.split("")))
                .collect(Collectors.toSet());
        System.out.println(collect);
    }
}
