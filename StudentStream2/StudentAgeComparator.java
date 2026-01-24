package StudentStream2;

import java.util.Comparator;

public class StudentAgeComparator implements Comparator<Student> {
    @Override
    public int compare(Student o1, Student o2) {
        return Integer.valueOf(o1.getAge()).compareTo(Integer.valueOf(o2.getAge()));
    }
}
