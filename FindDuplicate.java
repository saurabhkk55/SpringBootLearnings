import java.util.*;
import java.util.stream.Collectors;

public class FindDuplicate {
    public static void main(String[] args) {
        List<Integer> ls = Arrays.asList(1,3,2,5,8,8,2,2,5,4,7);

        Set<Integer> hs = new HashSet<>();

        Set<Integer> duplicates = ls.stream()
                .filter(x -> !hs.add(x))
                .collect(Collectors.toSet());

        duplicates.stream().forEach(x -> System.out.println(x));
    }
}
