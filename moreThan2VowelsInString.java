import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class moreThan2VowelsInString {
    public static void main(String[] args) {
        // Find all the Strings that contain more than 2 vowels
        String[] names = {"ritu", "saurabh", "ram", "prerna", "vivek"};

        List<String> validVowels = Arrays.stream(names)
                .filter(str -> {
                            List<String> vowels = str
                                    .chars()
                                    .mapToObj(c -> (char) c)
                                    .map(String::valueOf)
                                    .filter(s -> s.equals("a") || s.equals("e") || s.equals("i") || s.equals("o") || s.equals("u") || s.equals("A") || s.equals("E") || s.equals("I") || s.equals("O") || s.equals("U"))
                                    .toList();

                            return vowels.size() > 2;
                        }
                )
                .toList();

        validVowels.forEach(System.out::println);
    }
}
