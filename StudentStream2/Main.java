package StudentStream2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Student> studentList = new ArrayList<>();

        Student student1 = new Student("Alice", 1, 20, 105);
        Student student2 = new Student("Daisy", 2, 21, 102);
        Student student3 = new Student("Ethan", 3, 22, 104);
        Student student4 = new Student("Bob", 4, 20, 101);
        Student student5 = new Student("Charlie", 5, 23, 102);

        studentList.addAll(List.of(student1, student2, student3, student4, student5));

        System.out.println("\nSorting Students based on their Names");
        Collections.sort(studentList);
        studentList.stream()
                .forEach(x -> System.out.println(x));

        System.out.println("\nSorting Students based on their Ages");
        StudentAgeComparator studentAgeComparator = new StudentAgeComparator();
        Collections.sort(studentList, studentAgeComparator);
        studentList.stream()
                .forEach(x -> System.out.println(x));

        System.out.println("\nSorting Students based on their Roll numbers");
        StudentRollNumComparator studentRollNumComparator = new StudentRollNumComparator();
        Collections.sort(studentList, studentRollNumComparator);
        studentList.stream()
                .forEach(x -> System.out.println(x));
    }
}
