/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.assertions.renderer.Assertions.assertThat;
import static io.doov.core.dsl.DOOV.map;
import static io.doov.core.dsl.DOOV.template;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.documentOf;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.format;
import static io.doov.core.dsl.template.ParameterTypes.$String;

import org.jsoup.nodes.Document;
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
        assertThat(doc).percentageValue_DIV().containsExactly("0 %");
        assertThat(doc).nary_OL().hasSize(0);
        assertThat(doc).binary_LI().hasSize(0);
        assertThat(doc).nary_LI().hasSize(0);
        assertThat(doc).leaf_LI().hasSize(0);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);
        assertThat(doc).tokenWhen_SPAN().hasSize(0);
        assertThat(doc).tokenThen_SPAN().hasSize(0);
        assertThat(doc).tokenElse_SPAN().hasSize(0);
        assertThat(doc).tokenSingleMapping_SPAN().hasSize(0);
        assertThat(doc).tokenOperator_SPAN().containsExactly("|", "contains");
        assertThat(doc).tokenValue_SPAN().containsExactly("'google'");
        assertThat(doc).tokenNary_SPAN().isEmpty();
    }

    @Test
    void twoParamsTest() {
        Rule2<StringFieldInfo, StringFieldInfo> template = template($String, $String)
                .rule((url, name) -> url.contains(name.mapToString(String::toLowerCase)));
        result = template.bind(model.stringField("a", "param1"), model.stringField("b", "param2"))
                .executeOn(model);
        doc = documentOf(result);
        assertThat(doc).percentageValue_DIV().containsExactly("0 %");
        assertThat(doc).nary_OL().hasSize(0);
        assertThat(doc).binary_LI().hasSize(0);
        assertThat(doc).nary_LI().hasSize(0);
        assertThat(doc).leaf_LI().hasSize(0);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);
        assertThat(doc).tokenWhen_SPAN().hasSize(0);
        assertThat(doc).tokenThen_SPAN().hasSize(0);
        assertThat(doc).tokenElse_SPAN().hasSize(0);
        assertThat(doc).tokenSingleMapping_SPAN().hasSize(0);
        assertThat(doc).tokenOperator_SPAN().containsExactly("|", "contains", "|", "as a string");
        assertThat(doc).tokenValue_SPAN().isEmpty();
        assertThat(doc).tokenNary_SPAN().isEmpty();
    }

    @Test
    void mappingTemplateTest() {
        Map2<StringFieldInfo, StringFieldInfo> template = template($String, $String)
                .mapping((from, dest) -> map(from).to(dest));
        context = template.bind(model.stringField("a", "param1"), model.stringField(null, "param2"))
                .executeOn(model, model);
        doc = documentOf(context);
        assertThat(doc).percentageValue_DIV().isEmpty();
        assertThat(doc).nary_OL().hasSize(0);
        assertThat(doc).binary_LI().hasSize(0);
        assertThat(doc).nary_LI().hasSize(0);
        assertThat(doc).leaf_LI().hasSize(0);
        assertThat(doc).when_UL().hasSize(0);
        assertThat(doc).binary_UL().hasSize(0);
        assertThat(doc).binaryChild_UL().hasSize(0);
        assertThat(doc).unary_UL().hasSize(0);
        assertThat(doc).tokenWhen_SPAN().hasSize(0);
        assertThat(doc).tokenThen_SPAN().hasSize(0);
        assertThat(doc).tokenElse_SPAN().hasSize(0);
        assertThat(doc).tokenSingleMapping_SPAN().hasSize(1);
        assertThat(doc).tokenOperator_SPAN().containsExactly("map", "|", "to", "|");
        assertThat(doc).tokenValue_SPAN().isEmpty();
        assertThat(doc).tokenNary_SPAN().isEmpty();
    }

    @AfterEach
    void afterEach() {
        System.out.println(result != null ? format(result, doc) : format(context, doc));
    }
}
