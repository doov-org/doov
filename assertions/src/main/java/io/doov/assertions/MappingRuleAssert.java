/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package io.doov.assertions;

import static io.doov.core.dsl.meta.ast.FieldCollector.collect;

import java.util.List;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.IterableAssert;

import io.doov.core.FieldId;
import io.doov.core.dsl.lang.MappingRule;
import io.doov.core.dsl.lang.ValidationRule;

/**
 * Assertion for {@link ValidationRule}.
 */
public class MappingRuleAssert extends AbstractAssert<MappingRuleAssert, MappingRule> {

    MappingRuleAssert(MappingRule rule, Class<?> selfType) {
        super(rule, selfType);
    }

    public MappingRuleAssert isUsing(FieldId... fieldIds) {
        final List<FieldId> fields = collect(actual.metadata());
        final IterableAssert<FieldId> iterableAssert = new IterableAssert<>(fields);
        iterableAssert.contains(fieldIds);
        return this;
    }

    public MappingRuleAssert isNotUsing(FieldId... fieldIds) {
        final List<FieldId> fields = collect(actual.metadata());
        final IterableAssert<FieldId> iterableAssert = new IterableAssert<>(fields);
        iterableAssert.doesNotContain(fieldIds);
        return this;
    }
}
