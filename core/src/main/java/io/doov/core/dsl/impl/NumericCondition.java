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

import static io.doov.core.dsl.meta.LeafMetadata.*;
import static io.doov.core.dsl.meta.NaryMetadata.minMetadata;
import static io.doov.core.dsl.meta.NaryMetadata.sumMetadata;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.field.types.NumericFieldInfo;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.PredicateMetadata;

/**
 * Base class for numeric conditions.
 * <p>
 * It contains a {@link DslField} to get the value from the model, a {@link PredicateMetadata} to describe this node,
 * and a {@link BiFunction} to take the value from the model and return an optional value.
 *
 * @param <N> the type of the field value
 */
public abstract class NumericCondition<N extends Number> extends DefaultCondition<N> {

    NumericCondition(DslField field) {
        super(field);
    }

    NumericCondition(DslField field, PredicateMetadata metadata, BiFunction<DslModel, Context, Optional<N>> value) {
        super(field, metadata, value);
    }

    protected abstract NumericCondition<N> numericCondition(DslField field, PredicateMetadata metadata,
                    BiFunction<DslModel, Context, Optional<N>> value);

    /**
     * Returns a step condition checking if the node value is lesser than the given value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition lesserThan(N value) {
        return predicate(lesserThanMetadata(field, value),
                        (model, context) -> Optional.ofNullable(value),
                        (l, r) -> lesserThanFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is lesser than the given field value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition lesserThan(NumericFieldInfo<N> value) {
        return predicate(lesserThanMetadata(field, value),
                        (model, context) -> valueModel(model, value),
                        (l, r) -> lesserThanFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is lesser or equals the given value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition lesserOrEquals(N value) {
        return predicate(lesserOrEqualsMetadata(field, value),
                        (model, context) -> Optional.ofNullable(value),
                        (l, r) -> lesserOrEqualsFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is lesser or equals the given field value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition lesserOrEquals(NumericFieldInfo<N> value) {
        return predicate(lesserOrEqualsMetadata(field, value),
                        (model, context) -> valueModel(model, value),
                        (l, r) -> lesserOrEqualsFunction().apply(l, r));
    }

    abstract BiFunction<N, N, Boolean> lesserThanFunction();

    abstract BiFunction<N, N, Boolean> lesserOrEqualsFunction();

    /**
     * Returns a step condition checking if the node value is greater than the given value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition greaterThan(N value) {
        return predicate(greaterThanMetadata(field, value),
                        (model, context) -> Optional.ofNullable(value),
                        (l, r) -> greaterThanFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is greater than the given field value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition greaterThan(NumericFieldInfo<N> value) {
        return predicate(greaterThanMetadata(field, value),
                        (model, context) -> valueModel(model, value),
                        (l, r) -> greaterThanFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is greater or equals the given value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition greaterOrEquals(N value) {
        return predicate(greaterOrEqualsMetadata(field, value),
                        (model, context) -> Optional.ofNullable(value),
                        (l, r) -> greaterOrEqualsFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is greater or equals the given field value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition greaterOrEquals(NumericFieldInfo<N> value) {
        return predicate(greaterOrEqualsMetadata(field, value),
                        (model, context) -> valueModel(model, value),
                        (l, r) -> greaterOrEqualsFunction().apply(l, r));
    }

    abstract BiFunction<N, N, Boolean> greaterThanFunction();

    abstract BiFunction<N, N, Boolean> greaterOrEqualsFunction();

    /**
     * Returns a step condition checking if the node value is between the given min included and max
     * excluded values.
     *
     * @param minIncluded the min value included
     * @param maxExcluded the max value excluded
     * @return the step condition
     */
    public final StepCondition between(N minIncluded, N maxExcluded) {
        return greaterOrEquals(minIncluded).and(lesserThan(maxExcluded));
    }

    /**
     * Returns a step condition checking if the node value is between the given min included and max
     * excluded field values.
     *
     * @param minIncluded the min value included
     * @param maxExcluded the max value excluded
     * @return the step condition
     */
    public final StepCondition between(NumericFieldInfo<N> minIncluded, NumericFieldInfo<N> maxExcluded) {
        return greaterOrEquals(minIncluded).and(lesserThan(maxExcluded));
    }

    /**
     * Returns a numeric step condition that returns the min value of the given field values.
     *
     * @param fields the field values to minimize
     * @return the numeric condition
     */
    public final NumericCondition<N> min(List<NumericFieldInfo<N>> fields) {
        return numericCondition(null, minMetadata(getMetadataForFields(fields)),
                        (model, context) -> fields.stream()
                                        .map(f -> Optional.ofNullable(model.<N> get(f.id())))
                                        .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                                        .reduce(minFunction()));
    }

    abstract BinaryOperator<N> minFunction();

    /**
     * Returns a numeric step condition that returns the sum value of the given field values.
     *
     * @param fields the field values to sum
     * @return the numeric condition
     */
    public final NumericCondition<N> sum(List<NumericFieldInfo<N>> fields) {
        return numericCondition(null, sumMetadata(getMetadataForFields(fields)),
                        (model, context) -> Optional.of(fields.stream()
                                        .map(f -> Optional.ofNullable(model.<N> get(f.id())))
                                        .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                                        .reduce(identity(), sumFunction())));
    }

    /**
     * Returns a numeric step condition that returns the sum value of the given condition values.
     *
     * @param conditions the condition values to sum
     * @return the numeric condition
     */
    public final NumericCondition<N> sumConditions(List<NumericCondition<N>> conditions) {
        return numericCondition(null, sumMetadata(getMetadataForConditions(conditions)),
                        (model, context) -> Optional.of(conditions.stream()
                                        .map(c -> c.function.apply(model, context))
                                        .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                                        .reduce(identity(), sumFunction())));
    }

    abstract BinaryOperator<N> sumFunction();

    /**
     * Returns a numeric step condition that returns the node value multiplied by the given multiplier.
     *
     * @param multiplier the multiplier
     * @return the numeric condition
     */
    public final NumericCondition<N> times(int multiplier) {
        return numericCondition(field, timesMetadata(field, multiplier),
                        (model, context) -> valueModel(model, field)
                                        .map(v -> timesFunction().apply(v, multiplier)));
    }

    abstract BiFunction<N, Integer, N> timesFunction();

    /**
     * Returns a numeric step condition that returns the node value if the condition evaluates to true.
     *
     * @param condition the condition to test
     * @return the numeric condition
     */
    public final NumericCondition<N> when(StepCondition condition) {
        return numericCondition(field, whenMetadata(field, condition),
                        (model, context) -> condition.predicate().test(model, context)
                                        ? valueModel(model, field)
                                        : Optional.empty());
    }

    abstract N identity();

    private static <N extends Number> List<Metadata> getMetadataForFields(List<NumericFieldInfo<N>> fields) {
        return fields.stream().map(field -> field.getNumericCondition().metadata).collect(toList());
    }

    private static <N extends Number> List<Metadata> getMetadataForConditions(List<NumericCondition<N>> conditions) {
        return conditions.stream().map(condition -> condition.metadata).collect(toList());
    }

}
