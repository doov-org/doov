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

import static io.doov.core.dsl.meta.predicate.LeafPredicateMetadata.*;
import static java.util.Arrays.asList;

import java.util.*;
import java.util.function.*;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.field.BaseFieldInfo;
import io.doov.core.dsl.field.types.Condition;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

/**
 * Base class for all conditions.
 * <p>
 * It contains a {@link DslField} to get the value from the model, a {@link PredicateMetadata} to describe this node,
 * and a {@link BiFunction} to take the value from the model and return an optional value.
 *
 * @param <T> the type of the field value
 */
public class DefaultCondition<T> extends AbstractCondition<T> {

    public DefaultCondition(DslField<T> field) {
        this(fieldMetadata(field), (model, context) -> valueModel(model, field));
    }

    public DefaultCondition(PredicateMetadata metadata, BiFunction<DslModel, Context, Optional<T>> value) {
        super(metadata, value);
    }

    /**
     * Returns a step condition checking if the node value is null.
     *
     * @return the step condition
     */
    public final StepCondition isNull() {
        return new PredicateStepCondition<>(nullMetadata(metadata),
                (model, context) -> Optional.of(value(model, context)),
                optional -> !optional.isPresent());
    }

    /**
     * Returns a step condition checking if the node value is not null.
     *
     * @return the step condition
     */
    public final StepCondition isNotNull() {
        return new PredicateStepCondition<>(notNullMetadata(metadata),
                (model, context) -> Optional.of(value(model, context)),
                Optional::isPresent);
    }

    /**
     * Returns a step condition checking if the node value is equal to the given value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition eq(T value) {
        return predicate(equalsMetadata(metadata, value),
                (model, context) -> Optional.ofNullable(value),
                Object::equals);
    }

    /**
     * Returns a step condition checking if the node value is equal to the given field value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition eq(Supplier<T> value) {
        return predicate(equalsMetadata(metadata, value),
                (model, context) -> Optional.ofNullable(value.get()),
                Object::equals);
    }

    /**
     * Returns a step condition checking if the node value is equal to the given supplier value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition eq(BaseFieldInfo<T> value) {
        return predicate(equalsMetadata(metadata, value),
                (model, context) -> valueModel(model, value),
                Object::equals);
    }

    /**
     * Returns a step condition checking if the node value is not equal to the given condition.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition eq(DefaultCondition<T> value) {
        return predicate(equalsMetadata(metadata, value),
                value.function,
                Object::equals);
    }

    /**
     * Returns a step condition checking if the node value is not equal to the given value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition notEq(T value) {
        return predicate(notEqualsMetadata(metadata, value),
                (model, context) -> Optional.ofNullable(value),
                (l, r) -> !l.equals(r));
    }

    /**
     * Returns a step condition checking if the node value is not equal to the given supplier value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition notEq(Supplier<T> value) {
        return predicate(notEqualsMetadata(metadata, value),
                (model, context) -> Optional.ofNullable(value.get()),
                (l, r) -> !l.equals(r));
    }

    /**
     * Returns a step condition checking if the node value is not equal to the given field value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition notEq(BaseFieldInfo<T> value) {
        return predicate(notEqualsMetadata(metadata, value),
                (model, context) -> valueModel(model, value),
                (l, r) -> !l.equals(r));
    }

    /**
     * Returns a step condition checking if the node value is not equal to the given condition.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition notEq(DefaultCondition<T> value) {
        return predicate(notEqualsMetadata(metadata, value),
                value.function,
                (l, r) -> !l.equals(r));
    }

    /**
     * Returns a step condition checking if the node value matches any of the given values.
     *
     * @param values the values to match
     * @return the step condition
     */
    @SafeVarargs
    public final StepCondition anyMatch(T... values) {
        return predicate(matchAnyMetadata(metadata, asList(values)),
                value -> Arrays.stream(values).anyMatch(value::equals));
    }

    /**
     * Returns a step condition checking if the node value matches any of the given values.
     *
     * @param values the values to match
     * @return the step condition
     */
    public final StepCondition anyMatch(Collection<T> values) {
        return predicate(matchAnyMetadata(metadata, values),
                value -> values.stream().anyMatch(value::equals));
    }

    /**
     * Returns a step condition checking if any of the given predicates testing the node value match.
     *
     * @param values the values to match
     * @return the step condition
     */
    public final StepCondition anyMatch(List<Predicate<T>> values) {
        return predicate(matchAnyMetadata(metadata),
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
        return predicate(matchAllMetadata(metadata, asList(values)),
                value -> Arrays.stream(values).allMatch(value::equals));
    }

    /**
     * Returns a step condition checking if the node value matches all of the given values.
     *
     * @param values the values to match
     * @return the step condition
     */
    public final StepCondition allMatch(Collection<T> values) {
        return predicate(matchAllMetadata(metadata, values),
                value -> values.stream().allMatch(value::equals));
    }

    /**
     * Returns a step condition checking if all of the given predicates testing the node value match.
     *
     * @param values the values to match
     * @return the step condition
     */
    public final StepCondition allMatch(List<Predicate<T>> values) {
        return predicate(matchAllMetadata(metadata),
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
        return predicate(matchNoneMetadata(metadata, asList(values)),
                value -> Arrays.stream(values).noneMatch(value::equals));
    }

    /**
     * Returns a step condition checking if the node value matches none of the given values.
     *
     * @param values the values to match
     * @return the step condition
     */
    public final StepCondition noneMatch(Collection<T> values) {
        return predicate(matchNoneMetadata(metadata, values),
                value -> values.stream().noneMatch(value::equals));
    }

    /**
     * Returns a step condition checking if none of the given predicates testing the node value match.
     *
     * @param values the values to match
     * @return the step condition
     */
    public final StepCondition noneMatch(List<Predicate<T>> values) {
        return predicate(matchNoneMetadata(metadata),
                value -> values.stream().noneMatch(v -> v.test(value)));
    }

    /**
     * Returns an integer step condition that returns the node value mapped by the given mapper.
     *
     * @param mapper the to integer mapper to apply
     * @return the integer condition
     */
    public final IntegerCondition mapToInt(Function<T, Integer> mapper) {
        return new IntegerCondition(mapToIntMetadata(metadata), (model, context) -> value(model, context).map(mapper));
    }

    /**
     * Returns a string step condition that returns the node value mapped by the given function.
     *
     * @param mapper function to string to apply
     * @return string condition
     */
    public final StringCondition mapToString(Function<T, String> mapper) {
        return new StringCondition(mapToStringMetadata(metadata),
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
    public final <U, R> DefaultCondition<R> mapUsing(String readable, Condition<U> condition,
            BiFunction<T, U, R> mapper) {
        return new DefaultCondition<>(mapUsingMetadata(metadata, readable, condition),
                (model, context) -> value(model, context).flatMap(l -> Optional.ofNullable(mapper.apply(l,
                        condition.value(model, context).orElse(null)))));
    }

}
