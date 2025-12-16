package com.beyzatastan.employee_service.repository;

import com.beyzatastan.employee_service.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmployeeRepository extends JpaRepository<Employee, Long> {

    public Employee getEmployeeById(Long id);
    public Employee getEmployeeByEmail(String email);
    public boolean existsByEmail(String email);
    public boolean existsById(Long id);
}
