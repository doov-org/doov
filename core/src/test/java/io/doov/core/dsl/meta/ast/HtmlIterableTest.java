/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.assertions.renderer.Assertions.assertThat;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.documentOf;

import java.util.Arrays;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.field.types.IterableFieldInfo;
import io.doov.core.dsl.lang.MappingRule;
import io.doov.core.dsl.runtime.GenericModel;

class HtmlIterableTest {

    @Test
    void test_iterable() {

        List<String> content = Arrays.asList("1", "2", "3");

        GenericModel model = new GenericModel();
        IterableFieldInfo<String, List<String>> items = model.iterableField(content, "items");

        MappingRule rule = DOOV.mapIter(content).to(items);

        Document document = documentOf(rule.executeOn(model, model));

        assertThat(document).iterable_UL().extracting(Element::text).containsExactly("'1' '2' '3'");
        assertThat(document).iterable_UL()
                .extracting(e -> e.selectFirst("li").text()).containsExactly("'1'");
        assertThat(document).iterable_UL()
                .extracting(e -> e.select("li:nth-of-type(2)").text()).containsExactly("'2'");
        assertThat(document).iterable_UL()
                .extracting(e -> e.select("li:nth-of-type(3)").text()).containsExactly("'3'");
    }

    @Test
    void test_iterable_array() {

        GenericModel model = new GenericModel();
        IterableFieldInfo<String, List<String>> items = model.iterableField(null, "items");

        MappingRule rule = DOOV.mapIter("1", "2", "3").to(items);

        Document document = documentOf(rule.executeOn(model, model));

        assertThat(document).iterable_UL().extracting(Element::text).containsExactly("'1' '2' '3'");
        assertThat(document).iterable_UL()
                .extracting(e -> e.selectFirst("li").text()).containsExactly("'1'");
        assertThat(document).iterable_UL()
                .extracting(e -> e.select("li:nth-of-type(2)").text()).containsExactly("'2'");
        assertThat(document).iterable_UL()
                .extracting(e -> e.select("li:nth-of-type(3)").text()).containsExactly("'3'");
    }
}
