package streams;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class moreThan2VowelsInString {
    public static void main(String[] args) {
        // Find all the Strings that contain more than 2 vowels
        String[] names = {"ritu", "saurabh", "ram", "prerna", "vivek", "lucifer"};

        List<String> list = Arrays.stream(names)
                .filter(name -> {
                    String vowels = "aeiouAEIOU";

                    long count = name.chars()
                            .mapToObj(ch -> (char) ch)
                            .map(String::valueOf)
                            .filter(str -> vowels.contains(str))
                            .count();

                    return count > 2;
                })
                .toList();

        System.out.println(list);
    }
}
