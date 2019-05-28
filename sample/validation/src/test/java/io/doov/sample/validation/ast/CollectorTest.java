/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation.ast;

import static io.doov.core.dsl.meta.DefaultOperator.and;
import static io.doov.core.dsl.meta.DefaultOperator.or;
import static io.doov.sample.validation.EmployeeMapping.EMAIL_MAPPING;
import static io.doov.sample.validation.EmployeeMapping.FULLNAME_MAPPING;
import static io.doov.sample.validation.SampleRules.RULE_ACCOUNT;
import static io.doov.sample.validation.SampleTemplateInstances.RULE_ACCOUNT_2;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import io.doov.core.dsl.meta.MetadataType;
import io.doov.core.dsl.meta.ReturnType;
import io.doov.core.dsl.meta.ast.AstVisitorUtils;

class CollectorTest {

    @Test
    void has_converter() {
        assertThat(AstVisitorUtils.collect(FULLNAME_MAPPING.metadata(), false,
                (b, m) -> b || m.type() == MetadataType.TYPE_CONVERTER)).isTrue();
    }

    @Test
    void has_not_converter() {
        assertThat(AstVisitorUtils.collect(EMAIL_MAPPING.metadata(), false,
                (b, m) -> b || m.type() == MetadataType.TYPE_CONVERTER)).isFalse();
    }
    
    @Test
    void collectIf_boolean_operators() {
        assertThat(AstVisitorUtils.collectIf(RULE_ACCOUNT.metadata(),
                m -> m.getOperator().returnType() == ReturnType.BOOLEAN).count())
                .isEqualTo(6L);
    }

    @Test
    void collectIf_boolean() {
        assertThat(AstVisitorUtils.collectIf(RULE_ACCOUNT_2.metadata(),
                m -> m.getOperator() == and || m.getOperator() == or).count())
                .isEqualTo(3L);
    }
}
