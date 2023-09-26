package com.gonzalomendozafullstack.app.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gonzalomendozafullstack.app.exception.ResourceNotFoundException;
import com.gonzalomendozafullstack.app.model.Employee;
import com.gonzalomendozafullstack.app.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
	
	@Mock
	EmployeeRepository employeeRepository;
	
	@InjectMocks
	EmployeeServiceImpl employeeServiceImpl;
	
	private Employee employee;

	@BeforeEach
	void setUp() {
		employee = Employee.builder()
				.id(1l)
				.name("Gonzalo")
				.surName("Mendoza")
				.email("gmendoza.alvarado@hotmail.com")
				.build();
	}
	
	@Test
	void testSaveEmployee() {
		//given
		given(employeeRepository.findByEmail(employee.getEmail()))
		.willReturn(Optional.empty());
		given(employeeRepository.save(employee)).willReturn(employee);
		//when
		Employee saveEmployee = employeeServiceImpl.saveEmployee(employee);
	
		//then
		assertThat(saveEmployee).isNotNull();
	}
	
	@Test
	void testSaveEmployeeThrowException() {
		//given
		given(employeeRepository.findByEmail(employee.getEmail()))
		.willReturn(Optional.of(employee));
		
		
		//when
		assertThrows(ResourceNotFoundException.class, () ->{
			employeeServiceImpl.saveEmployee(employee);
		});
		
		//then
		verify(employeeRepository, never()).save(any(Employee.class));
	}
	
	@Test
	void testListEmployees() {
		//given
		Employee employee = Employee.builder()
				.id(2l)
				.name("Gonzalo")
				.surName(null)
				.email(null)
				.build();
		
	
		//when
		given(employeeRepository.findAll()).willReturn(List.of(this.employee,employee));
		//
		List<Employee> employees = employeeServiceImpl.getAllEmployees();
		
		assertThat(employees).isNotNull();
		assertThat(employees.size()).isEqualTo(2);
	}
	
	@Test
	void testCollectionEmployeesEmpty() {
		//given
		
	
		//when
		given(employeeRepository.findAll()).willReturn(Collections.emptyList());
		//
		List<Employee> employees = employeeServiceImpl.getAllEmployees();
		
		assertThat(employees).isEmpty();;
		assertThat(employees.size()).isEqualTo(0);
	}
	
	@Test
	void testGetEmployeById(){
		//given
		given(employeeRepository.findById(1l)).willReturn(Optional.of(employee));
		
		//then
		Employee saveEmployee = employeeServiceImpl.getEmployeeById(employee.getId()).get();
		
		assertThat(saveEmployee).isNotNull();
	}
	
	@Test
	void testUpdateEmployee() {
		given (employeeRepository.save(this.employee)).willReturn(employee);
		employee.setEmail("Mildred@hotmail.com");
		employee.setName("Mildred Mendoza");
		
		Employee updtaeEmployee = employeeServiceImpl.updateEmployee(employee);
		
		assertThat(updtaeEmployee.getEmail()).isEqualTo("Mildred@hotmail.com");
		assertThat(updtaeEmployee.getName()).isEqualTo("Mildred Mendoza");
		
	}
	
	@Test
	void deleteEmployee() {
		
		long employeeId = 1l;
		BDDMockito.willDoNothing().given(employeeRepository).deleteById(employeeId);
		
		employeeServiceImpl.deleteEmployee(employeeId);
		
		verify(employeeRepository,times(1)).deleteById(employeeId);
	}

}
