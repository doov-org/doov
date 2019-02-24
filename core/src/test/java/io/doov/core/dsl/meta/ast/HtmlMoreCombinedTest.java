/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.DOOV.matchAny;
import static io.doov.core.dsl.DOOV.sum;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.documentOf;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.file.AccessMode;
import java.time.LocalDate;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.*;

import io.doov.core.dsl.field.types.*;
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
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-nary")).isEmpty();
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(2);
        assertThat(doc.select("ul.dsl-ul-when")).isEmpty();
        assertThat(doc.select("ul.dsl-ul-binary")).isEmpty();
        assertThat(doc.select("ul.dsl-ul-binary-child")).isEmpty();
        assertThat(doc.select("ul.dsl-ul-unary")).isEmpty();
        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("100 %", "100 %", "0 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("age at", ">=", "or", "age at", ">=", "and", "<");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("0", "0", "0");
        assertThat(doc.select("span.dsl-token-nary")).extracting(Element::text)
                .containsExactly("sum");

    }

    @Test
    @Disabled
    // FIXME
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
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(1);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(3);
        assertThat(doc.select("li.dsl-li-nary")).isEmpty();
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(1);
        assertThat(doc.select("ul.dsl-ul-when")).isEmpty();
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(1);
        assertThat(doc.select("ul.dsl-ul-binary-child")).isEmpty();
        assertThat(doc.select("ul.dsl-ul-unary")).isEmpty();
        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("0 %", "100 %", "100 %", "0 %", "100 %", "100 %", "100 %", "0 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("=", "is", "is", "not", ">=", "<", "=");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("WRITE", "false", "true", "0", "1", "1");
        assertThat(doc.select("span.dsl-token-nary")).extracting(Element::text)
                .containsExactly("match any");

    }

    @Test
    @Disabled
    // FIXME
    void or_and_and_and() {
        result = when(zeroField.isNull().or(zeroField.eq(0))
                .and(booleanField1.isFalse())
                .and(dateField1.ageAt(dateField2).lesserThan(0)
                        .and(dateField2.ageAt(dateField1).greaterOrEquals(0))))
                                .validate().withShortCircuit(false).executeOn(model);
        doc = documentOf(result);

        assertFalse(result.value());
        assertThat(doc.select("ol.dsl-ol-nary")).isEmpty();
        assertThat(doc.select("li.dsl-li-binary")).hasSize(2);
        assertThat(doc.select("li.dsl-li-nary")).isEmpty();
        assertThat(doc.select("li.dsl-li-leaf")).isEmpty();
        assertThat(doc.select("ul.dsl-ul-when")).isEmpty();
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(1);
        assertThat(doc.select("ul.dsl-ul-binary-child")).isEmpty();
        assertThat(doc.select("ul.dsl-ul-unary")).isEmpty();
        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("0 %", "100 %", "100 %", "0 %", "100 %");
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("is null", "=", "is", "age at", "<", "age at", ">=");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("0", "false", "0", "0");
        assertThat(doc.select("span.dsl-token-nary")).extracting(Element::text)
                .isEmpty();

    }

    @AfterEach
    void afterEach() {
        System.out.println(format(result, doc));
    }
}
