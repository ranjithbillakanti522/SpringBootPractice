package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Employee;
import com.example.demo.repository.EmployeeRepository;

@RestController
public class EmployeeController {
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	
	@RequestMapping(method = RequestMethod.GET,path = "/getEmployees")
	public Page<Employee> getEmployees() {
		Sort sort=Sort.by("name");
		Page<Employee> page=employeeRepository.findAll(PageRequest.of(0, 5,sort));
		System.out.println(employeeRepository.findById("1823056").get());
	    page.get().forEach(e->System.out.println(e));
	    return page;
	}
	
	@RequestMapping(method = RequestMethod.GET,path = "/getbynameandcompany")
	public ResponseEntity<List<Employee>> findByNameAndCompany(@RequestParam String name,
			                                      @RequestParam String company){
		
		HttpHeaders httpheaders=new HttpHeaders();
		httpheaders.add("customheader", "check");
		httpheaders.setContentType(MediaType.APPLICATION_XML);
		List<Employee> emps=employeeRepository.findByNameAndCompanyAllIgnoreCase(name, company);
		ResponseEntity<List<Employee>> re=new ResponseEntity<List<Employee>>(emps, httpheaders, HttpStatus.ACCEPTED);
		return re;
		
	}
	@RequestMapping(method = RequestMethod.GET,path = "/companylike")
	public ResponseEntity<List<Employee>> findByCompanyNameLike(@RequestParam String company){
		HttpHeaders httpHeaders=new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_XML);
		List<Employee> emps=employeeRepository.findByCompanyContaining(company);
		return new ResponseEntity<List<Employee>>(emps, httpHeaders, HttpStatus.OK);
		
	}

}
