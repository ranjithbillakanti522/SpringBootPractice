package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String>{
//                                            PagingAndSortingRepository<Employee, String> {
	List<Employee> findByNameAndCompanyAllIgnoreCase(String name,String Company);
	List<Employee> findByCompanyContaining(String company);

}
