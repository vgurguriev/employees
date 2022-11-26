package com.example.pairofemployees.entity.dto;

import com.opencsv.bean.CsvBindByName;

import java.time.LocalDate;

public class EmployeeDTO {
    @CsvBindByName
    private long EmpId;

    @CsvBindByName
    private long ProjectID;

    @CsvBindByName
    private String DateFrom;

    @CsvBindByName
    private String DateTo;

    public EmployeeDTO(long empId, long projectID, String dateFrom, String dateTo) {
        EmpId = empId;
        ProjectID = projectID;
        DateFrom = dateFrom;
        DateTo = dateTo;
    }

    public EmployeeDTO() {}

    public long getEmpId() {
        return EmpId;
    }

    public EmployeeDTO setEmpId(long empId) {
        EmpId = empId;
        return this;
    }

    public long getProjectID() {
        return ProjectID;
    }

    public EmployeeDTO setProjectID(long projectID) {
        ProjectID = projectID;
        return this;
    }

    public String getDateFrom() {
        return DateFrom;
    }

    public EmployeeDTO setDateFrom(String dateFrom) {
        DateFrom = dateFrom;
        return this;
    }

    public String getDateTo() {
        return DateTo;
    }

    public EmployeeDTO setDateTo(String dateTo) {
        DateTo = dateTo;
        return this;
    }
}
