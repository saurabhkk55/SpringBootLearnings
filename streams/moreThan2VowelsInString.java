package streams;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class moreThan2VowelsInString {
    public static void main(String[] args) {
        // Find all the Strings that contain more than 2 vowels
        String[] names = {"ritu", "saurabh", "ram", "prerna", "vivek", "lucifer"};

        List<String> validNames = Arrays.stream(names)
                .filter(name -> {

                    String[] split = name.split("");
                    String vowels = "aeiou";

                    long vowelsCount = Arrays.stream(split)
                            .filter(str -> vowels.contains(str.toLowerCase()))
                            .count();

                    return vowelsCount > 2;
                })
                .toList();
        System.out.println(validNames);
    }
}
