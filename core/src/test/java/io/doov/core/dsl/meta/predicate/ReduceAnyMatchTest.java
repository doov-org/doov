/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.predicate;

import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.lang.ReduceType.FAILURE;
import static io.doov.core.dsl.meta.predicate.ReduceAnyMatchTest.EnumTest.VAL1;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.*;

import io.doov.core.dsl.field.types.EnumFieldInfo;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.runtime.GenericModel;

public class ReduceAnyMatchTest {
    private Result result;
    private Metadata reduce;
    private GenericModel model;
    private EnumFieldInfo<EnumTest> enumField;

    @BeforeEach
    void beforeEach() {
        this.model = new GenericModel();
        this.enumField = model.enumField(VAL1, "val1");
    }

    @Test
    void anyMatch_failure() {
        System.out.println(model.<EnumTest> get(enumField.id()));
        result = when(enumField.anyMatch(EnumTest.VAL1, EnumTest.VAL2)).validate().executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
    }

    @AfterEach
    void afterEach() {
        System.out.println("> " + reduce);
    }

    enum EnumTest {
        VAL1, VAL2;
    }
}
