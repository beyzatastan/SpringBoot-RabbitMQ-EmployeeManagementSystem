package com.beyzatastan.salary_service.repository;

import com.beyzatastan.salary_service.entity.SalaryRecords;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryRecorsRepository extends JpaRepository<SalaryRecords,Long> {
}
