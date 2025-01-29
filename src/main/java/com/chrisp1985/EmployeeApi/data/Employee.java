package com.chrisp1985.EmployeeApi.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "emp")
public class Employee {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "EMPNO")
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
}
