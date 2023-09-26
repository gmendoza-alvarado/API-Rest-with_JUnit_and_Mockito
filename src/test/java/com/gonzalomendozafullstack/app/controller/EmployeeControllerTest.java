package com.gonzalomendozafullstack.app.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gonzalomendozafullstack.app.model.Employee;
import com.gonzalomendozafullstack.app.service.EmployeeService;

@WebMvcTest
class EmployeeControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private EmployeeService employeeService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	
	
	@Test
	void testSaveEmployee() throws Exception{
		//given
		Employee employee = Employee.builder()
				.id(1l)
				.name("Gonzalo")
				.surName("Mendoza")
				.email("gmendoza.alvarado@hotmail.com")
				.build();
				
		
		given(employeeService.saveEmployee(any(Employee.class)))
		.willAnswer((invocation) -> invocation.getArgument(0));
		
		//when
		ResultActions response = mockMvc.perform(post("/api/employees")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employee)));
		
		//then
		response.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name", is(employee.getName())))
				.andExpect(jsonPath("$.surName", is(employee.getSurName())))
				.andExpect(jsonPath("$.email", is(employee.getEmail())));
	}
	
	@Test
	void testListEmployees() throws Exception{
		//given
		List<Employee> employeesList = new ArrayList<>();
		
		employeesList.add(Employee.builder()
				.name("Gonzalo")
				.surName("Mendoza")
				.email("gmendoza.alvarado@hotmail.com")
				.build());
		employeesList.add(Employee.builder()
				.name("Fernanda")
				.surName("Salas")
				.email("fernanda.salas@hotmail.com")
				.build());
		employeesList.add(Employee.builder()
				.name("Mildred")
				.surName("Mendoza")
				.email("mildred.loba@hotmail.com")
				.build());
		employeesList.add(Employee.builder()
				.name("Lorena")
				.surName("Alvarado")
				.email("lorena.alvarado@hotmail.com")
				.build());
		employeesList.add(Employee.builder()
				.name("Gonzalo")
				.surName("Garcia")
				.email("gonzalo.garcia@hotmail.com")
				.build());
		
		given(employeeService.getAllEmployees()).willReturn(employeesList);
		
		//when
		ResultActions response = mockMvc.perform(get("/api/employees"));
		
		//then
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.size()", is(employeesList.size())));
		
	}
	
	@Test
	void testGetEmployeeById() throws Exception{
		
		//given
		long employeeId= 1l;
		
		Employee employee = Employee.builder()
				.name("Gonzalo")
				.surName("Mendoza")
				.email("gmendoza.alvarado@hotmail.com")
				.build();
		
		given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));
		
		//when
		ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));
		
		//then
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.name", is(employee.getName())))
				.andExpect(jsonPath("$.surName", is(employee.getSurName())))
				.andExpect(jsonPath("$.email", is(employee.getEmail())));
		
		
		
	}
	
	@Test
	void testGetEmployeeByIdNotFound() throws Exception{
		
		//given
		long employeeId= 1l;
		
		
		given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
		
		//when
		ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));
		
		//then
		response.andExpect(status().isNotFound())
				.andDo(print());
		
	}
	
	@Test
	void testUpdateEmployee() throws Exception {
		// given
		long employeeId = 1l;
		Employee employee = Employee.builder().name("Gonzalo").surName("Mendoza").email("gmendoza.alvarado@hotmail.com")
				.build();

		given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));
		given(employeeService.updateEmployee(any(Employee.class)))
				.willAnswer((inovcation) -> inovcation.getArgument(0));

		Employee updateEmployee = Employee.builder().name("Fernanda").surName("Salas").email("fernandass886@gmail.com")
				.build();

		// when
		ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateEmployee)));
		// then
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.name", is(employee.getName())))
				.andExpect(jsonPath("$.surName", is(employee.getSurName())))
				.andExpect(jsonPath("$.email", is(employee.getEmail())));
	}

	@Test
	void testUpdateEmployeeNotFound() throws Exception {
		// given
		long employeeId = 1l;

		given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
		given(employeeService.updateEmployee(any(Employee.class)))
				.willAnswer((inovcation) -> inovcation.getArgument(0));

		Employee updateEmployee = Employee.builder().name("Fernanda").surName("Salas").email("fernandass886@gmail.com")
				.build();

		// when
		ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateEmployee)));
		// then
		response.andExpect(status().isNotFound())
				.andDo(print());
	}
	
	@Test
	void testDeleteEmployee() throws Exception{
		//given
		long employeeId = 1l;
		
		willDoNothing().given(employeeService).deleteEmployee(employeeId);
		
		//when
		ResultActions response = mockMvc.perform(delete("/api/employees/{id}",employeeId));
		
		//then
		response.andExpect(status().isOk())
				.andDo(print());
	}
	
}
