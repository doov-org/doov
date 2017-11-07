/*
 * Copyright 2017 Courtanet
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
package io.doov.core.dsl.impl;

import static io.doov.core.dsl.meta.FieldMetadata.*;

import java.util.*;
import java.util.function.*;

import io.doov.core.dsl.SimpleFieldId;
import io.doov.core.dsl.SimpleModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.FieldMetadata;

public class TypeCondition<T> extends DefaultCondition<T> {

    public TypeCondition(SimpleFieldId<T> field) {
        super(field);
    }

    public TypeCondition(FieldMetadata metadata, BiFunction<SimpleModel, Context, Optional<T>> value) {
        super(metadata, value);
    }

    // available

    public final StepCondition available() {
        return predicate(availableMetadata(field), value -> true);
    }

    public final StepCondition notAvailable() {
        return predicate(notAvailableMetadata(field), value -> false);
    }

    // equals

    public final StepCondition eq(T value) {
        return predicate(equalsMetadata(field, value),
                        (model, context) -> Optional.ofNullable(value),
                        Object::equals);
    }

    public final StepCondition eq(Supplier<T> value) {
        return predicate(equalsMetadata(field, value),
                        (model, context) -> Optional.ofNullable(value.get()),
                        Object::equals);
    }

    public final StepCondition eq(SimpleFieldId<T> value) {
        return predicate(equalsMetadata(field, value),
                        (model, context) -> value(model, value),
                        Object::equals);
    }

    public final StepCondition eq(FunctionStep<T, T> value) {
        return predicate(equalsMetadata(field, value),
                        (model, context) -> Optional.ofNullable(value.function.apply(model, context)),
                        Object::equals);
    }

    // not equals

    public final StepCondition notEq(T value) {
        return predicate(notEqualsMetadata(field, value),
                        (model, context) -> Optional.ofNullable(value),
                        (l, r) -> !l.equals(r));
    }

    public final StepCondition notEq(SimpleFieldId<T> value) {
        return predicate(notEqualsMetadata(field, value),
                        (model, context) -> value(model, value),
                        (l, r) -> !l.equals(r));
    }

    // null

    public final StepCondition isNull() {
        return predicate(nullMetadata(field, null), Objects::isNull);
    }

    // not null

    public final StepCondition isNotNull() {
        return predicate(notNullMetadata(field, null), obj -> !Objects.isNull(obj));
    }

    // match

    @SafeVarargs
    public final StepCondition anyMatch(T... values) {
        return predicate(matchAnyMetadata(field, (Object[]) values),
                        value -> Arrays.stream(values).anyMatch(value::equals));
    }

    @SafeVarargs
    public final StepCondition anyMatch(Predicate<T>... values) {
        return predicate(matchAnyMetadata(field, (Object[]) values),
                        value -> Arrays.stream(values).anyMatch(v -> v.test(value)));
    }

    public final StepCondition anyMatch(Collection<T> values) {
        return predicate(matchAnyMetadata(field, values),
                        value -> values.stream().anyMatch(value::equals));
    }

    @SafeVarargs
    public final StepCondition allMatch(T... values) {
        return predicate(matchAllMetadata(field, (Object[]) values),
                        value -> Arrays.stream(values).allMatch(value::equals));
    }

    @SafeVarargs
    public final StepCondition allMatch(Predicate<T>... values) {
        return predicate(matchAllMetadata(field, (Object[]) values),
                        value -> Arrays.stream(values).allMatch(v -> v.test(value)));
    }

    public final StepCondition allMatch(Collection<T> values) {
        return predicate(matchAllMetadata(field, values),
                        value -> values.stream().allMatch(value::equals));
    }

    @SafeVarargs
    public final StepCondition noneMatch(T... values) {
        return predicate(matchNoneMetadata(field, (Object[]) values),
                        value -> Arrays.stream(values).noneMatch(value::equals));
    }

    @SafeVarargs
    public final StepCondition noneMatch(Predicate<T>... values) {
        return predicate(matchNoneMetadata(field, (Object[]) values),
                        value -> Arrays.stream(values).noneMatch(v -> v.test(value)));
    }

    public final StepCondition noneMatch(Collection<T> values) {
        return predicate(matchNoneMetadata(field, values),
                        value -> values.stream().noneMatch(value::equals));
    }

}
