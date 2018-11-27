/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.predicate;

import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.lang.ReduceType.FAILURE;
import static io.doov.core.dsl.lang.ReduceType.SUCCESS;
import static io.doov.core.dsl.meta.predicate.ReduceAnyMatchTest.EnumTest.VAL1;
import static io.doov.core.dsl.meta.predicate.ReduceAnyMatchTest.EnumTest.VAL2;
import static io.doov.core.dsl.meta.predicate.ReduceAnyMatchTest.EnumTest.VAL3;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.junit.jupiter.api.*;

import io.doov.core.dsl.field.types.EnumFieldInfo;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.runtime.GenericModel;

public class ReduceAnyMatchTest {
    private static final Locale LOCALE = Locale.FRANCE;
    private Result result;
    private Metadata reduce;
    private GenericModel model;
    private EnumFieldInfo<EnumTest> enumField;

    @BeforeEach
    void beforeEach() {
        this.model = new GenericModel();
        this.enumField = model.enumField(VAL1, "enumField");
    }

    @Test
    void anyMatch_success() {
        result = when(enumField.anyMatch(VAL1, VAL2, VAL3)).validate().executeOn(model);
        reduce = result.reduce(SUCCESS);

        assertTrue(result.value());
        assertThat(reduce).isInstanceOf(LeafPredicateMetadata.class)
                .extracting(m -> m.readable(LOCALE))
                .isEqualTo(new String[] { "enumField = VAL1" });
    }

    @Test
    void anyMatch_failure() {
        result = when(enumField.anyMatch(VAL2, VAL3)).validate().executeOn(model);
        reduce = result.reduce(FAILURE);

        assertFalse(result.value());
        assertThat(reduce).isInstanceOf(LeafPredicateMetadata.class)
                .extracting(m -> m.readable(LOCALE))
                .isEqualTo(new String[] { "enumField != VAL1" });
    }

    @AfterEach
    void afterEach() {
        System.out.println("> " + reduce);
    }

    enum EnumTest {
        VAL1, VAL2, VAL3;
    }
}
