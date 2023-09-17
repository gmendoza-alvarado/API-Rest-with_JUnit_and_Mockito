package com.gonzalomendozafullstack.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.gonzalomendozafullstack.app.model.Employee;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerTestRestTemplateTests {

	@Autowired
	TestRestTemplate restTemplate;
	
	@Test
	@Order(1)
	void testSaveEmployee() {
		
		Employee employee = Employee.builder()
				.id(1l)
				.name("Gonzalo")
				.surName("Mendoza")
				.email("gmendoza.alvarado@hotmail.com")
				.build();
		
		ResponseEntity<Employee> response = restTemplate.postForEntity("http://localhost:8080/api/employees",employee, Employee.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
		
		Employee createdEmployee = response.getBody();
		assertNotNull(createdEmployee);
		
		assertEquals(1l, createdEmployee.getId());
		assertEquals("Gonzalo", createdEmployee.getName());
		assertEquals("Mendoza", createdEmployee.getSurName());
		assertEquals("gmendoza.alvarado@hotmail.com", createdEmployee.getEmail());
		
	}
	
	@Test
	@Order(2)
	void testListEmployees() {
		ResponseEntity<Employee[]> response = restTemplate.getForEntity("http://localhost:8080/api/employees", Employee[].class);
		
		List<Employee> employeesList = Arrays.asList(response.getBody());
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
		
		assertEquals(1, employeesList.size());
		assertEquals(1l, employeesList.get(0).getId());
		assertEquals("Gonzalo", employeesList.get(0).getName());
		assertEquals("Mendoza", employeesList.get(0).getSurName());
		assertEquals("gmendoza.alvarado@hotmail.com", employeesList.get(0).getEmail());

		
	}
	
	@Test
	@Order(3)
	void testGetEmployeeById() {
		ResponseEntity<Employee> response = restTemplate.getForEntity("http://localhost:8080/api/employees/1", Employee.class);

		Employee employee = response.getBody();
		
		assertNotNull(employee);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
		assertEquals(1l, employee.getId());
		assertEquals("Gonzalo", employee.getName());
		assertEquals("Mendoza", employee.getSurName());
		assertEquals("gmendoza.alvarado@hotmail.com", employee.getEmail());
		
	}
	
	@Test
	@Order(4)
	void testDeleteEmployee() {
		ResponseEntity<Employee[]> response = restTemplate.getForEntity("http://localhost:8080/api/employees", Employee[].class);
		List<Employee> employeesList = Arrays.asList(response.getBody());
		assertEquals(1, employeesList.size());
		
		Map<String, Long> pathVariables = new HashMap<>();
		pathVariables.put("id", 1l);
		ResponseEntity<Void> exchange = restTemplate.exchange("http://localhost:8080/api/employees/{id}", HttpMethod.DELETE,null,Void.class,pathVariables);
		assertEquals(HttpStatus.OK, exchange.getStatusCode());
		assertFalse(exchange.hasBody());
		
		response = restTemplate.getForEntity("http://localhost:8080/api/employees", Employee[].class);
		employeesList = Arrays.asList(response.getBody());
		assertEquals(0, employeesList.size());
		
		ResponseEntity<Employee> detailResponse = restTemplate.getForEntity("http://localhost:8080/api/employees/2", Employee.class);
		assertEquals(HttpStatus.NOT_FOUND, detailResponse.getStatusCode());
		assertFalse(detailResponse.hasBody());
		
	}
}
