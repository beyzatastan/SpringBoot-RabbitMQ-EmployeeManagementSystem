package com.beyzatastan.employee_service.repository;

import com.beyzatastan.employee_service.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmployeeRepository extends JpaRepository<Employee, Long> {
}
