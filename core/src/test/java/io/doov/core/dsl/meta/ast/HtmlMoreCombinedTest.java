/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.assertions.renderer.Assertions.assertThat;
import static io.doov.core.dsl.DOOV.matchAny;
import static io.doov.core.dsl.DOOV.sum;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.documentOf;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.format;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.file.AccessMode;
import java.time.LocalDate;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.field.types.BooleanFieldInfo;
import io.doov.core.dsl.field.types.EnumFieldInfo;
import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.field.types.LocalDateFieldInfo;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.runtime.GenericModel;

public class HtmlMoreCombinedTest {
    private Result result;
    private Document doc;
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
    void or_and_sum() {
        result = when((dateField1.ageAt(dateField2).greaterOrEquals(0)
                .or(dateField2.ageAt(dateField1).greaterOrEquals(0)))
                        .and(sum(zeroField, zeroField).lesserThan(0)))
                                .validate().withShortCircuit(false).executeOn(model);
        doc = documentOf(result);

        assertFalse(result.value());
        assertThat(doc).nary_OL().hasSize(1);
        assertThat(doc).binary_LI().hasSize(1);
        assertThat(doc).nary_LI().isEmpty();
        assertThat(doc).leaf_LI().hasSize(2);
        assertThat(doc).when_UL().isEmpty();
        assertThat(doc).binary_UL().isEmpty();
        assertThat(doc).binaryChild_UL().isEmpty();
        assertThat(doc).unary_UL().isEmpty();
        assertThat(doc).percentageValue_DIV().containsExactly("100 %", "100 %", "0 %");
        assertThat(doc).tokenOperator_SPAN().containsExactly("age at", ">=", "or", "age at", ">=", "and", "<");
        assertThat(doc).tokenValue_SPAN().containsExactly("0", "0", "0");
        assertThat(doc).tokenNary_SPAN().containsExactly("sum");

    }

    @Test
    void and_and_and_match_any_and_and() {
        result = when(enumField.eq(AccessMode.WRITE)
                .and(booleanField1.isFalse())
                .and(matchAny(booleanField1.isTrue(),
                        booleanField2.not()
                                .and(zeroField.between(0, 1))))
                .and(zeroField.eq(1)))
                        .validate().withShortCircuit(false).executeOn(model);
        doc = documentOf(result);

        assertFalse(result.value());
        assertThat(doc).nary_OL().hasSize(1);
        assertThat(doc).binary_LI().hasSize(3);
        assertThat(doc).nary_LI().isEmpty();
        assertThat(doc).leaf_LI().isEmpty();
        assertThat(doc).when_UL().isEmpty();
        assertThat(doc).binary_UL().isEmpty();
        assertThat(doc).binaryChild_UL().isEmpty();
        assertThat(doc).unary_UL().isEmpty();
        assertThat(doc).percentageValue_DIV()
                .containsExactly("0 %", "100 %", "100 %", "0 %", "100 %", "100 %", "100 %", "0 %");
        assertThat(doc).tokenOperator_SPAN()
                .containsExactly("=", "and", "is", "and", "is", "not", "and", ">=", "and", "<", "and", "=");
        assertThat(doc).tokenValue_SPAN().containsExactly("WRITE", "false", "true", "0", "1", "1");
        assertThat(doc).tokenNary_SPAN().containsExactly("match any");

    }

    @Test
    void or_and_and_and() {
        result = when(zeroField.isNull().or(zeroField.eq(0))
                .and(booleanField1.isFalse())
                .and(dateField1.ageAt(dateField2).lesserThan(0)
                        .and(dateField2.ageAt(dateField1).greaterOrEquals(0))))
                                .validate().withShortCircuit(false).executeOn(model);
        doc = documentOf(result);

        assertFalse(result.value());
        assertThat(doc).nary_OL().isEmpty();
        assertThat(doc).binary_LI().hasSize(1);
        assertThat(doc).nary_LI().isEmpty();
        assertThat(doc).leaf_LI().isEmpty();
        assertThat(doc).when_UL().isEmpty();
        assertThat(doc).binary_UL().isEmpty();
        assertThat(doc).binaryChild_UL().isEmpty();
        assertThat(doc).unary_UL().isEmpty();
        assertThat(doc).percentageValue_DIV().containsExactly("0 %", "100 %", "100 %", "0 %", "100 %");
        assertThat(doc).tokenOperator_SPAN()
                .containsExactly("is null", "or", "=", "and", "is", "and", "age at", "<", "and", "age at", ">=");
        assertThat(doc).tokenValue_SPAN().containsExactly("0", "false", "0", "0");
        assertThat(doc).tokenNary_SPAN().isEmpty();

    }

    @AfterEach
    void afterEach() {
        System.out.println(format(result, doc));
    }
}
