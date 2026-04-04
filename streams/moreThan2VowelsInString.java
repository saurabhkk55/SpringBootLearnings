package streams;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class moreThan2VowelsInString {
    public static void main(String[] args) {
        // Find all the Strings that contain more than 2 vowels
        String[] names = {"ritu", "saurabh", "ram", "prerna", "vivek"};

        List<String> validVowels = Arrays.stream(names)
                .filter(name -> {
                    String vowels = "aeiouAEIOU";

                    String[] nameChars = name.split("");
                    long vowelsCounter = Arrays.stream(nameChars)
                            .filter(vowels::contains)
                            .count();
                    if(vowelsCounter > 2) return true;
                    return false;
                })
                .toList();
        System.out.println(validVowels);
    }
}
