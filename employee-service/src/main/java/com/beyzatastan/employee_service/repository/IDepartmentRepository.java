package com.beyzatastan.employee_service.repository;

import com.beyzatastan.employee_service.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDepartmentRepository extends JpaRepository<Department, Long> {
    public Department getDepartmentById(Long id);
    public Department getDepartmentByName(String name);
    public boolean existsById(Long id);
    public boolean existsByName(String name);
}
