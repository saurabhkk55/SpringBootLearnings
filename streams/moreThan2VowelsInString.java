package streams;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class moreThan2VowelsInString {
    public static void main(String[] args) {
        // Find all the Strings that contain more than 2 vowels
        String[] names = {"ritu", "saurabh", "ram", "prerna", "vivek", "Saurabh Kumar"};

        List<String> validVowelList = Arrays.stream(names)
                .filter(name -> {
                    String validVowels = "aeiouAEIOU";
                    String[] nameArr = name.split("");

                    long vowelsCount = Arrays.stream(nameArr)
                            .filter(validVowels::contains)
                            .count();

                    return vowelsCount > 2;
                })
                .toList();

        System.out.println(validVowelList); // [saurabh, Saurabh Kumar]
    }
}
