package StudentStream;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
  public static void main(String[] args) {
    Map<Double, List<Integer>> collect = Student.getStudentList().stream()
            .collect(Collectors.groupingBy(s -> s.getMarks(), Collectors.mapping(s -> s.getId(), Collectors.toList())));

    System.out.println(collect);

    Map<Double, List<Integer>> collect1 = collect.entrySet().stream()
            .filter(s -> s.getValue().size() > 1)
            .collect(Collectors.toMap(s -> s.getKey(), s -> s.getValue()));

    System.out.println(collect1);

    List<Integer> collect2 = collect.values().stream()
            // .flatMap(List::stream)
            .flatMap(listOfIntegers -> listOfIntegers.stream())
            .collect(Collectors.toList());

    System.out.println(collect2);

    // Generate a list of nicknames' length for each of the students
    Stream<List<String>> stream_of_list_of_nickNames = Student.getStudentList().stream()
                                                            .map(s -> s.getNickNames());
    Stream<Integer> stream_of_nickName_length = stream_of_list_of_nickNames.flatMap(listOfNickNames -> {
      Stream<Integer> integerStream = listOfNickNames.stream().map(nickName -> nickName.length());
      return integerStream;
    });
    List<Integer> collect3 = stream_of_nickName_length.collect(Collectors.toList());
    System.out.println(collect3);
  }
}
