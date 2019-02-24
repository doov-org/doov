/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.DOOV.matchAny;
import static io.doov.core.dsl.DOOV.sum;
import static io.doov.core.dsl.DOOV.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.AccessMode;
import java.time.LocalDate;
import java.util.Locale;

import org.junit.jupiter.api.*;

import io.doov.core.dsl.field.types.*;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.runtime.GenericModel;

public class ToStringMoreCombinedTest {
    private static final Locale LOCALE = Locale.US;
    private ValidationRule rule;
    private GenericModel model;
    private LocalDateFieldInfo dateField1, dateField2;
    private BooleanFieldInfo booleanField1, booleanField2;
    private IntegerFieldInfo zeroField;
    private EnumFieldInfo<AccessMode> enumField;

    @BeforeEach
    void beforeEach() {
        this.model = new GenericModel();
        this.enumField = model.enumField(AccessMode.READ, "enum");
        this.dateField1 = model.localDateField(LocalDate.now(), "date 1");
        this.dateField2 = model.localDateField(LocalDate.now(), "date 2");
        this.booleanField1 = model.booleanField(false, "boolean 1");
        this.booleanField2 = model.booleanField(false, "boolean 2");
        this.zeroField = model.intField(0, "zero");
    }

    @Test
    @Disabled
    // FIXME
    void or_and_sum() {
        rule = when((dateField1.ageAt(dateField2).greaterOrEquals(0)
                .or(dateField2.ageAt(dateField1).greaterOrEquals(0)))
                        .and(sum(zeroField, zeroField).lesserThan(0))).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo(
                "rule when ((date 1 age at date 2 >= 0 or date 2 age at date 1 >= 0) and (sum [zero, zero] < 0)) validate");
    }

    @Test
    @Disabled
    // FIXME
    void and_and_and_match_any_and_and() {
        rule = when(enumField.eq(AccessMode.WRITE)
                .and(booleanField1.isFalse())
                .and(matchAny(booleanField1.isTrue(),
                        booleanField2.not()
                                .and(zeroField.between(0, 1))))
                .and(zeroField.eq(1))).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo(
                "rule when (((enum = WRITE and boolean 1 is false) and match any [boolean 1 is true, (boolean 2 not and (zero >= 0 and zero < 1))]) and zero = 1) validate");
    }

    @Test
    @Disabled
    // FIXME
    void or_and_and_and() {
        rule = when(zeroField.isNull().or(zeroField.eq(0))
                .and(booleanField1.isFalse())
                .and(dateField1.ageAt(dateField2).lesserThan(0)
                        .and(dateField2.ageAt(dateField1).greaterOrEquals(0)))).validate();
        assertThat(rule.readable(LOCALE)).isEqualTo(
                "rule when (((zero is null or zero = 0) and boolean 1 is false) and (date 1 age at date 2 < 0 and date 2 age at date 1 >= 0)) validate");
    }

    @AfterEach
    void afterEach() {
        System.out.println(rule);
    }
}
