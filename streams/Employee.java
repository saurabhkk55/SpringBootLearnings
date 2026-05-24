package streams;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Map<String, Optional<String>> empWIthMaxSalaryFromEachDept = employeeList.stream()
                .collect(Collectors.groupingBy(Employee::getDept, Collectors.collectingAndThen(
                        Collectors.toList(),
                        employees -> {
//                            employees.stream().map(Employee::getSalary).mapToDouble(n -> n).max()
                            return employees.stream()
                                    .distinct()
                                    .sorted((em1, em2) -> (int) (em2.getSalary() - em1.getSalary()))
                                    .limit(1)
                                    .map(em -> em.getName() + " => " + em.getSalary())
                                    .findFirst();

                        }
                )));
        System.out.println(empWIthMaxSalaryFromEachDept);

        // employee with second highest Salary From Each Dept
        Map<String, Optional<String>> empWith2ndHighestSalaryFromEachDept = employeeList.stream()
                .collect(Collectors.groupingBy(Employee::getDept, Collectors.collectingAndThen(
                        Collectors.toList(),
                        employees -> {
//                            employees.stream().distinct().map(Employee::getSalary).sorted(Collections.reverseOrder()).skip(1).findFirst()
                            return employees.stream()
                                    .distinct()
                                    .sorted((em1, em2) -> (int) (em2.getSalary() - em1.getSalary()))
                                    .skip(1)
                                    .limit(1)
                                    .map(em -> em.getName() + " => " + em.getSalary())
                                    .findFirst();
                        }
                )));
        System.out.println(empWith2ndHighestSalaryFromEachDept);

        // employees from each department with more than average salary
        Map<String, List<String>> empWithMoreThanAvgSalaryFromEachDept = employeeList.stream()
                .collect(Collectors.groupingBy(Employee::getDept, Collectors.collectingAndThen(
                        Collectors.toList(),
                        employees -> {
                            double avgSalary = employees.stream().map(Employee::getSalary).mapToDouble(n -> n).average().getAsDouble();

                            return employees.stream()
                                    .filter(emp -> emp.getSalary() > avgSalary)
                                    .map(em -> em.getName() + " => " + em.getSalary())
                                    .toList();
                        }
                )));

        System.out.println(empWithMoreThanAvgSalaryFromEachDept);
    }
}
