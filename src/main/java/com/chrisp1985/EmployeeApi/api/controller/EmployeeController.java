package com.chrisp1985.EmployeeApi.api.controller;

import com.chrisp1985.EmployeeApi.data.Employee;
import com.chrisp1985.EmployeeApi.data.EmployeeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @Operation(
            summary = "",
            description = ""
    )
    @GetMapping(value = "/{employeeId}")
    public Employee getEmployeeById(@PathVariable Long employeeId) {
        return employeeRepository.findById(String.valueOf(employeeId)).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id %d not found", employeeId)));
    }

    @GetMapping(value = "/{employeeId}/salary")
    public Long getEmployeeSalaryById(
            @Parameter(description = "The employee ID to search for")
            @PathVariable Long employeeId) {
        Employee employee = employeeRepository.findById(String.valueOf(employeeId)).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id %d not found", employeeId)));
        return employee.salary;
    }

    @GetMapping(value = "")
    @Cacheable("allEmployees")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping(value = "/clearcache")
    @CacheEvict("allEmployees")
    public void clearCacheManual() {
        // Manually clear the cache.
    }

    /**
     * EXAMPLE:
     * <p>
     * {
     * "lastName": "PARSONS",
     * "job": "ENGINEERING MGR",
     * "managerId": 7902,
     * "hiredate": "1980-12-17",
     * "salary": 1200,
     * "commissionValue": null,
     * "deptNumber": 20
     * }
     *
     * @return
     */
    @PostMapping(path = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict("allEmployees")
    public ResponseEntity<Employee> addNewEmployee(@RequestBody Employee employee) {
        Employee savedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(savedEmployee);
    }
}
