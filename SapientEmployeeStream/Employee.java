package SapientEmployeeStream;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private Long empId;
    private String name;
    private String department;
    private Long salary;

    public Employee(Long empId, String name, String department, Long salary) {
        this.empId = empId;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    public static List<Employee> getEmployeeList() {
        List<Employee> empList = new ArrayList<>();
        empList
            .addAll
                (
                    List.of
                        (
                            new Employee(101L, "Saurabh", "SE", 40000L),
                            new Employee(101L, "Rahul", "SE", 50000L),
                            new Employee(101L, "Rosy", "HR", 60000L),
                            new Employee(101L, "John", "IT", 70000L),
                            new Employee(101L, "Ben", "IT", 80000L),
                            new Employee(101L, "Rashi", "SE", 60000L),
                            new Employee(101L, "Monty", "IT", 70000L),
                            new Employee(101L, "Ricky", "HR", 40000L),
                            new Employee(101L, "Gian", "SE", 70000L)
                        )
                );
        return empList;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "empId=" + empId +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", salary=" + salary +
                '}';
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }
}
