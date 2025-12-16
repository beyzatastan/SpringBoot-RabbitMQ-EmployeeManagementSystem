package com.beyzatastan.employee_service.service;

import com.beyzatastan.employee_service.dto.department.DepartmentCreateRequest;
import com.beyzatastan.employee_service.dto.department.DepartmentResponse;
import com.beyzatastan.employee_service.entity.Department;
import com.beyzatastan.employee_service.exception.ResourceAlreadyExistsException;
import com.beyzatastan.employee_service.exception.ResourceNotFoundException;
import com.beyzatastan.employee_service.mapper.DepartmentMapper;
import com.beyzatastan.employee_service.repository.IDepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final IDepartmentRepository departmentRepository;

    public DepartmentResponse create(DepartmentCreateRequest request) {
        if (departmentRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException("Department already exists");
        }

        Department department = DepartmentMapper.toEntity(request);
        departmentRepository.save(department);
        return DepartmentMapper.toResponse(department);
    }

    public List<DepartmentResponse> getAll() {
        return departmentRepository.findAll()
                .stream()
                .map(DepartmentMapper::toResponse)
                .toList();
    }

    public Department getEntityById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found")
                );
    }
}
