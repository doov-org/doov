/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.DOOV.matchAny;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.meta.ast.ToStringAnyMatchTest.EnumTest.VAL1;
import static io.doov.core.dsl.meta.ast.ToStringAnyMatchTest.EnumTest.VAL2;
import static io.doov.core.dsl.meta.ast.ToStringAnyMatchTest.EnumTest.VAL3;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.*;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.field.types.EnumFieldInfo;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.runtime.GenericModel;

public class ToStringAnyMatchTest {
    private static final Locale LOCALE = Locale.US;
    private StepCondition A;
    private ValidationRule rule;
    private GenericModel model;
    private EnumFieldInfo<EnumTest> enumField;

    @BeforeEach
    void beforeEach() {
        this.model = new GenericModel();
        this.enumField = model.enumField(VAL1, "enumField");
    }

    @Test
    void anyMatch_success() {
        rule = when(enumField.anyMatch(VAL1, VAL2, VAL3)).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when enumField match any  : VAL1, VAL2, VAL3 validate");

    }

    @Test
    void anyMatch_failure() {
        rule = when(enumField.anyMatch(VAL2, VAL3)).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when enumField match any  : VAL2, VAL3 validate");
    }

    @Test
    @Disabled
    // FIXME
    void and_combined_anyMatch_success() {
        A = DOOV.alwaysTrue("A");
        rule = when(A.and(enumField.anyMatch(VAL1, VAL2, VAL3))).validate();
        assertThat(rule.readable(LOCALE))
                .isEqualTo("rule when (always true A and enumField match any  : VAL1, VAL2, VAL3) validate");
    }

    @Test
    @Disabled
    // FIXME
    void and_combined_anyMatch_failure() {
        A = DOOV.alwaysTrue("A");
        rule = when(A.and(enumField.anyMatch(VAL2, VAL3))).validate();
        assertThat(rule.readable(LOCALE))
                .isEqualTo("rule when (always true A and enumField match any  : VAL2, VAL3) validate");
    }

    @Test
    @Disabled
    // FIXME
    void matchAny_combined_anyMatch_success() {
        A = DOOV.alwaysTrue("A");
        rule = when(matchAny(A, enumField.anyMatch(VAL1, VAL2, VAL3))).validate();
        assertThat(rule.readable(LOCALE))
                .isEqualTo("rule when match any [always true A, enumField match any  : VAL1, VAL2, VAL3] validate");
    }

    @Test
    @Disabled
    // FIXME
    void matchAny_combined_anyMatch_failure() {
        A = DOOV.alwaysFalse("A");
        rule = when(matchAny(A, enumField.anyMatch(VAL2, VAL3))).validate();
        assertThat(rule.readable(LOCALE))
                .isEqualTo("rule when match any [always false A, enumField match any  : VAL2, VAL3] validate");
    }

    @AfterEach
    void afterEach() {
        System.out.println(rule);
    }

    enum EnumTest {
        VAL1, VAL2, VAL3;
    }
}
