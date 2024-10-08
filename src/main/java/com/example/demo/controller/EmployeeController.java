package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.ScrollPosition.Direction;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Employee;
import com.example.demo.exception.EmployeeAlreadyExistException;
import com.example.demo.exception.NoItemFoundException;
import com.example.demo.projections.SubEmployeeProjection;
import com.example.demo.repository.EmployeeRepository;

import jakarta.transaction.Transactional;

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
	public ResponseEntity<Page<Employee>> findByCompanyNameLike(@RequestParam String company,int pageNum){
		HttpHeaders httpHeaders=new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_XML);
		Sort sort=Sort.by("salary","name");
		sort.ascending();
		Pageable page=PageRequest.of(pageNum, 2,sort);
		Page<Employee> emps=employeeRepository.findByCompanyContaining(company,page);
		emps.getTotalElements();emps.getTotalPages();
		return new ResponseEntity<Page<Employee>>(emps, httpHeaders, HttpStatus.OK);
		
	}
	@RequestMapping(method = RequestMethod.GET,path = "/getByName")
	public ResponseEntity<Employee> findByName(@RequestParam String name) {
		
		HttpHeaders httpheaders=new HttpHeaders();
		httpheaders.setContentType(MediaType.APPLICATION_XML);
		Optional<Employee> emp=employeeRepository.findByNameIgnoreCase(name);
		
		if(!emp.isPresent()) {
			throw new NoItemFoundException("there is no employee with name : "+name);
		}
		return new ResponseEntity<>(emp.get(), httpheaders, HttpStatus.OK);
		
	}
	@RequestMapping(path = "/findall",method = RequestMethod.GET)
	public ResponseEntity<Page<Employee>> findAll(@RequestParam int pageNo,int pageSize){
		
		HttpHeaders httpHeaders=new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_XML);
		Sort sort=Sort.by("name");
		Pageable pageable=PageRequest.of(pageNo, pageSize,sort);
		Page<Employee> emps=employeeRepository.findAll(pageable);
		ResponseEntity<Page<Employee>> response=new ResponseEntity<Page<Employee>>(emps, httpHeaders, HttpStatus.OK);
		return response;
	}
	@RequestMapping(path = "/findbyname",method=RequestMethod.GET)
	public ResponseEntity<Page<Employee>> findByname(@RequestParam String name,int pageNo,int pageSize){
		HttpHeaders httpheaders=new HttpHeaders();
		httpheaders.setContentType(MediaType.APPLICATION_XML);
		Pageable pageable=PageRequest.of(pageNo,pageSize);
		Page<Employee> emps=employeeRepository.findbyname(name, pageable);
		return new ResponseEntity<Page<Employee>>(emps,httpheaders,HttpStatus.OK);
	}
	@RequestMapping(path = "/saveemployee",method = RequestMethod.POST,consumes = "application/xml")
	public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee){
		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_XML);
		Optional<Employee> emp=employeeRepository.findById(employee.getId());
		System.out.println(emp);
		if(emp.isPresent()==true) {
			throw new EmployeeAlreadyExistException("Employee already exist with id "+employee.getId());
		}
		Employee e=employeeRepository.save(employee);
		ResponseEntity<Employee> response=new ResponseEntity<Employee>(e, headers, HttpStatus.CREATED);
		return response;
	}
	
	@Transactional
	@RequestMapping(path = "/updatewhereidlike",method = RequestMethod.PATCH)
	public ResponseEntity<Integer> updateWhereIdLike(@RequestParam String company,String id){
		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_XML);
		List<Employee> emps=employeeRepository.findWhereIdLike(id);
		emps.forEach(e->System.out.println(e));
		if(emps.isEmpty()==true) {
			throw new NoItemFoundException("there are no employees with is Like "+id);
		}
		
		int count=employeeRepository.updateCompanyById(id, company);
		ResponseEntity<Integer> response=new ResponseEntity<Integer>(count,
				                                                                    headers, 
				                                                                    HttpStatus.ACCEPTED);
		return response;
		
	}
	
	@RequestMapping(path = "/getsubemployee",method = RequestMethod.GET)
	public ResponseEntity<List<SubEmployeeProjection>> getSubEmployeeProjection(@RequestParam String company){
		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_XML);
		List<SubEmployeeProjection> subemployee=employeeRepository.findByCompanyIgnoreCase(company);
		ResponseEntity<List<SubEmployeeProjection>> response=new ResponseEntity<List<SubEmployeeProjection>>(subemployee, headers, HttpStatus.OK);
		return response;
	}
	@RequestMapping(path = "/dynamicProjection")
	public ResponseEntity<List<Employee>> dynamicProjection(@RequestParam String name){
		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_XML);
		List<Employee> subemployee=employeeRepository.findBynameIgnoreCase(name, Employee.class);
		ResponseEntity<List<Employee>> response=new ResponseEntity<>(subemployee, headers, HttpStatus.OK);
		return response;
	}
	@RequestMapping(path = "/querybyexample")
	public ResponseEntity<List<Employee>> findbyExample(){
		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_XML);
		Employee employee=new Employee();
		employee.setCompany("tcs");
		employee.setSalary(99000);
		ExampleMatcher matcher=ExampleMatcher.matching()
				                             .withIgnorePaths("salary");
		Example<Employee> example=Example.of(employee,matcher);
//		Example<Employee> example=Example.of(employee);
		List<Employee> emps=employeeRepository.findAll(example);
		ResponseEntity<List<Employee>> response=new ResponseEntity<List<Employee>>(emps, headers, HttpStatus.OK);
		return response;
	}
	
	

}
