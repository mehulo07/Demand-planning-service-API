package com.bns.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bns.exception.ResourceNotFoundException;
import com.bns.model.Employee;
import com.bns.service.EmployeeService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@RestController
@RequestMapping("/api")
public class EmployeeController {
	private final Logger log = LogManager.getLogger(this.getClass());
    @Autowired
    private EmployeeService employeeService;
    
    @GetMapping("/employees")
    @ApiOperation(value = "Get All Employee" ,
    notes = "Return all the Employee present in database",response = List.class)
    public List < Employee > getAllEmployees() {
        return employeeService.getAllEmployees();
    }
    

    @GetMapping("/employees/{id}")
    @ApiOperation(value = "Get Employee by id" , notes = "Get Specific employee by its id",response = Employee.class)
    public ResponseEntity < Employee > getEmployeeById(
    		@ApiParam(value = "Pass Employee ID ", required = true) @PathVariable(value = "id") Long employeeId)
    throws ResourceNotFoundException {
        Employee employee = employeeService.getEmployeeById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
        return ResponseEntity.ok().body(employee);
    }

    @PostMapping("/employees")
    public Employee createEmployee(@Valid @RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity < Employee > updateEmployee(@PathVariable(value = "id") Long employeeId,
        @Valid @RequestBody Employee employeeDetails) throws ResourceNotFoundException {
        Employee updatedEmployee = employeeService.updateEmployee(employeeId, employeeDetails);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/employees/{id}")
    public Map < String, Boolean > deleteEmployee(@PathVariable(value = "id") Long employeeId)
    throws ResourceNotFoundException {
        return employeeService.deleteEmployee(employeeId);
    }
}