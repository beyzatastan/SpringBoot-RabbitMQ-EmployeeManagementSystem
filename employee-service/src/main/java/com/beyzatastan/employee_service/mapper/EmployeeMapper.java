package com.beyzatastan.employee_service.mapper;

import com.beyzatastan.employee_service.dto.employee.EmployeeResponse;
import com.beyzatastan.employee_service.entity.Employee;

public class EmployeeMapper {

    public static EmployeeResponse toResponse(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();

        response.setId(employee.getId());
        response.setEmployeeCode(employee.getEmployeeCode());
        response.setFirstName(employee.getFirstName());
        response.setLastName(employee.getLastName());
        response.setEmail(employee.getEmail());
        response.setPhone(employee.getPhone());
        response.setHireDate(employee.getHireDate());
        response.setPosition(employee.getPosition());
        response.setSalary(employee.getSalary());
        response.setIsActive(employee.getIsActive());
        response.setAddress(employee.getAddress());

        if (employee.getDepartment() != null) {
            response.setDepartment(
                    DepartmentMapper.toResponse(employee.getDepartment())
            );
        }


        return response;
    }
}
