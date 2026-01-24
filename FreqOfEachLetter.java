import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class FreqOfEachLetter {
    public static void main(String[] args) {
        String str = "hello how are you doing";

        str
            .chars()
            .mapToObj(c -> (char) c)
            .map(c -> String.valueOf(c))
            .collect(Collectors.groupingBy(c -> c, LinkedHashMap::new,Collectors.counting()))
            .forEach((x,y) -> System.out.println(x + " => " + y));
    }
}
