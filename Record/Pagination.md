Entity
```java
package com.theCuriousCoder.interview_practice_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "department")
    private String dept;

    @Column(name = "joining_date")
    private Date joiningDate;

}
```

Controller
```java
package com.theCuriousCoder.interview_practice_service.controller;

import com.theCuriousCoder.interview_practice_service.model.Employee;
import com.theCuriousCoder.interview_practice_service.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/list")
    public List<Employee> getEmployees(@RequestParam(required = false, defaultValue = "1") int pageNo,
                                       @RequestParam(required = false, defaultValue = "5") int pageSize,
                                       @RequestParam(required = false, defaultValue = "id") String sortBy,
                                       @RequestParam(required = false, defaultValue = "ASC") String sortDir,
                                       @RequestParam(required = false) String search) {
        Sort sort = null;
        if(sortDir.equalsIgnoreCase("ASC")) {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }
        return employeeService.fetchAllEmployees(PageRequest.of(pageNo-1,pageSize, sort), search);
    }
}
```

Service
```java
package com.theCuriousCoder.interview_practice_service.services;

import com.theCuriousCoder.interview_practice_service.model.Employee;
import com.theCuriousCoder.interview_practice_service.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> fetchAllEmployees(Pageable pageable, String search) {
        if(search==null) {
            return employeeRepository.findAll(pageable).getContent();
        } else {
            return employeeRepository.findByName(search, pageable).getContent();
        }
    }
}
```

Repository
```java
package com.theCuriousCoder.interview_practice_service.repository;

import com.theCuriousCoder.interview_practice_service.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Page<Employee> findByName(String name, Pageable pageable);
}
```
