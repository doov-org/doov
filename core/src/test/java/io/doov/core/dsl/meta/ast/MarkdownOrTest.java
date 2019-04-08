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
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.meta.ast.MarkdownAndTest.parse;
import static io.doov.core.dsl.meta.ast.MarkdownAndTest.render;

import java.time.LocalDate;

import org.commonmark.node.Node;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.field.types.LocalDateFieldInfo;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.runtime.GenericModel;
import io.doov.core.dsl.time.LocalDateSuppliers;

public class MarkdownOrTest {
    private StepCondition A, B, C;
    private ValidationRule rule;
    private Node node;

    @Test
    void or_true_false_complex() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        C = alwaysTrue("C");
        rule = when(A.or(B.or(C))).validate();
        node = parse(rule.metadata());
        assertThat(node).countBulletList().isEqualTo(4);
        assertThat(node).countListItem().isEqualTo(6);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(6);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "always true A or", "always false B or", "always true C",
                "validate");
    }

    @Test
    void or_false_true_complex() {
        A = alwaysFalse("A");
        B = alwaysTrue("B");
        C = alwaysTrue("C");
        rule = when(A.or(B.and(C))).validate();
        node = parse(rule.metadata());
        assertThat(node).countBulletList().isEqualTo(4);
        assertThat(node).countListItem().isEqualTo(6);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(6);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "always false A or", "always true B and", "always true C",
                "validate");
    }

    @Test
    void or_false_false() {
        A = alwaysFalse("A");
        B = alwaysFalse("B");
        rule = when(A.or(B)).validate();
        node = parse(rule.metadata());
        assertThat(node).countBulletList().isEqualTo(3);
        assertThat(node).countListItem().isEqualTo(5);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(5);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "always false A or", "always false B",
                "validate");
    }

    @Test
    void or_false_false_complex() {
        A = alwaysFalse("A");
        B = alwaysFalse("B");
        C = alwaysTrue("C");
        rule = when(A.or(B.and(C))).validate();
        node = parse(rule.metadata());
        assertThat(node).countBulletList().isEqualTo(4);
        assertThat(node).countListItem().isEqualTo(6);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(6);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "always false A or", "always false B and", "always true C",
                "validate");
    }

    @Test
    void or_true_false() {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        rule = when(A.or(B)).validate();
        node = parse(rule.metadata());
        assertThat(node).countBulletList().isEqualTo(3);
        assertThat(node).countListItem().isEqualTo(5);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(5);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "always true A or", "always false B",
                "validate");
    }

    @Test
    void or_false_true() {
        A = alwaysFalse("A");
        B = alwaysTrue("B");
        rule = when(A.or(B)).validate();
        node = parse(rule.metadata());
        assertThat(node).countBulletList().isEqualTo(3);
        assertThat(node).countListItem().isEqualTo(5);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(5);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "always false A or", "always true B",
                "validate");
    }

    @Test
    void or_true_true() {
        A = alwaysTrue("A");
        B = alwaysTrue("B");
        rule = when(A.or(B)).validate();
        node = parse(rule.metadata());
        assertThat(node).countBulletList().isEqualTo(3);
        assertThat(node).countListItem().isEqualTo(5);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(5);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "always true A or", "always true B",
                "validate");
    }

    @Test
    void or_field_true_true() {
        GenericModel model = new GenericModel();
        IntegerFieldInfo zero = model.intField(0, "zero");
        LocalDateFieldInfo yesterday = model.localDateField(LocalDate.now().minusDays(1), "yesterday");
        A = zero.lesserThan(4);
        B = yesterday.before(LocalDateSuppliers.today());
        rule = when(A.or(B)).validate();
        node = parse(rule.metadata());
        assertThat(node).countBulletList().isEqualTo(3);
        assertThat(node).countListItem().isEqualTo(5);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(5);
        assertThat(node).textNodes().containsExactly("rule", "when",
                "zero < 4 or", "yesterday before today",
                "validate");
    }

    @AfterEach
    void afterEach() {
        System.out.println(render(node));
    }
}
