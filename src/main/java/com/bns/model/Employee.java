package com.bns.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "employees")
@ApiModel(description = "Details about employee.")
public class Employee {

	@ApiModelProperty(notes = "Employee id")
    private long employee_id;
	@ApiModelProperty(notes = "Employee first name")
    private String firstName;
	@ApiModelProperty(notes = "Employee last name")
    private String lastName;
	@ApiModelProperty(notes = "Employee email id")
    private String emailId;

    public Employee() {

    }

    public Employee(String firstName, String lastName, String emailId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "employee_id", nullable = false)
    public long getId() {
        return employee_id;
    }
    public void setId(long id) {
        this.employee_id = id;
    }

    @Column(name = "first_name", nullable = false)
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name", nullable = false)
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "email", nullable = false)
    public String getEmailId() {
        return emailId;
    }
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    @Override
    public String toString() {
        return "Employee [id=" + employee_id + ", firstName=" + firstName + ", lastName=" + lastName + ", emailId=" + emailId +
            "]";
    }
}