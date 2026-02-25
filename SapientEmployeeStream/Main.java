package SapientEmployeeStream;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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


        Map<String, Optional<Employee>> collect = employeeList.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparing(Employee::getSalary)),
                                empOpt -> empOpt
//                                        .map(e -> e.getDepartment() + " => " + e.getName())
//                                        .orElse("")
                        )
                ));
//                .values()
//                .stream()
//                .collect(Collectors.toList());

        System.out.println(collect);

        Map<String, Optional<Long>> collect1 = employeeList.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                emplist -> emplist.stream()
                                        .max((e1, e2) -> Math.toIntExact(e1.getSalary() - e2.getSalary()))
                                        .map(Employee::getSalary)
                        )
                ));
        System.out.println(collect1);
    }
}
