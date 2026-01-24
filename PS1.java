import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PS1 {
    static void main() {
        List<String> input = List.of("abc", "aabb", "defg", "xyzx", "mnop");

        List<String> list1 = input.stream()
                .filter(x -> x.chars().distinct().count() == x.length())
                .collect(Collectors.toList());

        list1.forEach(System.out::println);
    }
}
