package com.example.pairofemployees.entity;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CsvBindByName(column = "EmpId")
    private long employeeId;

    @CsvBindByName(column = "ProjectID")
    private long projectID;

    @CsvBindByName(column = "DateFrom")
    private LocalDate dateFrom;

    @CsvBindByName(column = "DateTo")
    private LocalDate dateTo;

    public Employee(long id, long employeeId, long projectID,
                    LocalDate dateFrom, LocalDate dateTo) {
        this.id = id;
        this.employeeId = employeeId;
        this.projectID = projectID;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public Employee() {}

    public long getId() {
        return id;
    }

    public Employee setId(long id) {
        this.id = id;
        return this;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public Employee setEmployeeId(long empId) {
        this.employeeId = empId;
        return this;
    }

    public long getProjectID() {
        return projectID;
    }

    public Employee setProjectID(long projectID) {
        this.projectID = projectID;
        return this;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public Employee setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public Employee setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
        return this;
    }
}
