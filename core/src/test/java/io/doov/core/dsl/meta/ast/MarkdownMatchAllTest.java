/*
 * Copyright 2018 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.assertions.renderer.Assertions.assertThat;
import static io.doov.core.dsl.DOOV.alwaysFalse;
import static io.doov.core.dsl.DOOV.alwaysTrue;
import static io.doov.core.dsl.DOOV.matchAll;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.meta.ast.MarkdownAndTest.parse;
import static io.doov.core.dsl.meta.ast.MarkdownAndTest.render;

import java.time.LocalDate;

import org.commonmark.node.Node;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.field.types.LocalDateFieldInfo;
import io.doov.core.dsl.field.types.StringFieldInfo;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.runtime.GenericModel;
import io.doov.core.dsl.time.LocalDateSuppliers;

public class MarkdownMatchAllTest {
    private StepCondition A, B, C;
    private ValidationRule rule;
    private Node node;

    @Test
    void matchAll_true_true_true() {
        A = alwaysTrue("A");
        B = alwaysTrue("B");
        C = alwaysTrue("C");
        rule = when(matchAll(A, B, C)).validate();
        node = parse(rule.metadata());
        assertThat(node).countBulletList().isEqualTo(4);
        assertThat(node).countListItem().isEqualTo(7);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(7);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "match all",
                "always true 'A'",
                "always true 'B'",
                "always true 'C'",
                "validate");
    }

    @Test
    void matchAll_true_true_false() {
        A = alwaysTrue("A");
        B = alwaysTrue("B");
        C = alwaysFalse("C");
        rule = when(matchAll(A, B, C)).validate();
        node = parse(rule.metadata());
        assertThat(node).countBulletList().isEqualTo(4);
        assertThat(node).countListItem().isEqualTo(7);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(7);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "match all",
                "always true 'A'",
                "always true 'B'",
                "always false 'C'",
                "validate");
    }

    @Test
    void matchAll_true_false_false() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        C = alwaysFalse("C");
        rule = when(matchAll(A, B, C)).validate();
        node = parse(rule.metadata());
        assertThat(node).countBulletList().isEqualTo(4);
        assertThat(node).countListItem().isEqualTo(7);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(7);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "match all",
                "always true 'A'",
                "always false 'B'",
                "always false 'C'",
                "validate");
    }

    @Test
    void matchAll_false_false_false() {
        A = alwaysFalse("A");
        B = alwaysFalse("B");
        C = alwaysFalse("C");
        rule = when(matchAll(A, B, C)).validate();
        node = parse(rule.metadata());
        assertThat(node).countBulletList().isEqualTo(4);
        assertThat(node).countListItem().isEqualTo(7);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(7);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "match all",
                "always false 'A'",
                "always false 'B'",
                "always false 'C'",
                "validate");
    }

    @Test
    void matchAll_field_false_false_false_failure() {
        GenericModel model = new GenericModel();
        IntegerFieldInfo zero = model.intField(0, "zero");
        LocalDateFieldInfo yesterday = model.localDateField(LocalDate.now().minusDays(1), "yesterday");
        StringFieldInfo something = model.stringField("something", "string field");
        A = zero.greaterThan(4);
        B = yesterday.after(LocalDateSuppliers.today());
        C = something.matches("^other.*");
        rule = when(matchAll(A, B, C)).validate();
        node = parse(rule.metadata());
        assertThat(node).countBulletList().isEqualTo(4);
        assertThat(node).countListItem().isEqualTo(7);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(7);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "match all",
                "zero > '4'",
                "yesterday after today",
                "string field matches '^other.*'",
                "validate");
    }

    @AfterEach
    void afterEach() {
        System.out.println(render(node));
    }
}
