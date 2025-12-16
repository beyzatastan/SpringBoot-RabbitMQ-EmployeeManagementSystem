package com.beyzatastan.employee_service.mapper;

import com.beyzatastan.employee_service.dto.department.DepartmentCreateRequest;
import com.beyzatastan.employee_service.dto.department.DepartmentResponse;
import com.beyzatastan.employee_service.entity.Department;

public class DepartmentMapper {

    public static Department toEntity(DepartmentCreateRequest request) {
        return Department.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    public static DepartmentResponse toResponse(Department department) {
        DepartmentResponse response = new DepartmentResponse();
        response.setId(department.getId());
        response.setName(department.getName());
        response.setDescription(department.getDescription());
        return response;
    }
}
