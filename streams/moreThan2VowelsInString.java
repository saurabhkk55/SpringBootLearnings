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

                    String vowels = "aeiouAEIOU";

                    List<String> vowelsInName = name.chars()
                            .mapToObj(c -> (char) c)
                            .map(String::valueOf)
                            .filter(vowels::contains)
                            .toList();

                    if (vowelsInName.size() > 2) return true;
                    return false;
                })
                .toList();

        System.out.println(validVowels);
    }
}
