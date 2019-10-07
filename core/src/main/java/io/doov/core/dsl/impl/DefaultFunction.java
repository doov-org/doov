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
import static java.util.Arrays.asList;

import java.util.*;
import java.util.function.*;

import io.doov.core.*;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.field.BaseFieldInfo;
import io.doov.core.dsl.field.types.Function;
import io.doov.core.dsl.impl.base.IterableFunction;
import io.doov.core.dsl.impl.base.StringFunction;
import io.doov.core.dsl.impl.num.IntegerFunction;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.function.MapFunctionMetadata;
import io.doov.core.dsl.meta.predicate.*;

public class DefaultFunction<T, M extends Metadata> implements Function<T> {

    protected final M metadata;
    protected final BiFunction<FieldModel, Context, Optional<T>> function;

    public DefaultFunction(M metadata, BiFunction<FieldModel, Context, Optional<T>> function) {
        this.metadata = metadata;
        this.function = function;
    }

    public Optional<T> value(FieldModel model, Context context) {
        return function.apply(model, context);
    }

    public M getMetadata() {
        return metadata;
    }

    public BiFunction<FieldModel, Context, Optional<T>> getFunction() {
        return function;
    }

    @Override
    public String readable() {
        return metadata.readable();
    }

    /**
     * Returns a iterable function encapsulating the tags of this encapsulated field.
     *
     * @return the iterable function
     */
    public final IterableFunction<TagId, List<TagId>> tags() {
        return new IterableFunction<>(UnaryPredicateMetadata.tagsMetadata(metadata),
                (m, c) -> Optional.ofNullable(metadata)
                        .filter(o -> o instanceof FieldMetadata)
                        .map(f -> (FieldMetadata) f)
                        .map(FieldMetadata::field)
                        .map(DslField::id)
                        .map(FieldId::tags));
    }

    /**
     * Returns a integer function encapsulating the position of this encapsulated field.
     *
     * @return the integer function
     */
    public final IntegerFunction position() {
        return new IntegerFunction(UnaryPredicateMetadata.positionMetadata(metadata),
                (m, c) -> Optional.ofNullable(metadata)
                        .filter(o -> o instanceof FieldMetadata)
                        .map(f -> (FieldMetadata) f)
                        .map(FieldMetadata::field)
                        .map(DslField::id)
                        .map(FieldId::position));
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
     * @deprecated use {@link #eq(Object)} or {@link #eq(Function)} instead
     */
    @Deprecated
    public final StepCondition eq(Supplier<T> supplier) {
        return LeafStepCondition.stepCondition(BinaryPredicateMetadata.equalsMetadata(metadata, supplier), function,
                supplier,
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
     * Returns a step condition checking if the node value is equal to the given supplier value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition eq(Function<T> value) {
        return LeafStepCondition.stepCondition(BinaryPredicateMetadata.equalsMetadata(metadata, value), function,
                (BiFunction<FieldModel, Context, Optional<T>>) (m, c) -> Optional.ofNullable(value.read(m, c)),
                Object::equals);
    }

    /**
     * Returns a step condition checking if the node value is not equal to the given value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition notEq(T value) {
        return LeafStepCondition.stepCondition(BinaryPredicateMetadata.notEqualsMetadata(metadata, value), function,
                value,
                (l, r) -> !l.equals(r));
    }

    /**
     * Returns a step condition checking if the node value is not equal to the given supplier value.
     *
     * @param supplier the right side value
     * @return the step condition
     * @deprecated use {@link #notEq(Object)} or {@link #notEq(Function)} instead
     */
    @Deprecated
    public final StepCondition notEq(Supplier<T> supplier) {
        return LeafStepCondition.stepCondition(BinaryPredicateMetadata.notEqualsMetadata(metadata, supplier), function,
                supplier,
                (l, r) -> !l.equals(r));
    }

    /**
     * Returns a step condition checking if the node value is not equal to the given field value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition notEq(BaseFieldInfo<T> value) {
        return LeafStepCondition.stepCondition(BinaryPredicateMetadata.notEqualsMetadata(metadata, value), function,
                value,
                (l, r) -> !l.equals(r));
    }

    /**
     * Returns a step condition checking if the node value is not equal to the given field value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition notEq(Function<T> value) {
        return LeafStepCondition.stepCondition(BinaryPredicateMetadata.notEqualsMetadata(metadata, value), function,
                (BiFunction<FieldModel, Context, Optional<T>>) (m, c) -> Optional.ofNullable(value.read(m, c)),
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
     * @param predicateReadables strings defining predicates in order
     * @return the step condition
     */
    public final StepCondition anyMatch(List<Predicate<T>> values, String... predicateReadables) {
        return LeafStepCondition.stepCondition(BinaryPredicateMetadata.anyMatchMetadata(metadata, predicateReadables),
                function, value -> values.stream().anyMatch(v -> v.test(value)));
    }

    /**
     * Returns a step condition checking if the node value matches all of the given values.
     *
     * @param values the values to match
     * @return the step condition
     */
    @SafeVarargs
    public final StepCondition allMatch(T... values) {
        return LeafStepCondition.stepCondition(BinaryPredicateMetadata.allMatchMetadata(metadata, asList(values)),
                function,
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
     * @param predicateReadables strings defining predicates in order
     * @return the step condition
     */
    public final StepCondition allMatch(List<Predicate<T>> values, String... predicateReadables) {
        return LeafStepCondition.stepCondition(BinaryPredicateMetadata.allMatchMetadata(metadata, predicateReadables),
                function, value -> values.stream().allMatch(v -> v.test(value)));
    }

    /**
     * Returns a step condition checking if the node value matches none of the given values.
     *
     * @param values the values to match
     * @return the step condition
     */
    @SafeVarargs
    public final StepCondition noneMatch(T... values) {
        return LeafStepCondition.stepCondition(BinaryPredicateMetadata.noneMatchMetadata(metadata, asList(values)),
                function,
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
     * @param predicateReadables strings defining predicates in order
     * @return the step condition
     */
    public final StepCondition noneMatch(List<Predicate<T>> values, String... predicateReadables) {
        return LeafStepCondition.stepCondition(BinaryPredicateMetadata.matchNoneMetadata(metadata, predicateReadables),
                function, value -> values.stream().noneMatch(v -> v.test(value)));
    }

    /**
     * Returns an integer step condition that returns the node value mapped by the given mapper.
     *
     * @param mapper the to integer mapper to apply
     * @return the integer condition
     * @deprecated use {@link #mapToInt(String, java.util.function.Function)} instead
     */
    @Deprecated
    public final IntegerFunction mapToInt(java.util.function.Function<T, Integer> mapper) {
        return mapToInt("", mapper);
    }

    /**
     * Returns an integer step condition that returns the node value mapped by the given mapper.
     *
     * @param readable descriptor string for the mapper function
     * @param mapper the to integer mapper to apply
     * @return the integer condition
     */
    public final IntegerFunction mapToInt(String readable, java.util.function.Function<T, Integer> mapper) {
        return new IntegerFunction(mapToIntMetadata(metadata, readable),
                (model, context) -> value(model, context).map(mapper));
    }

    /**
     * Returns a string step condition that returns the node value mapped by the given function.
     *
     * @param mapper function to string mapper to apply
     * @return string condition
     * @deprecated use {@link #mapToString(String, java.util.function.Function)} instead
     */
    @Deprecated
    public final StringFunction mapToString(java.util.function.Function<T, String> mapper) {
        return mapToString("", mapper);
    }

    /**
     * Returns a string step condition that returns the node value mapped by the given function.
     *
     * @param readable descriptor string for the mapper function
     * @param mapper function to string mapper to apply
     * @return string condition
     */
    public final StringFunction mapToString(String readable, java.util.function.Function<T, String> mapper) {
        return new StringFunction(mapToStringMetadata(metadata, readable),
                (model, context) -> value(model, context).map(mapper));
    }

    /**
     * Returns a default step condition that returns the node value mapped by the given function.
     *
     * @param readable text describing the function
     * @param mapper   mapper function to apply
     * @param          <R> target type
     * @return condition with target type
     */
    // TODO move into a function provider
    public final <R> DefaultFunction<R, MapFunctionMetadata> map(String readable, java.util.function.Function<T, R> mapper) {
        return new DefaultFunction<>(mapAsMetadata(metadata, readable),
                (model, context) -> value(model, context).map(mapper));
    }

    /**
     * Returns an default step condition that returns the node value mapped by the given function.
     *
     * @param readable  description
     * @param condition condition
     * @param mapper    mapper function to apply
     * @param           <U> condition type
     * @param           <R> target type
     * @return condition with target type
     */
    // TODO move into a function provider
    public final <U, R> DefaultFunction<R, MapFunctionMetadata> mapUsing(String readable,
            io.doov.core.dsl.field.types.Function<U> condition,
            BiFunction<T, U, R> mapper) {
        return new DefaultFunction<>(mapUsingMetadata(metadata, readable, condition),
                (model, context) -> value(model, context).flatMap(l -> Optional.ofNullable(mapper.apply(l,
                        condition.value(model, context).orElse(null)))));
    }


}
