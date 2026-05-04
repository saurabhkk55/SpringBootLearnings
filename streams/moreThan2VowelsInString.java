package streams;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class moreThan2VowelsInString {
    public static void main(String[] args) {
        // Find all the Strings that contain more than 2 vowels
        String[] names = {"ritu", "saurabh", "ram", "prerna", "vivek", "Saurabh Kumar"};

        List<String> validVowels = Arrays.stream(names)
                .filter(name -> {

                    String[] nameSplit = name.split("");
                    String vowels = "aeiouAEIOU";

                    long vowelsFreq = Arrays.stream(nameSplit)
                            .filter(vowels::contains)
                            .count();

                    return vowelsFreq > 2;
                })
                .toList();

        System.out.println(validVowels);
    }
}
