package streams;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class moreThan2VowelsInString {
    public static void main(String[] args) {
        // Find all the Strings that contain more than 2 vowels
        String[] names = {"ritu", "saurabh", "ram", "prerna", "vivek"};

        // Approach 1
        List<String> validVowels1 = Arrays.stream(names)
                .filter(str -> {
                            long count = Arrays.stream(str.split(""))
                                    .filter(s -> s.equals("a")
                                            || s.equals("e")
                                            || s.equals("i")
                                            || s.equals("o")
                                            || s.equals("u")
                                            || s.equals("A")
                                            || s.equals("E")
                                            || s.equals("I")
                                            || s.equals("O")
                                            || s.equals("U"))
                                    .count();
                            return count > 2;
                        }
                )
                .toList();
        System.out.println(validVowels1);

        // Approach 2
        List<String> validVowels2 = Arrays.stream(names)
                .filter(str -> {
                            List<String> vowels = str
                                    .chars()
                                    .mapToObj(c -> (char) c)
                                    .map(String::valueOf)
                                    .filter(s -> s.equals("a")
                                            || s.equals("e")
                                            || s.equals("i")
                                            || s.equals("o")
                                            || s.equals("u")
                                            || s.equals("A")
                                            || s.equals("E")
                                            || s.equals("I")
                                            || s.equals("O")
                                            || s.equals("U"))
                                    .toList();

                            return vowels.size() > 2;
                        }
                )
                .toList();
        System.out.println(validVowels2);
    }
}
