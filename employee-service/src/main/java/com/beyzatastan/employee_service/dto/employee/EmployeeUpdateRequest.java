package com.beyzatastan.employee_service.dto.employee;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EmployeeUpdateRequest {

    private String phone;
    private String position;
    private BigDecimal salary;
    private Boolean isActive;

    private Long departmentId;
    private String address;
}
