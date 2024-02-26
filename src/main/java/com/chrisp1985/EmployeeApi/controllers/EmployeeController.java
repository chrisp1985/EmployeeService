package com.chrisp1985.EmployeeApi.controllers;

import com.chrisp1985.EmployeeApi.data.Employee;
import com.chrisp1985.EmployeeApi.data.EmployeeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @PostConstruct
    public void doThisAfterTheServiceStarts() {
        System.out.println("Results Controller started.");
    }

    @GetMapping(value = "/{employeeId}")
    public Employee getEmployeeById(@PathVariable Long employeeId) {
        return employeeRepository.findById(String.valueOf(employeeId)).orElse(null);
    }

    @GetMapping(value = "/{employeeId}/salary")
    public Long getEmployeeSalaryById(@PathVariable Long employeeId) {
        return employeeRepository.findById(String.valueOf(employeeId)).get().salary;
    }

    @GetMapping(value = "")
    @Cacheable("allEmployees")
    public List<Employee> getAllEmployees() {
        System.out.println(employeeRepository.findAll());
        return employeeRepository.findAll();
    }

    @GetMapping(value = "/clearcache")
    @CacheEvict("allEmployees")
    public void clearCache() {
        // Manually clear the cache.
    }

    /**
     * EXAMPLE:
     *
     * {
     *     "lastName": "PARSONS",
     *     "job": "ENGINEERING MGR",
     *     "managerId": 7902,
     *     "hiredate": "1980-12-17",
     *     "salary": 1200,
     *     "commissionValue": null,
     *     "deptNumber": 20
     * }
     */
    @PostMapping(path = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict("allEmployees")
    public void addNewEmployee(@RequestBody Employee employee) {
        employeeRepository.save(employee);
    }
}
