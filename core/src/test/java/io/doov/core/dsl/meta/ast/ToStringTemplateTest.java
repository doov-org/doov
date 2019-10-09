/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.DOOV.map;
import static io.doov.core.dsl.DOOV.template;
import static io.doov.core.dsl.template.ParameterTypes.$String;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import io.doov.core.dsl.field.types.StringFieldInfo;
import io.doov.core.dsl.runtime.GenericModel;
import io.doov.core.dsl.template.TemplateMapping.Map2;
import io.doov.core.dsl.template.TemplateRule.Rule1;
import io.doov.core.dsl.template.TemplateRule.Rule2;

public class ToStringTemplateTest {
    private static final Locale LOCALE = Locale.US;
    GenericModel model = new GenericModel();

    @Test
    void templateTest() {
        Rule1<StringFieldInfo> template = template($String).rule(site -> site.contains("google"));
        assertThat(template.readable(LOCALE)).isEqualTo("rule when {$String} contains 'google' validate");
    }

    @Test
    void twoParamsTest() {
        Rule2<StringFieldInfo, StringFieldInfo> template = template($String, $String)
                        .rule((url, name) -> url.contains(name.mapToString("lower case", String::toLowerCase)));
        assertThat(template.readable(LOCALE))
                        .isEqualTo("rule when {$String} contains {$String} as a string lower case validate");
    }

    @Test
    void mappingTemplateTest() {
        Map2<StringFieldInfo, StringFieldInfo> template = template($String, $String)
                        .mapping((from, dest) -> map(from).to(dest));
        assertThat(template.readable(LOCALE)).isEqualTo("map {$String} to {$String}");
    }

    @Test
    void bindTemplateTest() {
        Rule1<StringFieldInfo> template = template($String).rule(site -> site.contains("google"));
        assertThat(template.bind(model.stringField("a", "param1")).readable(LOCALE))
                        .isEqualTo("rule when {param1} contains 'google' validate");
    }

    @Test
    void bindTwoParamsTest() {
        Rule2<StringFieldInfo, StringFieldInfo> template = template($String, $String)
                        .rule((url, name) -> url.contains(name.mapToString("lower case", String::toLowerCase)));
        assertThat(template.bind(model.stringField("a", "param1"), model.stringField("b", "param2")).readable(LOCALE))
                        .isEqualTo("rule when {param1} contains {param2} as a string lower case validate");
    }

    @Test
    void bindMappingTemplateTest() {
        Map2<StringFieldInfo, StringFieldInfo> template = template($String, $String)
                        .mapping((from, dest) -> map(from).to(dest));
        assertThat(template.bind(model.stringField("a", "param1"), model.stringField(null, "param2")).readable(LOCALE))
                        .isEqualTo("map {param1} to {param2}");
    }

}
