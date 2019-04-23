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
import static io.doov.sample.validation.ast.MarkdownSampleRulesTest.parse;
import static io.doov.sample.validation.ast.MarkdownSampleRulesTest.render;

import org.commonmark.node.Node;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class MarkdownEmployeeMappingTest {
    private Node node;

    @Test
    void FULLNAME_MAPPING() {
        node = parse(FULLNAME_MAPPING.metadata());
        assertThat(node).countBulletList().isEqualTo(2);
        assertThat(node).countListItem().isEqualTo(3);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(3);
        assertThat(node).textNodes().containsExactly("map user first name and user last name", "using 'combine names'",
                        "to employee full name");
    }

    @Test
    void EMAIL_MAPPING() {
        node = parse(EMAIL_MAPPING.metadata());
        assertThat(node).countBulletList().isEqualTo(5);
        assertThat(node).countListItem().isEqualTo(6);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(6);
        assertThat(node).textNodes().containsExactly("mappings", "when", "account accept.email is 'true'", "then",
                        "map account email", "to employee email");
    }

    @Test
    void AGE_MAPPING() {
        node = parse(AGE_MAPPING.metadata());
        assertThat(node).countBulletList().isEqualTo(2);
        assertThat(node).countListItem().isEqualTo(2);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(2);
        assertThat(node).textNodes().containsExactly("map user birthdate age at '2019-01-01'", "to employee age");
    }

    @Test
    void COUNTRY_MAPPING() {
        node = parse(COUNTRY_MAPPING.metadata());
        assertThat(node).countBulletList().isEqualTo(2);
        assertThat(node).countListItem().isEqualTo(3);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(3);
        assertThat(node).textNodes().containsExactly("map account country", "using 'country name'",
                        "to employee country");

    }

    @Test
    void COMPANY_MAPPING() {
        node = parse(COMPANY_MAPPING.metadata());
        assertThat(node).countBulletList().isEqualTo(2);
        assertThat(node).countListItem().isEqualTo(3);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(3);
        assertThat(node).textNodes().containsExactly("map account company", "using 'company name'",
                        "to employee company");
    }

    @Test
    void ALL_MAPPINGS() {
        node = parse(ALL_MAPPINGS.metadata());
        assertThat(node).countBulletList().isEqualTo(10);
        assertThat(node).countListItem().isEqualTo(18);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(18);
        assertThat(node).textNodes().containsExactly("mappings", "mappings", "when", "account accept.email is 'true'",
                        "then", "map account email", "to employee email",
                        "map user first name and user last name", "using 'combine names'", "to employee full name",
                        "map user birthdate age at '2019-01-01'", "to employee age", "map account country",
                        "using 'country name'", "to employee country", "map account company", "using 'company name'",
                        "to employee company");
    }

    @AfterEach
    void afterEach() {
        System.out.println(render(node));
    }
}
