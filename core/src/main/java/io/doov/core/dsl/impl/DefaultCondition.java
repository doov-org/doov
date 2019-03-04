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

import static io.doov.core.dsl.meta.function.MapFunctionMetadata.mapAsMetadata;
import static io.doov.core.dsl.meta.function.MapFunctionMetadata.mapToIntMetadata;
import static io.doov.core.dsl.meta.function.MapFunctionMetadata.mapToStringMetadata;
import static io.doov.core.dsl.meta.function.MapFunctionMetadata.mapUsingMetadata;
import static io.doov.core.dsl.meta.predicate.ValuePredicateMetadata.fieldMetadata;
import static java.util.Arrays.asList;

import java.util.*;
import java.util.function.*;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.field.BaseFieldInfo;
import io.doov.core.dsl.impl.num.IntegerFunction;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.predicate.BinaryPredicateMetadata;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

/**
 * Base class for all conditions.
 * <p>
 * It contains a {@link DslField} to get the value from the model, a {@link PredicateMetadata} to describe this node,
 * and a {@link BiFunction} to take the value from the model and return an optional value.
 *
 * @param <T> the type of the field value
 */
public class DefaultCondition<T> extends DefaultFunction<T, PredicateMetadata> {

    public DefaultCondition(DslField<T> field) {
        this(fieldMetadata(field), (model, context) -> valueModel(model, field));
    }

    public DefaultCondition(PredicateMetadata metadata, BiFunction<FieldModel, Context, Optional<T>> value) {
        super(metadata, value);
    }

    /**
     * Returns a step condition checking if the node value is null.
     *
     * @return the step condition
     */
    public final StepCondition isNull() {
        return LeafStepCondition.isNull(this);
    }

    /**
     * Returns a step condition checking if the node value is not null.
     *
     * @return the step condition
     */
    public final StepCondition isNotNull() {
        return LeafStepCondition.isNotNull(this);
    }

    /**
     * Returns a step condition checking if the node value is equal to the given value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition eq(T value) {
        return LeafStepCondition.stepCondition(BinaryPredicateMetadata.equalsMetadata(metadata, value), function, value,
                Object::equals);
    }

    /**
     * Returns a step condition checking if the node value is equal to the given field value.
     *
     * @param supplier the right side value
     * @return the step condition
     */
    public final StepCondition eq(Supplier<T> supplier) {
        return LeafStepCondition.stepCondition(BinaryPredicateMetadata.equalsMetadata(metadata, supplier), function, supplier,
                Object::equals);
    }

    /**
     * Returns a step condition checking if the node value is equal to the given supplier value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition eq(BaseFieldInfo<T> value) {
        return LeafStepCondition.stepCondition(BinaryPredicateMetadata.equalsMetadata(metadata, value), function, value,
                Object::equals);
    }

    /**
     * Returns a step condition checking if the node value is not equal to the given value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition notEq(T value) {
        return LeafStepCondition.stepCondition(BinaryPredicateMetadata.notEqualsMetadata(metadata, value), function, value,
                (l, r) -> !l.equals(r));
    }

    /**
     * Returns a step condition checking if the node value is not equal to the given supplier value.
     *
     * @param supplier the right side value
     * @return the step condition
     */
    public final StepCondition notEq(Supplier<T> supplier) {
        return LeafStepCondition.stepCondition(BinaryPredicateMetadata.notEqualsMetadata(metadata, supplier), function, supplier,
                (l, r) -> !l.equals(r));
    }

    /**
     * Returns a step condition checking if the node value is not equal to the given field value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition notEq(BaseFieldInfo<T> value) {
        return LeafStepCondition.stepCondition(BinaryPredicateMetadata.notEqualsMetadata(metadata, value), function, value,
                (l, r) -> !l.equals(r));
    }

    /**
     * Returns a step condition checking if the node value matches any of the given values.
     *
     * @param values the values to match
     * @return the step condition
     */
    public final StepCondition anyMatch(Collection<T> values) {
        return LeafStepCondition.stepCondition(BinaryPredicateMetadata.anyMatchMetadata(metadata, values), function,
                value -> values.stream().anyMatch(value::equals));
    }

    /**
     * Returns a step condition checking if any of the given predicates testing the node value match.
     *
     * @param values the values to match
     * @return the step condition
     */
    public final StepCondition anyMatch(List<Predicate<T>> values) {
        return LeafStepCondition.stepCondition(BinaryPredicateMetadata.anyMatchMetadata(metadata), function,
                value -> values.stream().anyMatch(v -> v.test(value)));
    }

    /**
     * Returns a step condition checking if the node value matches all of the given values.
     *
     * @param values the values to match
     * @return the step condition
     */
    @SafeVarargs
    public final StepCondition allMatch(T... values) {
        return LeafStepCondition.stepCondition(BinaryPredicateMetadata.allMatchMetadata(metadata, asList(values)), function,
                value -> Arrays.stream(values).allMatch(value::equals));
    }

    /**
     * Returns a step condition checking if the node value matches all of the given values.
     *
     * @param values the values to match
     * @return the step condition
     */
    public final StepCondition allMatch(Collection<T> values) {
        return LeafStepCondition.stepCondition(BinaryPredicateMetadata.allMatchMetadata(metadata, values), function,
                value -> values.stream().allMatch(value::equals));
    }

    /**
     * Returns a step condition checking if all of the given predicates testing the node value match.
     *
     * @param values the values to match
     * @return the step condition
     */
    public final StepCondition allMatch(List<Predicate<T>> values) {
        return LeafStepCondition.stepCondition(BinaryPredicateMetadata.allMatchMetadata(metadata), function,
                value -> values.stream().allMatch(v -> v.test(value)));
    }

    /**
     * Returns a step condition checking if the node value matches none of the given values.
     *
     * @param values the values to match
     * @return the step condition
     */
    @SafeVarargs
    public final StepCondition noneMatch(T... values) {
        return LeafStepCondition.stepCondition(BinaryPredicateMetadata.noneMatchMetadata(metadata, asList(values)), function,
                value -> Arrays.stream(values).noneMatch(value::equals));
    }

    /**
     * Returns a step condition checking if the node value matches none of the given values.
     *
     * @param values the values to match
     * @return the step condition
     */
    public final StepCondition noneMatch(Collection<T> values) {
        return LeafStepCondition.stepCondition(BinaryPredicateMetadata.noneMatchMetadata(metadata, values), function,
                value -> values.stream().noneMatch(value::equals));
    }

    /**
     * Returns a step condition checking if none of the given predicates testing the node value match.
     *
     * @param values the values to match
     * @return the step condition
     */
    public final StepCondition noneMatch(List<Predicate<T>> values) {
        return LeafStepCondition.stepCondition(BinaryPredicateMetadata.matchNoneMetadata(metadata), function,
                value -> values.stream().noneMatch(v -> v.test(value)));
    }

    /**
     * Returns an integer step condition that returns the node value mapped by the given mapper.
     *
     * @param mapper the to integer mapper to apply
     * @return the integer condition
     */
    public final IntegerFunction mapToInt(Function<T, Integer> mapper) {
        return new IntegerFunction(mapToIntMetadata(metadata), (model, context) -> value(model, context).map(mapper));
    }

    /**
     * Returns a string step condition that returns the node value mapped by the given function.
     *
     * @param mapper function to string to apply
     * @return string condition
     */
    public final StringFunction mapToString(Function<T, String> mapper) {
        return new StringFunction(mapToStringMetadata(metadata),
                (model, context) -> value(model, context).map(mapper));
    }

    /**
     * Returns a default step condition that returns the node value mapped by the given function.
     *
     * @param readable text describing the function
     * @param mapper   mapper function to apply
     * @param <R>      target type
     * @return condition with target type
     */
    // TODO move into a function provider
    public final <R> DefaultCondition<R> map(String readable, Function<T, R> mapper) {
        return new DefaultCondition<>(mapAsMetadata(metadata, readable),
                (model, context) -> value(model, context).map(mapper));
    }

    /**
     * Returns an default step condition that returns the node value mapped by the given function.
     *
     * @param readable  description
     * @param condition condition
     * @param mapper    mapper function to apply
     * @param <U>       condition type
     * @param <R>       target type
     * @return condition with target type
     */
    // TODO move into a function provider
    public final <U, R> DefaultCondition<R> mapUsing(String readable,
            io.doov.core.dsl.field.types.Function<U> condition,
            BiFunction<T, U, R> mapper) {
        return new DefaultCondition<>(mapUsingMetadata(metadata, readable, condition),
                (model, context) -> value(model, context).flatMap(l -> Optional.ofNullable(mapper.apply(l,
                        condition.value(model, context).orElse(null)))));
    }

}
