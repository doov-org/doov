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
package io.doov.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.field.types.IterableFieldInfo;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.MetadataVisitor;

public class ReductionTest {

    private StepCondition conditionTrue;
    private StepCondition conditionFalse;
    private StepCondition conditionFalse2;
    private IterableFieldInfo<String, List<String>> field;
    private IntegerFieldInfo zeroInt;

    @BeforeEach
    void setUp() {
        conditionTrue = MockConditions.trueCondition("TRUE");
        conditionFalse = MockConditions.falseCondition("FALSE");
        conditionFalse2 = MockConditions.falseCondition("FALSE2");
        zeroInt = MockConditions.intField(0, "readable");
        field = MockConditions.iterableField(Arrays.asList("toto", "tata"), "list");
    }

    @Test
    void matchAll() {
        ValidationRule rule = DOOV.when(DOOV.matchAll(conditionFalse, conditionFalse2, conditionTrue)).validate();
        Result result = rule.withShortCircuit(false).executeOn(null);
        assertThat(result.isFalse()).isTrue();
        System.out.println(result.getFailureCause());
        Metadata reducedFailureCause = result.getContext().getRootMetadata().reduce(result.getContext(), ReduceType.FAILURE);
        System.out.println(reducedFailureCause);
        assertThat(collectMetadata(reducedFailureCause)).contains(conditionFalse.metadata());
        assertThat(collectMetadata(reducedFailureCause)).contains(conditionFalse2.metadata());
    }

    @Test
    void and() {
        ValidationRule rule = DOOV.when(conditionTrue.and(conditionFalse)).validate();
        Result result = rule.withShortCircuit(false).executeOn(MockConditions.model());
        assertThat(result.isFalse()).isTrue();
        Metadata reducedFailureCause = result.getContext().getRootMetadata().reduce(result.getContext(), ReduceType.FAILURE);
        System.out.println(reducedFailureCause);
        assertThat(collectMetadata(reducedFailureCause)).contains(conditionFalse.metadata());
    }

    @Test
    void zeroInt() {
        ValidationRule rule = DOOV.when(zeroInt.notEq(0)).validate();
        Result result = rule.withShortCircuit(false).executeOn(MockConditions.model());
        assertThat(result.isFalse()).isTrue();
        Metadata reducedFailureCause = result.getContext().getRootMetadata().reduce(result.getContext(), ReduceType.FAILURE);
        System.out.println(reducedFailureCause);
    }

    @Test
    void list() {
        ValidationRule rule = DOOV.when(field.contains("titi")).validate();
        Result result = rule.withShortCircuit(false).executeOn(MockConditions.model());
        assertThat(result.isFalse()).isTrue();
        System.out.println(result.getFailureCause());
    }

    private Set<Metadata> collectMetadata(Metadata root) {
        HashSet<Metadata> metadatas = new HashSet<>();
        MetadataVisitor visitor = new MetadataVisitor() {
            @Override
            public void start(Metadata metadata, int depth) {
                metadatas.add(metadata);
            }

            @Override
            public void beforeChild(Metadata metadata, Metadata child, int depth) {

            }

            @Override
            public void afterChild(Metadata metadata, Metadata child, boolean hasNext, int depth) {

            }

            @Override
            public void end(Metadata metadata, int depth) {

            }
        };
        visitor.browse(root, 0);
        return metadatas;
    }

}
