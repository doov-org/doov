/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation;

import static io.doov.assertions.Assertions.assertThat;
import static io.doov.core.dsl.DOOV.count;
import static io.doov.core.dsl.time.LocalDateSuppliers.today;
import static io.doov.sample.field.dsl.DslSampleModel.accountCountry;
import static io.doov.sample.field.dsl.DslSampleModel.accountPhoneNumber;
import static io.doov.sample.field.dsl.DslSampleModel.userBirthdate;

import java.time.LocalDate;

import org.junit.jupiter.api.*;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.sample.model.*;

/**
 * Validate that a profile match at least two conditions :
 * profile has at least 18 years<br>
 * coutry is France<br>
 * phone number start with '+33
 */
public class FailureCauseSample5Test {
    private final ValidationRule rule = DOOV
                    .when(count(userBirthdate.ageAt(today()).lesserThan(18),
                                    accountCountry.notEq(Country.FR),
                                    accountPhoneNumber.startsWith("+33").not()).lesserThan(2))
                    .validate();

    private final SampleModel beanModel = new SampleModel();
    private final DslModel wrapperModel = new SampleModelWrapper(beanModel);

    @BeforeEach
    public void plaintText() {
        System.out.print(rule.readable());
    }

    @AfterEach
    public void blankline() {
        System.out.println("");
    }
    
    @Test
    public void getFailureCause_setup_0() {
        Result result = rule.withShortCircuit(false).executeOn(wrapperModel);
        assertThat(result).isTrue();
        System.out.println("> " + result.getFailureCause());
    }

    @Test
    public void getFailureCause_setup_1() {
        beanModel.getUser().setBirthDate(LocalDate.now().minusYears(19));
        beanModel.getAccount().setCountry(Country.FR);
        beanModel.getAccount().setPhoneNumber("+33 1 23 45 67 89");
        
        Result result = rule.withShortCircuit(false).executeOn(wrapperModel);
        assertThat(result).isTrue();
        System.out.println("> " + result.getFailureCause());
    }
    
    @Test
    public void getFailureCause_setup_2() {
        beanModel.getUser().setBirthDate(LocalDate.now().minusYears(16));
        beanModel.getAccount().setCountry(Country.FR);
        beanModel.getAccount().setPhoneNumber("+33 1 23 45 67 89");
        
        Result result = rule.withShortCircuit(false).executeOn(wrapperModel);
        assertThat(result).isTrue();
        System.out.println("> " + result.getFailureCause());
    }

    @Test
    public void getFailureCause_setup_3() {
        beanModel.getUser().setBirthDate(LocalDate.now().minusYears(19));
        beanModel.getAccount().setCountry(Country.CAN);
        beanModel.getAccount().setPhoneNumber("1 23 45 67 89");
        
        Result result = rule.withShortCircuit(false).executeOn(wrapperModel);
        assertThat(result).isFalse();
        System.out.println("> " + result.getFailureCause());
    }
    
    @Test
    public void getFailureCause_setup_4() {
        beanModel.getUser().setBirthDate(LocalDate.now().minusYears(16));
        beanModel.getAccount().setCountry(Country.CAN);
        beanModel.getAccount().setPhoneNumber("1 23 45 67 89");
        
        Result result = rule.withShortCircuit(false).executeOn(wrapperModel);
        assertThat(result).isFalse();
        System.out.println("> " + result.getFailureCause());
    }
    
    @Test
    public void getFailureCause_setup_5() {
        beanModel.getUser().setBirthDate(LocalDate.now().minusYears(16));
        beanModel.getAccount().setCountry(Country.CAN);
        beanModel.getAccount().setPhoneNumber("+33 1 23 45 67 89");
        
        Result result = rule.withShortCircuit(false).executeOn(wrapperModel);
        assertThat(result).isFalse();
        System.out.println("> " + result.getFailureCause());
    }
}
