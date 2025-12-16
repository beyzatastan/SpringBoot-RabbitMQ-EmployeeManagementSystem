package com.beyzatastan.employee_service.controller;

import com.beyzatastan.employee_service.dto.department.DepartmentCreateRequest;
import com.beyzatastan.employee_service.dto.department.DepartmentResponse;
import com.beyzatastan.employee_service.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public DepartmentResponse create(
            @RequestBody @Valid DepartmentCreateRequest request) {
        return departmentService.create(request);
    }

    @GetMapping
    public List<DepartmentResponse> getAll() {
        return departmentService.getAll();
    }
}
