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
import static io.doov.core.dsl.DOOV.sum;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.meta.ast.MarkdownAndTest.parse;
import static io.doov.core.dsl.meta.ast.MarkdownAndTest.render;

import org.commonmark.node.Node;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.runtime.GenericModel;

public class MarkdownSumTest {
    private final GenericModel model = new GenericModel();
    private IntegerFieldInfo A, B;
    private ValidationRule rule;
    private Node node;

    @Test
    void sum_1_2_greaterThan_1() {
        A = model.intField(1, "A");
        B = model.intField(1, "B");
        rule = when(sum(A, B).greaterThan(1)).validate();
        node = parse(rule.metadata());
        assertThat(node).countBulletList().isEqualTo(4);
        assertThat(node).countListItem().isEqualTo(7);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(7);
        assertThat(node).textNodes().containsExactly("rule", "when", "sum", "A", "B >", "1", "validate");
    }

    @Test
    void sum_1_2_greaterThan_3() {
        A = model.intField(1, "A");
        B = model.intField(1, "B");
        rule = when(sum(A, B).greaterThan(3)).validate();
        node = parse(rule.metadata());
        assertThat(node).countBulletList().isEqualTo(4);
        assertThat(node).countListItem().isEqualTo(7);
        assertThat(node).countOrderedList().isEqualTo(0);
        assertThat(node).countText().isEqualTo(7);
        assertThat(node).textNodes().containsExactly("rule", "when", "sum", "A", "B >", "3", "validate");
    }

    @AfterEach
    void afterEach() {
        System.out.println(render(node));
    }
}
