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

        System.out.print("26. Valid emails: ");
        List<Object> emails = Arrays.asList(88, "yoyo", "saurabh@gmail.com", "joy.com", "Roy12@yahoo.com", "", " ", "   ", null);

        List<String> result2 = emails.stream()
                .filter(Objects::nonNull)
                .filter(obj -> (obj instanceof String) && !((String) obj).isBlank())
                .map(String::valueOf)
                .filter(str -> str.contains("@") && str.contains(".com") && (str.contains("gmail") || str.contains("yahoo")))
                .toList();
        System.out.println(result2);

        System.out.print("27. Count frequency & sort by frequency of character in a string and also maintain the insertion order: ");
        // Sort the map based on frequency (value) using sorted() and then collect into a LinkedHashMap to maintain insertion order.
        String str1 = "hello how are you doing";
        LinkedHashMap<String, Long> sortedMap = str1.chars()
                .mapToObj(c -> (char) c)
                .map(String::valueOf)
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                .entrySet().stream()
//                .sorted(Map.Entry.comparingByValue())   // ascending order
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()) // descending order
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new
                ));

        System.out.println(sortedMap);
    }
}
