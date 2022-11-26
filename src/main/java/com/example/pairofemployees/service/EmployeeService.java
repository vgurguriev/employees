package com.example.pairofemployees.service;

import com.example.pairofemployees.entity.Employee;
import com.example.pairofemployees.entity.dto.EmployeeDTO;
import com.example.pairofemployees.repository.EmployeeRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void saveEmployees(MultipartFile file) throws IOException {
        Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));


            CsvToBean<EmployeeDTO> csvToBean = new CsvToBeanBuilder<EmployeeDTO>(reader)
                    .withType(EmployeeDTO.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();


            List<EmployeeDTO> employeesDTO = csvToBean.parse();


            List<Employee> employees =  employeesDTO
                    .stream()
                            .map(employeeDTO -> new Employee()
                                    .setEmpId(employeeDTO.getEmpId())
                                    .setProjectID(employeeDTO.getProjectID())
                                    .setDateFrom(LocalDate.parse(employeeDTO.getDateFrom()))
                                    .setDateTo(getTimeTo(employeeDTO.getDateTo())))
                    .collect(Collectors.toList());

        employeeRepository.saveAll(employees);
    }

    private LocalDate getTimeTo(String dateTo) {
        if (dateTo.equals("NULL")) {
            return LocalDate.now();
        }

        return LocalDate.parse(dateTo);
    }

    public List<Employee> getAllEmployees() {
        return this.employeeRepository
                .findAll();
    }
}
