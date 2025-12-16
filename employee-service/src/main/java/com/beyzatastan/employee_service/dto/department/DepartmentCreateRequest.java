package com.beyzatastan.employee_service.dto.department;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DepartmentCreateRequest {

    @NotBlank
    private String name;

    private String description;
}