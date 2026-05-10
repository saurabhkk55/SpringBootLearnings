package streams;

import java.util.*;
import java.util.stream.Collectors;

public class Employee {
    int empId;
    String name;
    String dept;
    double salary;

    public Employee(int empId, String name, String dept, double salary) {
        this.empId = empId;
        this.name = name;
        this.dept = dept;
        this.salary = salary;
    }

    public int getEmpId() {
        return empId;
    }

    public String getName() {
        return name;
    }

    public String getDept() {
        return dept;
    }

    public double getSalary() {
        return salary;
    }

    static void main() {
        Employee e1 = new Employee(101, "Joy", "IT", 10);
        Employee e2 = new Employee(102, "John", "IT", 20);
        Employee e3 = new Employee(103, "Jack", "IT", 30);
        Employee e4 = new Employee(104, "Macky", "HR", 5);
        Employee e5 = new Employee(105, "Martin", "HR", 6);
        Employee e6 = new Employee(106, "Moly", "HR", 7);
        Employee e7 = new Employee(107, "Katty", "Dev", 20);
        Employee e8 = new Employee(108, "Katrina", "Dev", 30);
        Employee e9 = new Employee(109, "Kim", "Dev", 40);

        List<Employee> employeeList = List.of(e1, e2, e3, e4, e5, e6, e7, e8, e9);

        // Find employee from each dept with highest salary
        Map<String, String> empWithHighestSalaryFromEachDept = employeeList.stream()
                .collect(Collectors.groupingBy(emp -> emp.dept, Collectors.maxBy((emp1, emp2) -> (int) (emp1.getSalary() - emp2.getSalary()))))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, emp -> emp.getValue().get().getName() + " " + emp.getValue().get().getSalary()));
        System.out.println(empWithHighestSalaryFromEachDept);

        // employee With Second Highest Salary From Each Dept
        Map<String, Optional<String>> empWithSecondHighestSalaryFromEachDept = employeeList.stream()
                .collect(Collectors.groupingBy(emp -> emp.dept, Collectors.collectingAndThen(
                        Collectors.toList(),
                        empList -> empList.stream()
                                .distinct()
                                .sorted((emp1, emp2) -> (int) (emp2.getSalary() - emp1.getSalary()))
                                .skip(1)
                                .map(e -> e.getName() + " " + e.getSalary())
                                .findFirst()

                )));
        System.out.println(empWithSecondHighestSalaryFromEachDept);

        // employees from each department with more than average salary
        Map<String, List<Employee>> result = employeeList.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDept,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                empList -> {

                                    double avgSalary = empList.stream()
                                            .collect(Collectors.averagingDouble(Employee::getSalary));

                                    return empList.stream()
                                            .filter(emp -> emp.getSalary() > avgSalary)
                                            .collect(Collectors.toList());
                                }
                        )
                ));


    }

}
