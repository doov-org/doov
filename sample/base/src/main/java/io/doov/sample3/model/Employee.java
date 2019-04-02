/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample3.model;

import static io.doov.sample3.field.EmployeeFieldId.AGE;
import static io.doov.sample3.field.EmployeeFieldId.COMPANY;
import static io.doov.sample3.field.EmployeeFieldId.COUNTRY;
import static io.doov.sample3.field.EmployeeFieldId.EMAIL;
import static io.doov.sample3.field.EmployeeFieldId.FULLNAME;

import io.doov.sample3.field.EmployeePath;

public class Employee {
    @EmployeePath(field = FULLNAME, readable = "employee.fullname")
    private String fullName;
    @EmployeePath(field = EMAIL, readable = "employee.email")
    private String email;
    @EmployeePath(field = AGE, readable = "employee.age")
    private int age;
    @EmployeePath(field = COUNTRY, readable = "employee.country")
    private String country;
    @EmployeePath(field = COMPANY, readable = "employee.company")
    private String company;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
