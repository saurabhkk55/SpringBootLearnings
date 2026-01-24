package StudentStream2;

import java.util.Comparator;

public class StudentRollNumComparator implements Comparator<Student> {
    @Override
    public int compare(Student o1, Student o2) {
        return Integer.valueOf(o1.getRoll_num()).compareTo(Integer.valueOf(o2.getRoll_num()));
    }
}
