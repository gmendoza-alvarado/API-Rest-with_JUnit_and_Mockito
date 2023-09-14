package com.gonzalomendozafullstack.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gonzalomendozafullstack.app.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

	Optional<Employee> findByEmail(String email);
}
