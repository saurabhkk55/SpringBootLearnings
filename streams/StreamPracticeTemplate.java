package streams;

import java.util.Arrays;
import java.util.List;

public class StreamPracticeTemplate {
    static void main() {

        List<Integer> list = Arrays.asList(2, 0, 9, 8, 11, -9, 0, 8, 11);

        System.out.print("Given list: ");
        list.forEach(item -> System.out.print(item + " "));

        System.out.print("\n1. Largest: ");


        System.out.print("3. Smallest: ");


        System.out.print("4. Second largest: ");


        System.out.print("5. Third smallest: ");


        System.out.print("6. Count of even numbers: ");

//        System.out.println(evenNumCount);

        System.out.print("8. Even numbers saving/storing in a variable: ");

//        System.out.println(evenNumList);

        System.out.print("9. Even numbers in ascending order: ");

//        System.out.println(ascSortedEvenNumList);

        System.out.print("10. Even numbers in descending order: ");

//        System.out.println(descSortedEvenNumList);

        List<String> words = Arrays.asList("appleeeeeeeeeeeeeeeee", "banana", "kiwi", "strawberry", "mango");

        System.out.print("14. Find longest string by length: ");


        System.out.print("15. Find longest string lexicographically: ");


        System.out.println("16. Group Elements by Even and Odd");

//        System.out.println("\tEven numbers: " + groupedByEvenOdd.get(true));
//        System.out.println("\tOdd numbers: " + groupedByEvenOdd.get(false));

        System.out.print("17. Concatenate Strings with a Delimiter: ");

//        System.out.println(concatenatedString);

        String input = "hello world";

        System.out.print("18. Count Distinct Characters in a String: ");

//        System.out.println(distinctCharacterCount);

        System.out.print("19. Remove duplicates: ");

//        System.out.println(uniqueString);

        System.out.print("20. Find the First Non-Repeating Character in a String: ");


        List<String> fruits = Arrays.asList("apple", "banana", "apple", "orange", "banana", "apple");

        System.out.print("21. Create a Map of Word Frequencies: ");

//        System.out.println(wordFreq);

        String str1 = "hello how are you doing";

        System.out.print("22. Count frequency of character in a string and also maintain the insertion order: ");

//        System.out.println(characterFreq);

        List<String> input1 = List.of("abc", "aabb", "defg", "xyzx", "mnop");

        System.out.print("23. Given a list of strings, find all the string that contains all unique characters: ");

//        System.out.println(list1);
    }
}
