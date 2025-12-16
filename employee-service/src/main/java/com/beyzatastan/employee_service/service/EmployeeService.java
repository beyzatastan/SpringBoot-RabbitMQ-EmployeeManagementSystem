package com.beyzatastan.employee_service.service;

import com.beyzatastan.employee_service.dto.employee.EmployeeCreateRequest;
import com.beyzatastan.employee_service.dto.employee.EmployeeResponse;
import com.beyzatastan.employee_service.dto.employee.EmployeeUpdateRequest;
import com.beyzatastan.employee_service.entity.Department;
import com.beyzatastan.employee_service.entity.Employee;
import com.beyzatastan.employee_service.exception.ResourceNotFoundException;
import com.beyzatastan.employee_service.mapper.EmployeeMapper;
import com.beyzatastan.employee_service.repository.IEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final IEmployeeRepository employeeRepository;
    private final DepartmentService departmentService;

    public EmployeeResponse create(EmployeeCreateRequest request) {
        Department department =
                departmentService.getEntityById(request.getDepartmentId());

        Employee employee = new Employee();
        employee.setEmployeeCode(request.getEmployeeCode());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setDateOfBirth(request.getDateOfBirth());
        employee.setHireDate(request.getHireDate());
        employee.setPosition(request.getPosition());
        employee.setSalary(request.getSalary());
        employee.setIsActive(true);
        employee.setDepartment(department);
        employee.setAddress(request.getAddress());

        employeeRepository.save(employee);
        return EmployeeMapper.toResponse(employee);
    }

    public EmployeeResponse getById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found")
                );
        return EmployeeMapper.toResponse(employee);
    }

    public EmployeeResponse update(Long id, EmployeeUpdateRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found")
                );

        if (request.getPhone() != null)
            employee.setPhone(request.getPhone());

        if (request.getPosition() != null)
            employee.setPosition(request.getPosition());

        if (request.getSalary() != null)
            employee.setSalary(request.getSalary());

        if (request.getIsActive() != null)
            employee.setIsActive(request.getIsActive());

        if (request.getDepartmentId() != null) {
            Department department =
                    departmentService.getEntityById(request.getDepartmentId());
            employee.setDepartment(department);
        }

        if (request.getAddress() != null) {
            employee.setAddress(request.getAddress());
        }

        employeeRepository.save(employee);
        return EmployeeMapper.toResponse(employee);
    }

    public void delete(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found")
                );
        employeeRepository.delete(employee);
    }
}
