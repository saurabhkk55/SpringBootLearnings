package StudentStream;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private int id;
    private double marks;
    private int age;
    private List<String> nickNames;

    public Student(int id, double marks, int age, List<String> nickNames) {
        this.id = id;
        this.marks = marks;
        this.age = age;
        this.nickNames = nickNames;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static List<Student> getStudentList() {
        List<Student> studentsList = new ArrayList<>();

        studentsList
            .addAll(
                List.of(
                    new Student(1, 85.0, 20, List.of("pinky", "tara")),
                    new Student(2, 90.0, 32, List.of("kiara", "alia", "disha")),
                    new Student(3, 85.0, 16, List.of("scarlett", "Kate")),
                    new Student(4, 75.0, 72, List.of("Kristy")),
                    new Student(5, 90.0, 62, List.of("Kareena", "Katrina", "Jacquline"))
                )
            );

        return studentsList;
    }

    public List<String> getNickNames() {
        return nickNames;
    }

    public void setNickNames(List<String> nickNames) {
        this.nickNames = nickNames;
    }
}
