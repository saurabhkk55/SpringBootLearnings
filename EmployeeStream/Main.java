package EmployeeStream;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        Employee emp = new Employee();
        List<Employee> empList = emp.getEmpList();

        // Group employees by location and join names
        Map<String, String> groupEmployeesByLocationAndJoinNames = empList.stream()
                .collect(Collectors.groupingBy(Employee::getLocation, Collectors.collectingAndThen(
                        Collectors.toList(),
                        employeeList -> employeeList.stream()
                                .map(Employee::getName)
                                .collect(Collectors.joining(", "))
                )));
        System.out.println(groupEmployeesByLocationAndJoinNames); // {Chennai=Jeya, Pune=Shubham, Kolkata=Gobinda, Saptarshi}

        // Method 1: Convert employee list into Name → Mobile map
        Map<String, List<String>> collect = empList.stream()
                .collect(Collectors.toMap(Employee::getName, Employee::getMobileNum));
        System.out.println(collect); // {Jeya=[9748294522, 9748294523], Gobinda=[9748294524, 9748294525], Saptarshi=[9748294555, 9748294529], Shubham=[9748294526, 9748294527]}

        // Method 2: Convert employee list into Name → Mobile map
        Map<String, List<String>> collect1 = empList.stream()
                .collect(Collectors.toMap(e -> e.getName(), e -> e.getMobileNum()));
        System.out.println(collect1); // {Jeya=[9748294522, 9748294523], Gobinda=[9748294524, 9748294525], Saptarshi=[9748294555, 9748294529], Shubham=[9748294526, 9748294527]}
    }
}
