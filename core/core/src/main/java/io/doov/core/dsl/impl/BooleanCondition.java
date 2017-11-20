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
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.field.LogicalFieldInfo;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.FieldMetadata;

public class BooleanCondition extends DefaultCondition<Boolean> {

    public BooleanCondition(DslField field) {
        super(field);
    }

    public BooleanCondition(DslField field, FieldMetadata metadata,
                    BiFunction<DslModel, Context, Optional<Boolean>> value) {
        super(field, metadata, value);
    }

    // not

    public final StepCondition not() {
        return predicate(notMetadata(field), value -> !value);
    }

    // and

    public final StepCondition and(boolean value) {
        return predicate(andMetadata(field, value),
                        (model, context) -> Optional.of(value),
                        Boolean::logicalAnd);
    }

    public final StepCondition and(LogicalFieldInfo value) {
        return predicate(andMetadata(field, value),
                        (model, context) -> value(model, value),
                        Boolean::logicalAnd);
    }

    // or

    public final StepCondition or(boolean value) {
        return predicate(orMetadata(field, value),
                        (model, context) -> Optional.of(value),
                        Boolean::logicalOr);
    }

    public final StepCondition or(LogicalFieldInfo value) {
        return predicate(orMetadata(field, value),
                        (model, context) -> value(model, value),
                        Boolean::logicalOr);
    }

    // xor

    public final StepCondition xor(boolean value) {
        return predicate(xorMetadata(field, value),
                        (model, context) -> Optional.of(value),
                        Boolean::logicalXor);
    }

    public final StepCondition xor(LogicalFieldInfo value) {
        return predicate(xorMetadata(field, value),
                        (model, context) -> value(model, value),
                        Boolean::logicalXor);
    }

    // true

    public final StepCondition isTrue() {
        return predicate(isMetadata(field, true),
                        (model, context) -> Optional.of(TRUE),
                        Boolean::equals);
    }

    // false

    public final StepCondition isFalse() {
        return predicate(isMetadata(field, false),
                        (model, context) -> Optional.of(FALSE),
                        Boolean::equals);
    }

}
