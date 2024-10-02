package com.example.demo.Entity;


import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@XmlRootElement
@Entity(name = "Employee")
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
	
	@Id
	@Column(name = "id")
	String id;
	@Column(name = "name")
	String name;
	@Column(name = "company")
	String company;
	@Column(name = "location")
	String location;
	@Column(name = "salary")
	double salary;
	

}
