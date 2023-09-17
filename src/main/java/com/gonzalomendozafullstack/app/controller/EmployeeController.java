package com.gonzalomendozafullstack.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gonzalomendozafullstack.app.model.Employee;
import com.gonzalomendozafullstack.app.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
	
	@Autowired
	EmployeeService employeeService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Employee saveEmployee(@RequestBody Employee employee) {
		return employeeService.saveEmployee(employee);
	}
	
	@GetMapping
	public List<Employee> ListEmployees(){
		return employeeService.getAllEmployees();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") long employeeId){
		return employeeService.getEmployeeById(employeeId)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable("id") long employeeId,@RequestBody Employee employee ){
		return employeeService.getEmployeeById(employeeId)
				.map(saveEmployee -> {
					saveEmployee.setName(employee.getName());
					saveEmployee.setSurName(employee.getSurName());
					saveEmployee.setEmail(employee.getEmail());
					
					Employee updateEmployee = employeeService.updateEmployee(saveEmployee);
					return new ResponseEntity<>(updateEmployee,HttpStatus.OK);
				}).orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable("id") long employeeId){
		employeeService.deleteEmployee(employeeId);
		return new ResponseEntity<String>("Delete Employee succesfully", HttpStatus.OK);
	}

}
