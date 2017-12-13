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
import static java.util.Arrays.asList;

import java.util.*;
import java.util.function.*;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.field.BaseFieldInfo;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.Metadata;

public class DefaultCondition<T> extends AbstractCondition<T> {

    public DefaultCondition(DslField field) {
        super(field);
    }

    public DefaultCondition(DslField field, Metadata metadata, BiFunction<DslModel, Context, Optional<T>> value) {
        super(field, metadata, value);
    }

    // null

    public final StepCondition isNull() {
        return new PredicateStepCondition<>(this.metadata.merge(nullMetadata(field)),
                (model, context) -> Optional.of(function.apply(model, context)),
                optional -> !optional.isPresent());
    }

    public final StepCondition isNotNull() {
        return new PredicateStepCondition<>(this.metadata.merge(notNullMetadata(field)),
                (model, context) -> Optional.of(function.apply(model, context)),
                Optional::isPresent);
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

    public final StepCondition eq(BaseFieldInfo<T> value) {
        return predicate(equalsMetadata(field, value),
                (model, context) -> valueModel(model, value),
                Object::equals);
    }

    public final StepCondition eq(DefaultCondition<T> value) {
        return predicate(equalsMetadata(field, value),
                value.function,
                Object::equals);
    }

    // not equals

    public final StepCondition notEq(T value) {
        return predicate(notEqualsMetadata(field, value),
                (model, context) -> Optional.ofNullable(value),
                (l, r) -> !l.equals(r));
    }

    public final StepCondition notEq(Supplier<T> value) {
        return predicate(notEqualsMetadata(field, value),
                (model, context) -> Optional.ofNullable(value.get()),
                (l, r) -> !l.equals(r));
    }

    public final StepCondition notEq(BaseFieldInfo<T> value) {
        return predicate(notEqualsMetadata(field, value),
                (model, context) -> valueModel(model, value),
                (l, r) -> !l.equals(r));
    }

    public final StepCondition notEq(DefaultCondition<T> value) {
        return predicate(notEqualsMetadata(field, value),
                value.function,
                (l, r) -> !l.equals(r));
    }

    // any match

    @SafeVarargs
    public final StepCondition anyMatch(T... values) {
        return predicate(matchAnyMetadata(field, asList(values)),
                value -> Arrays.stream(values).anyMatch(value::equals));
    }

    public final StepCondition anyMatch(Collection<T> values) {
        return predicate(matchAnyMetadata(field, values),
                value -> values.stream().anyMatch(value::equals));
    }

    public final StepCondition anyMatch(List<Predicate<T>> values) {
        return predicate(matchAnyMetadata(field),
                value -> values.stream().anyMatch(v -> v.test(value)));
    }

    // all match

    @SafeVarargs
    public final StepCondition allMatch(T... values) {
        return predicate(matchAllMetadata(field, asList(values)),
                value -> Arrays.stream(values).allMatch(value::equals));
    }

    public final StepCondition allMatch(Collection<T> values) {
        return predicate(matchAllMetadata(field, values),
                value -> values.stream().allMatch(value::equals));
    }

    public final StepCondition allMatch(List<Predicate<T>> values) {
        return predicate(matchAllMetadata(field),
                value -> values.stream().allMatch(v -> v.test(value)));
    }

    // none match

    @SafeVarargs
    public final StepCondition noneMatch(T... values) {
        return predicate(matchNoneMetadata(field, asList(values)),
                value -> Arrays.stream(values).noneMatch(value::equals));
    }

    public final StepCondition noneMatch(Collection<T> values) {
        return predicate(matchNoneMetadata(field, values),
                value -> values.stream().noneMatch(value::equals));
    }

    public final StepCondition noneMatch(List<Predicate<T>> values) {
        return predicate(matchNoneMetadata(field),
                value -> values.stream().noneMatch(v -> v.test(value)));
    }

    // map

    public final IntegerCondition mapToInt(Function<T, Integer> mapper) {
        return new IntegerCondition(field, mapToIntMetadata(field),
                (model, context) -> valueModel(model, field).flatMap(l -> Optional.of(mapper.apply(l))));
    }

}
