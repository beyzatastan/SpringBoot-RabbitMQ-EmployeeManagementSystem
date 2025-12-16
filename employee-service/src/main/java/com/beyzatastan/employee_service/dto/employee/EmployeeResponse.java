package com.beyzatastan.employee_service.dto.employee;

import com.beyzatastan.employee_service.dto.department.DepartmentResponse;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EmployeeResponse {

    private Long id;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    private LocalDate hireDate;

    private String position;
    private BigDecimal salary;
    private Boolean isActive;

    private DepartmentResponse department;
    private String address;
}
