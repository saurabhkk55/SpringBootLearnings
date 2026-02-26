package StudentStream;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {

        List<Student> studentList = Student.getStudentList();

        // Method 1: Group Students by the same marks
        Map<Double, List<Integer>> groupStudentsByMarks = studentList.stream()
                .collect(Collectors.groupingBy(Student::getMarks, Collectors.collectingAndThen(
                        Collectors.toList(),
                        studentList1 -> studentList1.stream().map(Student::getId).toList()
                )));
        System.out.println(groupStudentsByMarks); // {75.0=[4], 85.0=[1, 3], 90.0=[2, 5]}

        // Method 2: Group Students by the same marks
        Map<Double, List<Integer>> groupStudentsByMarks1 = studentList.stream()
                .collect(Collectors.groupingBy(Student::getMarks, Collectors.mapping(Student::getId, Collectors.toList())));
        System.out.println(groupStudentsByMarks1); // {75.0=[4], 85.0=[1, 3], 90.0=[2, 5]}

        // Group Students by the same marks but group size should be more than 1
        Map<Double, List<Integer>> onlyMultiStudentsWithTheSameMarks = groupStudentsByMarks.entrySet().stream()
                .filter(item -> item.getValue().size() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println(onlyMultiStudentsWithTheSameMarks); // {85.0=[1, 3], 90.0=[2, 5]}

        // Ids of all students
        List<Integer> allStudentsId = groupStudentsByMarks.entrySet().stream()
                .map(Map.Entry::getValue)
                .flatMap(List::stream)
                .toList();
        System.out.println(allStudentsId); // [4, 1, 3, 2, 5]

        // Nicknames of all students
        List<String> allStudentsNickNames = studentList.stream()
                .map(Student::getNickNames)
                .flatMap(List::stream)
                .toList();
        System.out.println(allStudentsNickNames); // [pinky, tara, kiara, alia, disha, scarlett, Kate, Kristy, Kareena, Katrina, Jacquline]
    }
}
