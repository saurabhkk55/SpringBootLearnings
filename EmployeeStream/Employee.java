package EmployeeStream;

import java.util.List;

public class Employee {
    private String name;
    private int age;
    private String location;
    private double salary;
    private List<String> mobileNum;

    public Employee(String name, int age, double salary, String location, List<String> mobileNum) {
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.location = location;
        this.mobileNum = mobileNum;
    }

    public Employee() {

    }


    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getLocation() {
        return location;
    }

    public List<String> getMobileNum() {
        return mobileNum;
    }

    public double getSalary() {
        return salary;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMobileNum(List<String> mobileNum) {
        this.mobileNum = mobileNum;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", location='" + location + '\'' +
                ", mobileNum=" + mobileNum +
                ", salary=" + salary +
                '}';
    }

    public List<Employee> getEmpList() {
        return List.of(
                new Employee("Jeya", 22, 47000, "Chennai", List.of("9748294522", "9748294523")),
                new Employee("Gobinda", 37, 40000, "Kolkata", List.of("9748294524", "9748294525")),
                new Employee("Shubham", 24, 40000, "Pune", List.of("9748294526", "9748294527")),
                new Employee("Saptarshi", 36, 150000, "Kolkata", List.of("9748294555", "9748294529"))
        );
    }
}
