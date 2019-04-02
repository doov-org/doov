/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.DOOV.map;
import static io.doov.core.dsl.DOOV.template;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.documentOf;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.format;
import static io.doov.core.dsl.template.ParameterTypes.$String;
import static org.assertj.core.api.Assertions.assertThat;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.field.types.StringFieldInfo;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.runtime.GenericModel;
import io.doov.core.dsl.template.TemplateMapping.Map2;
import io.doov.core.dsl.template.TemplateRule.Rule1;
import io.doov.core.dsl.template.TemplateRule.Rule2;

public class HtmlTemplateTest {
    GenericModel model = new GenericModel();
    Result result;
    Context context;
    Document doc;

    @Test
    void templateTest() {
        Rule1<StringFieldInfo> template = template($String).rule(site -> site.contains("google"));
        result = template.bind(model.stringField("a", "param1")).executeOn(model);
        doc = documentOf(result);
        assertThat(doc.select("div.percentage-value")).extracting(Element::text).containsExactly("0 %");
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);
        assertThat(doc.select("span.dsl-token-when")).hasSize(0);
        assertThat(doc.select("span.dsl-token-then")).hasSize(0);
        assertThat(doc.select("span.dsl-token-else")).hasSize(0);
        assertThat(doc.select("span.dsl-token-single-mapping")).hasSize(0);
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("|", "contains");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("'google'");
        assertThat(doc.select("span.dsl-token-nary")).extracting(Element::text).isEmpty();
    }

    @Test
    void twoParamsTest() {
        Rule2<StringFieldInfo, StringFieldInfo> template = template($String, $String)
                .rule((url, name) -> url.contains(name.mapToString(String::toLowerCase)));
        result = template.bind(model.stringField("a", "param1"), model.stringField("b", "param2"))
                .executeOn(model);
        doc = documentOf(result);
        assertThat(doc.select("div.percentage-value")).extracting(Element::text).containsExactly("0 %");
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);
        assertThat(doc.select("span.dsl-token-when")).hasSize(0);
        assertThat(doc.select("span.dsl-token-then")).hasSize(0);
        assertThat(doc.select("span.dsl-token-else")).hasSize(0);
        assertThat(doc.select("span.dsl-token-single-mapping")).hasSize(0);
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("|", "contains", "|", "as a string");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .isEmpty();
        assertThat(doc.select("span.dsl-token-nary")).extracting(Element::text).isEmpty();
    }

    @Test
    void mappingTemplateTest() {
        Map2<StringFieldInfo, StringFieldInfo> template = template($String, $String)
                .mapping((from, dest) -> map(from).to(dest));
        context = template.bind(model.stringField("a", "param1"), model.stringField(null, "param2"))
                .executeOn(model, model);
        doc = documentOf(context);
        assertThat(doc.select("div.percentage-value")).extracting(Element::text).isEmpty();
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);
        assertThat(doc.select("span.dsl-token-when")).hasSize(0);
        assertThat(doc.select("span.dsl-token-then")).hasSize(0);
        assertThat(doc.select("span.dsl-token-else")).hasSize(0);
        assertThat(doc.select("span.dsl-token-single-mapping")).hasSize(1);
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("map", "|", "to", "|");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .isEmpty();
        assertThat(doc.select("span.dsl-token-nary")).extracting(Element::text).isEmpty();
    }

    @AfterEach
    void afterEach() {
        System.out.println(result != null ? format(result, doc) : format(context, doc));
    }
}
