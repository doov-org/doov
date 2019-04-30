/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.lang.ReduceType.FAILURE;
import static io.doov.core.dsl.lang.ReduceType.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.doov.core.TagId;
import io.doov.core.dsl.field.types.BooleanFieldInfo;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.runtime.GenericModel;

class FieldConditionTest {

    private static Locale LOCALE = Locale.US;
    private ValidationRule rule;
    private GenericModel model = new GenericModel();
    private BooleanFieldInfo A = model.booleanField(true, "A");
    private Result result;
    private Metadata reduce;

    @Test
    void hasTag() {
        rule = when(A.hasTag(CustomTag.CUSTOM)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A tags contains CUSTOM validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A tags contains CUSTOM");
    }

    @Test
    void emptyTags() {
        rule = when(A.tags().isEmpty()).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(SUCCESS);

        assertTrue(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A tags is empty validate");
        assertThat(result.reduceMessage(LOCALE, SUCCESS)).isEqualTo("A tags is empty");
    }

    @Test
    void position() {
        rule = when(A.position().eq(0)).validate();
        result = rule.executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(rule.readable(LOCALE)).isEqualTo("rule when A position = 0 validate");
        assertThat(result.getFailureCause(LOCALE)).isEqualTo("A position = 0");
    }

    @AfterEach
    void afterEach() {
        System.out.println(rule + " -> " + reduce);
    }

    enum CustomTag implements TagId {CUSTOM}
}
