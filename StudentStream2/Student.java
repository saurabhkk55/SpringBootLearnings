package StudentStream2;

import java.util.Objects;

public class Student implements Comparable<Student>{
    String name;
    int id;
    int age;
    int roll_num;

    public Student(String name, int id, int age, int roll_num) {
        this.name = name;
        this.id = id;
        this.age = age;
        this.roll_num = roll_num;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public int getRoll_num() {
        return roll_num;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setRoll_num(int roll_num) {
        this.roll_num = roll_num;
    }

    @Override
    public int compareTo(Student student) {
        return this.getName().compareTo(student.getName());
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", age=" + age +
                ", roll_num=" + roll_num +
                '}';
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Student student = (Student) o;
//        return id == student.id && age == student.age && roll_num == student.roll_num && Objects.equals(name, student.name);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(name, id, age, roll_num);
//    }
}
