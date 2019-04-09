/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.assertions.renderer.Assertions.assertThat;
import static io.doov.core.dsl.DOOV.map;
import static io.doov.core.dsl.DOOV.template;
import static io.doov.core.dsl.meta.ast.MarkdownAndTest.parse;
import static io.doov.core.dsl.meta.ast.MarkdownAndTest.render;
import static io.doov.core.dsl.template.ParameterTypes.$String;

import org.commonmark.node.Node;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.field.types.StringFieldInfo;
import io.doov.core.dsl.lang.MappingRule;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.runtime.GenericModel;
import io.doov.core.dsl.template.TemplateMapping.Map2;
import io.doov.core.dsl.template.TemplateRule.Rule1;
import io.doov.core.dsl.template.TemplateRule.Rule2;

public class MarkdownTemplateTest {
    GenericModel model = new GenericModel();
    Node node;

    @Test
    void templateTest() {
        Rule1<StringFieldInfo> template = template($String).rule(site -> site.contains("google"));
        ValidationRule rule = template.bind(model.stringField("a", "param1"));
        node = parse(rule.metadata());
        assertThat(node).countBulletList().isEqualTo(3);
        assertThat(node).countListItem().isEqualTo(4);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(4);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "{$String|param1} contains 'google'",
                "validate");
    }

    @Test
    void twoParamsTest() {
        Rule2<StringFieldInfo, StringFieldInfo> template = template($String, $String)
                .rule((url, name) -> url.contains(name.mapToString(String::toLowerCase)));
        ValidationRule rule = template.bind(model.stringField("a", "param1"), model.stringField("b", "param2"));
        node = parse(rule.metadata());
        assertThat(node).countBulletList().isEqualTo(3);
        assertThat(node).countListItem().isEqualTo(4);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(4);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "{$String|param1} contains {$String|param2} as a string -function-",
                "validate");
    }

    @Test
    void mappingTemplateTest() {
        Map2<StringFieldInfo, StringFieldInfo> template = template($String, $String)
                .mapping((from, dest) -> map(from).to(dest));
        MappingRule rule = template.bind(model.stringField("a", "param1"), model.stringField(null, "param2"));
        node = parse(rule.metadata());
        assertThat(node).countBulletList().isEqualTo(2);
        assertThat(node).countListItem().isEqualTo(2);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(2);
        assertThat(node).textNodes().containsExactly("map {$String|param1}", "to {$String|param2}");
    }

    @AfterEach
    void afterEach() {
        System.out.println(render(node));
    }
}
