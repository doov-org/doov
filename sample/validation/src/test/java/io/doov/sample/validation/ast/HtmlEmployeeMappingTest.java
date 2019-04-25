/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation.ast;

import static io.doov.assertions.renderer.Assertions.assertThat;
import static io.doov.sample.validation.EmployeeMapping.AGE_MAPPING;
import static io.doov.sample.validation.EmployeeMapping.ALL_MAPPINGS;
import static io.doov.sample.validation.EmployeeMapping.COMPANY_MAPPING;
import static io.doov.sample.validation.EmployeeMapping.COUNTRY_MAPPING;
import static io.doov.sample.validation.EmployeeMapping.EMAIL_MAPPING;
import static io.doov.sample.validation.EmployeeMapping.FULLNAME_MAPPING;
import static io.doov.sample.validation.ast.HtmlSampleRulesTest.documentOf;
import static io.doov.sample.validation.ast.HtmlSampleRulesTest.format;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.Context;
import io.doov.sample.model.SampleModels;
import io.doov.sample3.model.Employee;
import io.doov.sample3.wrapper.EmployeeWrapper;

public class HtmlEmployeeMappingTest {
    private final FieldModel inModel = SampleModels.wrapper();
    private final FieldModel outModel = new EmployeeWrapper(new Employee());
    private Context context;
    private Document doc;

    @Test
    void FULLNAME_MAPPING() {
        context = FULLNAME_MAPPING.executeOn(inModel, outModel);
        doc = documentOf(context);

        assertThat(doc).nary_OL().hasSize(0);
        assertThat(doc).binary_LI().hasSize(0);
        assertThat(doc).nary_LI().hasSize(0);
        assertThat(doc).leaf_LI().hasSize(0);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);

        assertThat(doc).percentageValue_DIV().isEmpty();
        assertThat(doc).tokenOperator_SPAN().containsExactly("map", "and", "using", "to");
        assertThat(doc).tokenValue_SPAN().containsExactly("'combine names'");
        assertThat(doc).tokenField_SPAN().containsExactly("user first name", "user last name", "employee full name");
        assertThat(doc).tokenNary_SPAN().isEmpty();
    }

    @Test
    void EMAIL_MAPPING() {
        context = EMAIL_MAPPING.executeOn(inModel, outModel);
        doc = documentOf(context);

        assertThat(doc).nary_OL().hasSize(0);
        assertThat(doc).binary_LI().hasSize(0);
        assertThat(doc).nary_LI().hasSize(0);
        assertThat(doc).leaf_LI().hasSize(0);
        assertThat(doc).when_UL().hasSize(1);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);

        assertThat(doc).percentageValue_DIV().containsExactly("100 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly("is", "map", "to");
        assertThat(doc).tokenValue_SPAN().containsExactly("true");
        assertThat(doc).tokenField_SPAN().containsExactly("account accept email", "account email", "employee email");
        assertThat(doc).tokenNary_SPAN().isEmpty();
    }

    @Test
    void AGE_MAPPING() {
        context = AGE_MAPPING.executeOn(inModel, outModel);
        doc = documentOf(context);

        assertThat(doc).nary_OL().hasSize(0);
        assertThat(doc).binary_LI().hasSize(0);
        assertThat(doc).nary_LI().hasSize(0);
        assertThat(doc).leaf_LI().hasSize(0);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);

        assertThat(doc).percentageValue_DIV().isEmpty();
        assertThat(doc).tokenOperator_SPAN().containsExactly("map", "age at", "to");
        assertThat(doc).tokenValue_SPAN().containsExactly("2019-01-01");
        assertThat(doc).tokenField_SPAN().containsExactly("user birthdate", "employee age");
        assertThat(doc).tokenNary_SPAN().isEmpty();
    }

    @Test
    void COUNTRY_MAPPING() {
        context = COUNTRY_MAPPING.executeOn(inModel, outModel);
        doc = documentOf(context);

        assertThat(doc).nary_OL().hasSize(0);
        assertThat(doc).binary_LI().hasSize(0);
        assertThat(doc).nary_LI().hasSize(0);
        assertThat(doc).leaf_LI().hasSize(0);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);

        assertThat(doc).percentageValue_DIV().isEmpty();
        assertThat(doc).tokenOperator_SPAN().containsExactly("map", "using", "to");
        assertThat(doc).tokenValue_SPAN().containsExactly("'country name'");
        assertThat(doc).tokenField_SPAN().containsExactly("account country", "employee country");
        assertThat(doc).tokenNary_SPAN().isEmpty();
    }

    @Test
    void COMPANY_MAPPING() {
        context = COMPANY_MAPPING.executeOn(inModel, outModel);
        doc = documentOf(context);

        assertThat(doc).nary_OL().hasSize(0);
        assertThat(doc).binary_LI().hasSize(0);
        assertThat(doc).nary_LI().hasSize(0);
        assertThat(doc).leaf_LI().hasSize(0);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);

        assertThat(doc).percentageValue_DIV().isEmpty();
        assertThat(doc).tokenOperator_SPAN().containsExactly("map", "using", "to");
        assertThat(doc).tokenValue_SPAN().containsExactly("'company name'");
        assertThat(doc).tokenField_SPAN().containsExactly("account company", "employee company");
        assertThat(doc).tokenNary_SPAN().isEmpty();
    }

    @Test
    void ALL_MAPPINGS() {
        context = ALL_MAPPINGS.executeOn(inModel, outModel);
        doc = documentOf(context);

        assertThat(doc).nary_OL().hasSize(0);
        assertThat(doc).binary_LI().hasSize(0);
        assertThat(doc).nary_LI().hasSize(5);
        assertThat(doc).leaf_LI().hasSize(0);
        assertThat(doc).when_UL().hasSize(1);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);

        assertThat(doc).percentageValue_DIV().containsExactly("100 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly("is", "map", "to",
                        "map", "and", "using", "to",
                        "map", "age at", "to",
                        "map", "using", "to",
                        "map", "using", "to");
        assertThat(doc).tokenValue_SPAN().containsExactly("true", "'combine names'", "2019-01-01",
                        "'country name'", "'company name'");
        assertThat(doc).tokenField_SPAN().containsExactly("account accept email", "account email", "employee email",
                        "user first name", "user last name", "employee full name",
                        "user birthdate", "employee age",
                        "account country", "employee country",
                        "account company", "employee company");
        assertThat(doc).tokenNary_SPAN().isEmpty();
    }

    @AfterEach
    void afterEach() {
        System.out.println(format(context, doc));
    }
}
