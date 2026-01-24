package SapientEmployeeStream;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class Main {
    static void main(String[] args) {
        List<Employee> employeeList = Employee.getEmployeeList();

        employeeList.stream()
                .sorted((e1, e2) -> e2.getSalary().compareTo(e1.getSalary()))
                .limit(1)
                .forEach(e -> System.out.println(e));

        employeeList.stream()
                .max((e1, e2) -> e1.getSalary().compareTo(e2.getSalary()))
                .map(e -> e.getName())
                .ifPresent(System.out::println);

        List<String> ListOfEmpWithMaxSalaryFromEachDept = employeeList.stream()
                .collect(Collectors.groupingBy(e -> e.getDepartment(), Collectors.toList()))
                .entrySet().stream()
                .map(x -> {
                    Optional<String> empWithMaxSalaryInCurrentDept = x.getValue().stream()
                            .max((e1, e2) -> e1.getSalary().compareTo(e2.getSalary()))
                            .map(e -> e.getName());
                    return (x.getKey().toString() + " " + empWithMaxSalaryInCurrentDept.get().toString());
                })
                .collect(Collectors.toList());

        System.out.println(ListOfEmpWithMaxSalaryFromEachDept);
    }
}
