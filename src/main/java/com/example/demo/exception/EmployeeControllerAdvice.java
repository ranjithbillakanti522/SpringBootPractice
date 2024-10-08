package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EmployeeControllerAdvice {
	
	@ExceptionHandler(value = NoItemFoundException.class)
	public ResponseEntity<String> noItemFoundHandler(Exception e) {
		ResponseEntity<String> re=new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		return re;
		
	}
	@ExceptionHandler(value = EmployeeAlreadyExistException.class)
	public ResponseEntity<String> employeeAlreadyExist(Exception e){
		ResponseEntity<String> response=new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_GATEWAY);
		return response;
	}

}
