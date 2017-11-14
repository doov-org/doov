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
package io.doov.core.dsl.impl;

import static io.doov.core.dsl.meta.FieldMetadata.*;

import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.FieldMetadata;

public class StringCondition extends DefaultCondition<String> {

    public StringCondition(DslField field) {
        super(field);
    }

    public StringCondition(FieldMetadata metadata, BiFunction<DslModel, Context, Optional<String>> value) {
        super(metadata, value);
    }

    public final StepCondition contains(String regex) {
        return predicate(containsMetadata(field, regex),
                        (model, context) -> Optional.ofNullable(regex),
                        String::contains);
    }

    public final StepCondition matches(String regex) {
        return predicate(matchesMetadata(field, regex),
                        (model, context) -> Optional.ofNullable(regex),
                        String::matches);
    }

    public final StepCondition startsWith(String value) {
        return predicate(startsWithMetadata(field, value),
                        (model, context) -> Optional.ofNullable(value),
                        String::startsWith);
    }

    public final StepCondition endsWith(String value) {
        return predicate(endsWithMetadata(field, value),
                        (model, context) -> Optional.ofNullable(value),
                        String::endsWith);
    }

    public IntegerCondition length() {
        return new IntegerCondition(
                        lengthIsMetadata(field),
                        (model, context) -> Optional.ofNullable(model.<String> get(field.id())).map(String::length));
    }

    public IntegerCondition parseInt() {
        return new IntegerCondition(
                        fieldOnlyMetadata(field),
                        (model, context) -> Optional.ofNullable(model.<String> get(field.id())).map(Integer::parseInt));
    }

}
