package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.Employee;
import com.example.demo.projections.SubEmployeeProjection;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String>,QueryByExampleExecutor<Employee>{
//                                            PagingAndSortingRepository<Employee, String> {
	List<Employee> findByNameAndCompanyAllIgnoreCase(String name,String Company);
	Page<Employee> findByCompanyContaining(String company,Pageable page);
	Optional<Employee> findByNameIgnoreCase(String name);
	
	@Query(value = "select e from Employee e")
	Page<Employee> findAll(Pageable page);
	
	@Query(value = "select * from employee where name=?1",nativeQuery = true)
	Page<Employee> findbyname(String name,Pageable page);
	
	@Query(value = "select * from employee where id like :id%",nativeQuery = true)
	List<Employee> findWhereIdLike(@Param("id") String id);
	@Modifying
	@Query(value = "update Employee e set e.company=:company where e.id like :id%")
	int updateCompanyById(@Param("id")String id,@Param("company")String company);
	
	List<SubEmployeeProjection> findByCompanyIgnoreCase(String company);
	
	<T> List<T> findBynameIgnoreCase(String name, Class<T> type);

}
