/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.mapping;

import static io.doov.sample.validation.EmployeeMapping.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import io.doov.sample.model.SampleModels;
import io.doov.sample.wrapper.SampleModelWrapper;
import io.doov.sample3.model.Employee;
import io.doov.sample3.wrapper.EmployeeWrapper;

public class EmployeeMappingTest {
    Employee employee = new Employee();
    SampleModelWrapper IN = SampleModels.wrapper();
    EmployeeWrapper OUT = new EmployeeWrapper(employee);

    @Test
    public void testFullName() {
        FULLNAME_MAPPING.executeOn(IN, OUT);
        assertThat(employee.getFullName()).isEqualTo("Foo BAR");
    }

    @Test
    public void testEmail() {
        EMAIL_MAPPING.executeOn(IN, OUT);
        assertThat(employee.getEmail()).isEqualTo("foo@bar.com");
    }

    @Test
    public void testAge() {
        AGE_MAPPING.executeOn(IN, OUT);
        assertThat(employee.getAge()).isEqualTo(38);
    }

    @Test
    public void testCountry() {
        COUNTRY_MAPPING.executeOn(IN, OUT);
        assertThat(employee.getCountry()).isEqualTo("FR");
    }

    @Test
    public void testCompany() {
        COMPANY_MAPPING.executeOn(IN, OUT);
        assertThat(employee.getCompany()).isEqualTo("LES_FURETS");
    }
    
    @Test
    public void testAccountMapNull() {
        MAPPING_ACCOUNT_NULL.executeOn(IN, IN);
        assertThat(IN.getModel().getAccount().getCompany()).isNull();
        assertThat(IN.getModel().getAccount().getLanguage()).isNull();
        assertThat(IN.getModel().getAccount().getLogin()).isNull();
    }
}
