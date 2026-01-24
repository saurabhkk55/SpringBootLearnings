package EmployeeStream;

import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Employee emp = new Employee();
        List<Employee> empList = emp.getEmpList();

        empList.stream()
                .collect(Collectors.groupingBy(e -> e.getLocation(), Collectors.mapping(e -> e.getName(), Collectors.joining(", "))))
                .entrySet().stream()
                .forEach(e -> System.out.println(e.getKey() + " => " + e.getValue()));

        empList.stream()
                .collect(Collectors.toMap(e -> e.getName(), e -> e.getMobileNum()))
                .entrySet().stream()
                .forEach(e -> System.out.println(e.getKey() + " => " + e.getValue()));
    }
}
