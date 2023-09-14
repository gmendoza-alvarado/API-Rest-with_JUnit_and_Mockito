package com.gonzalomendozafullstack.app.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.gonzalomendozafullstack.app.model.Employee;

@DataJpaTest //COMPONENETES DE LA CAPA DE PERSISTENCIA (SOLO ENTITY, REPOSITORIO)
public class EmployeeRepositoryTest {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	private Employee employee;
	
	@BeforeEach
	void setUp() {
		employee = Employee.builder()
				.name("Gonzalo")
				.surName("Mendoza")
				.email("gmendoza.alvarado@hotmail.com")
				.build();
	}
	@Test
	void testSaveEmployee() {
		//given - dada o condicion previo o configuracion
		Employee employee = Employee.builder()
				.name("Gonzalo")
				.surName("Mendoza")
				.email("gmendoza.alvarado@hotmail.com")
				.build();
		
		//when- las condiciones de las acciones que se van a ejecutar
		Employee saveEmployee = employeeRepository.save(employee);
		
		//THEN sE DEBE MOSTRAR LOS MENSAJES DE VALIDACION APROPIADOS
		assertThat(saveEmployee).isNotNull();
		assertThat(saveEmployee.getId()).isGreaterThan(0);
		
		
	}
	
	@Test
	void testListEmployee() {
		//given
		Employee employee = Employee.builder()
				.name("Fernanda")
				.surName("Salas")
				.email("gmendoza.alvarado@hotmail.com")
				.build();
		employeeRepository.save(employee);
		employeeRepository.save(this.employee);
		//when
		List<Employee> listEmployees = employeeRepository.findAll();
		
		//then
		assertThat(listEmployees).isNotNull();
		assertThat(listEmployees.size()).isEqualTo(0);
		
	}
}
