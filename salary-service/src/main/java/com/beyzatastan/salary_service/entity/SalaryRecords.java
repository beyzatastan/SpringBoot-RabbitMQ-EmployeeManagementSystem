package com.beyzatastan.salary_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "salary_records")
@Data
public class SalaryRecords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "base_salary", nullable = false)
    private BigDecimal baseSalary;

    private BigDecimal bonus = BigDecimal.ZERO;

    private BigDecimal deductions = BigDecimal.ZERO;

    @Column(name = "net_salary", nullable = false)
    private BigDecimal netSalary;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Column(name = "payment_month", nullable = false)
    private Integer paymentMonth;

    @Column(name = "payment_year", nullable = false)
    private Integer paymentYear;

    private String status = "PENDING";
}