package com.gonzalomendozafullstack.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gonzalomendozafullstack.app.exception.ResourceNotFoundException;
import com.gonzalomendozafullstack.app.model.Employee;
import com.gonzalomendozafullstack.app.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService{
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	
	@Override
	public Employee saveEmployee(Employee employee) {
	 Optional<Employee> saveEmployee = employeeRepository.findByEmail(employee.getEmail());
	
	 if(saveEmployee.isPresent()) {
		 throw new ResourceNotFoundException("The Employee with this e-mail no exist");
	 }
	 return employeeRepository.save(employee);
	}

	@Override
	public List<Employee> getAllEmployees() {
		
		return employeeRepository.findAll();
	}

	@Override
	public Optional<Employee> getEmployeeById(Long id) {
		
		return employeeRepository.findById(id);
	}

	@Override
	public Employee updateEmployee(Employee updateEmployee) {
		return employeeRepository.save(updateEmployee);
	}

	@Override
	public void deleteEmployee(Long id) {
		employeeRepository.deleteById(id);
		
	}

}
