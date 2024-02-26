package com.chrisp1985.EmployeeApi.data;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "emp")
public class Employee {
    public Employee(String lastName, String job, Long managerId, Date hiredate, Long salary, Long commissionValue, Long deptNumber) {
        this.lastName = lastName;
        this.job = job;
        this.managerId = managerId;
        this.hiredate = hiredate;
        this.salary = salary;
        this.commissionValue = commissionValue;
        this.deptNumber = deptNumber;
    }

    @Column(name = "EMPNO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    public Long employeeId;

    @Column(name = "ENAME")
    public String lastName;

    @Column(name = "JOB")
    public String job;

    @Column(name = "MGR")
    public Long managerId;

    @Column(name = "HIREDATE")
    public Date hiredate;

    @Column(name = "SAL")
    public Long salary;

    @Column(name = "COMM")
    public Long commissionValue;

    @Column(name = "DEPTNO")
    public Long deptNumber;

    public Employee() {

    }
}
