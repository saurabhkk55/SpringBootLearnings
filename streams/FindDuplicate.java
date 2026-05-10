package streams;
import java.util.*;
import java.util.stream.Collectors;

public class FindDuplicate {
    public static void main(String[] args) {
        List<Integer> ls = Arrays.asList(1,3,2,5,8,8,2,2,5,4,7);

        Set<Integer> hs = new HashSet<>();

        Set<Integer> duplicates = ls.stream()
                .filter(n -> !hs.add(n))
                .collect(Collectors.toSet());
        System.out.println(duplicates); // [8, 2, 5]

        List<Object> objectList = Arrays.asList(1, -9, null, "", " ", "     ");

        List<Object> result = objectList.stream()
                .filter(Objects::nonNull) // remove null
                .filter(e -> !(e instanceof String) || !((String) e).isBlank())
                .collect(Collectors.toList());

        System.out.println(result);

        List<Object> emails = Arrays.asList(88, "yoyo", "saurabh@gmail.com", "joy.com", "Roy12@yahoo.com", "", " ", "   ", null);
        System.out.print("26. Valid emails: ");

        List<String> result2 = emails.stream()
                .filter(Objects::nonNull)
                .filter(obj -> (obj instanceof String) && !((String) obj).isBlank())
                .map(String::valueOf)
                .filter(str -> str.contains("@") && str.contains(".com") && (str.contains("gmail") || str.contains("yahoo")))
                .toList();
        System.out.println(result2);
    }
}
