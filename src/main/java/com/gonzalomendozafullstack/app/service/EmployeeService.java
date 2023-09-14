package com.gonzalomendozafullstack.app.service;

import java.util.List;
import java.util.Optional;

import com.gonzalomendozafullstack.app.model.Employee;

public interface EmployeeService {
	
	Employee saveEmployee(Employee employee);
	
	List<Employee> getAllEmployees();
	
	Optional<Employee> getEmployeeById(Long id);
	
	Employee updateEmployee(Employee updateEmployee);
	
	void deleteEmployee(Long id);

}
