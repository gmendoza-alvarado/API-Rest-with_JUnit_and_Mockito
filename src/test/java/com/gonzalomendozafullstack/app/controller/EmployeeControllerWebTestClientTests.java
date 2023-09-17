package com.gonzalomendozafullstack.app.controller;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.gonzalomendozafullstack.app.model.Employee;

import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerWebTestClientTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	@Order(1)
	void testSaveEmployee() {
		// given
		Employee employee = Employee.builder().id(3l).name("ñ").surName("l")
				.email("gmza.alvarado@hotmail.com").build();

		// when
		webTestClient.post().uri("http://localhost:8080/api/employees").contentType(MediaType.APPLICATION_JSON)
				.bodyValue(employee).exchange() // send the request

				// then
				.expectStatus().isCreated().expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody()
				.jsonPath("$.id").isEqualTo(employee.getId()).jsonPath("$.name").isEqualTo(employee.getName())
				.jsonPath("$.surName").isEqualTo(employee.getSurName()).jsonPath("$.email")
				.isEqualTo(employee.getEmail());
	}

	@Test
	@Order(2)
	void testGetEmployeeById() {
		webTestClient.get().uri("http://localhost:8080/api/employees/1").exchange().expectStatus().isOk().expectHeader()
				.contentType(MediaType.APPLICATION_JSON).expectBody().jsonPath("$.id").isEqualTo(1).jsonPath("$.name")
				.isEqualTo("Gonzalo").jsonPath("$.surName").isEqualTo("Mendoza").jsonPath("$.email")
				.isEqualTo("gmendoza.alvarado@hotmail.com");
	}

	@Test
	@Order(3)
	void testListEmployees() {
		webTestClient.get().uri("http://localhost:8080/api/employees").exchange().expectStatus().isOk().expectHeader()
				.contentType(MediaType.APPLICATION_JSON).expectBody().jsonPath("$[0].name").isEqualTo("Gonzalo")
				.jsonPath("$[0].surName").isEqualTo("Mendoza").jsonPath("$[0].email")
				.isEqualTo("gmendoza.alvarado@hotmail.com").jsonPath("$").isArray().jsonPath("$").value(hasSize(1));
	}

	@Test
	@Order(4)
	void testGetListEmployees() {
		webTestClient.get().uri("http://localhost:8080/api/employees").exchange().expectStatus().isOk().expectHeader()
				.contentType(MediaType.APPLICATION_JSON).expectBodyList(Employee.class).consumeWith(response -> {
					List<Employee> employees = response.getResponseBody();
					Assertions.assertEquals(1, employees.size());
					Assertions.assertNotNull(employees);
				});
	}

	@Test
	@Order(5)
	void testUpdateEmployee() {
		Employee updateEmployee = Employee.builder().name("Gonzalo").surName("Mendoza")
				.email("gmendoza.alvarado@hotmail.com").build();

		webTestClient.put().uri("http://localhost:8080/api/employees/1").contentType(MediaType.APPLICATION_JSON)
				.bodyValue(updateEmployee).exchange() // send the request

				// then
				.expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON);
	}

	@Test
	@Order(6)
	void testDeleteEmployee() {
		webTestClient.get().uri("http://localhost:8080/api/employees").exchange().expectStatus().isOk().expectHeader()
				.contentType(MediaType.APPLICATION_JSON).expectBodyList(Employee.class).hasSize(2);

		webTestClient.delete().uri("http://localhost:8080/api/employees/2").exchange().expectStatus().isOk();

		webTestClient.get().uri("http://localhost:8080/api/employees").exchange().expectStatus().isOk().expectHeader()
				.contentType(MediaType.APPLICATION_JSON).expectBodyList(Employee.class).hasSize(1);

		webTestClient.get().uri("http://localhost:8080/api/employees/1").exchange().expectStatus().is4xxClientError();
	}

}
