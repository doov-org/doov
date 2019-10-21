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
package io.doov.core.dsl.impl.num;

import static io.doov.core.dsl.meta.function.NumericFunctionMetadata.*;
import static io.doov.core.dsl.meta.predicate.NaryPredicateMetadata.maxMetadata;
import static io.doov.core.dsl.meta.predicate.NaryPredicateMetadata.minMetadata;
import static io.doov.core.dsl.meta.predicate.NaryPredicateMetadata.sumMetadata;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.field.types.NumericFieldInfo;
import io.doov.core.dsl.impl.DefaultFunction;
import io.doov.core.dsl.impl.LeafStepCondition;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

/**
 * Base class for numeric functions.
 * <p>
 * It contains a {@link DslField} to get the value from the model, a {@link PredicateMetadata} to describe this node,
 * and a {@link BiFunction} to take the value from the model and return an optional value.
 *
 * @param <N> the type of the field value
 */
public abstract class NumericFunction<N extends Number> extends DefaultFunction<N, PredicateMetadata>
        implements NumericOperators<N> {

    public NumericFunction(PredicateMetadata metadata, BiFunction<FieldModel, Context, Optional<N>> value) {
        super(metadata, value);
    }

    protected abstract NumericFunction<N> numericFunction(PredicateMetadata metadata,
            BiFunction<FieldModel, Context, Optional<N>> value);

    /**
     * Returns a step condition checking if the node value is lesser than the given value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition lesserThan(N value) {
        return LeafStepCondition.stepCondition(lesserThanMetadata(metadata, value), getFunction(), value,
                (l, r) -> lesserThanFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is lesser than the given field value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition lesserThan(NumericFieldInfo<N> value) {
        return LeafStepCondition.stepCondition(lesserThanMetadata(metadata, value), getFunction(), value,
                (l, r) -> lesserThanFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is lesser than the given field value.
     *
     * @param function the right side value
     * @return the step condition
     */
    public final StepCondition lesserThan(NumericFunction<N> function) {
        return LeafStepCondition.stepCondition(lesserThanMetadata(metadata, function.metadata), getFunction(),
                function.getFunction(), (l, r) -> lesserThanFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is lesser or equals the given value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition lesserOrEquals(N value) {
        return LeafStepCondition.stepCondition(lesserOrEqualsMetadata(metadata, value), getFunction(), value,
                (l, r) -> lesserOrEqualsFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is lesser or equals the given field value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition lesserOrEquals(NumericFieldInfo<N> value) {
        return LeafStepCondition.stepCondition(lesserOrEqualsMetadata(metadata, value), getFunction(), value,
                (l, r) -> lesserOrEqualsFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is lesser or equals the given field value.
     *
     * @param function the right side function
     * @return the step condition
     */
    public final StepCondition lesserOrEquals(NumericFunction<N> function) {
        return LeafStepCondition.stepCondition(lesserOrEqualsMetadata(metadata, function.metadata), getFunction(),
                function.getFunction(), (l, r) -> lesserOrEqualsFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is greater than the given value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition greaterThan(N value) {
        return LeafStepCondition.stepCondition(greaterThanMetadata(metadata, value), getFunction(), value,
                (l, r) -> greaterThanFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is greater than the given field value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition greaterThan(NumericFieldInfo<N> value) {
        return LeafStepCondition.stepCondition(greaterThanMetadata(metadata, value), getFunction(), value,
                (l, r) -> greaterThanFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is greater than the given field value.
     *
     * @param function the right side function
     * @return the step condition
     */
    public final StepCondition greaterThan(NumericFunction<N> function) {
        return LeafStepCondition.stepCondition(greaterThanMetadata(metadata, function.metadata), getFunction(),
                function.getFunction(), (l, r) -> greaterThanFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is greater or equals the given value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition greaterOrEquals(N value) {
        return LeafStepCondition.stepCondition(greaterOrEqualsMetadata(metadata, value), getFunction(), value,
                (l, r) -> greaterOrEqualsFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is greater or equals the given field value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition greaterOrEquals(NumericFieldInfo<N> value) {
        return LeafStepCondition.stepCondition(greaterOrEqualsMetadata(metadata, value), getFunction(), value,
                (l, r) -> greaterOrEqualsFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is greater or equals the given field value.
     *
     * @param function the right side value
     * @return the step condition
     */
    public final StepCondition greaterOrEquals(NumericFunction<N> function) {
        return LeafStepCondition.stepCondition(greaterOrEqualsMetadata(metadata, function.metadata), getFunction(),
                function.getFunction(), (l, r) -> greaterOrEqualsFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is between the given min included and max excluded values.
     *
     * @param minIncluded the min value included
     * @param maxExcluded the max value excluded
     * @return the step condition
     */
    public final StepCondition between(N minIncluded, N maxExcluded) {
        return greaterOrEquals(minIncluded).and(lesserThan(maxExcluded));
    }

    /**
     * Returns a step condition checking if the node value is between the given min included and max excluded field
     * values.
     *
     * @param minIncluded the min value included
     * @param maxExcluded the max value excluded
     * @return the step condition
     */
    public final StepCondition between(NumericFieldInfo<N> minIncluded, NumericFieldInfo<N> maxExcluded) {
        return greaterOrEquals(minIncluded).and(lesserThan(maxExcluded));
    }

    /**
     * Returns a step condition checking if the node value is between the given min included and max excluded field
     * values.
     *
     * @param minIncluded the min function included
     * @param maxExcluded the max function excluded
     * @return the step condition
     */
    public final StepCondition between(NumericFunction<N> minIncluded, NumericFunction<N> maxExcluded) {
        return greaterOrEquals(minIncluded).and(lesserThan(maxExcluded));
    }

    /**
     * Returns a numeric function that returns the node value multiplied by the given multiplier.
     *
     * @param multiplier the multiplier
     * @return the numeric function
     */
    public final NumericFunction<N> times(int multiplier) {
        return numericFunction(timesMetadata(metadata, multiplier),
                (model, context) -> value(model, context).map(v -> timesFunction().apply(v, multiplier)));
    }

    /**
     * Returns a numeric function that returns the node value sum with the node value param.
     *
     * @param field the field to sum
     * @return the numeric function
     */
    public final NumericFunction<N> plus(NumericFieldInfo<N> field) {
        return numericFunction(plusMetadata(metadata, field),
                (model, context) -> value(model, context)
                        .map(v -> sumFunction().apply(v,
                                Optional.ofNullable(model.<N> get(field.id())).orElse(identity()))));
    }

    /**
     * Returns a numeric function that returns the node value sum with the node value param.
     *
     * @param value to sum
     * @return the numeric function
     */
    public final NumericFunction<N> plus(N value) {
        return numericFunction(plusMetadata(metadata, value),
                (model, context) -> value(model, context).map(v -> sumFunction().apply(v, value)));
    }

    /**
     * Returns a numeric function that returns the node value sum with the node value param.
     *
     * @param numericFunction function to sum
     * @return the numeric function
     */
    public final NumericFunction<N> plus(NumericFunction<N> numericFunction) {
        return numericFunction(plusMetadata(metadata, numericFunction.metadata),
                (model, context) -> value(model, context).map(v ->
                        sumFunction().apply(v, numericFunction.value(model, context).orElse(identity()))));
    }

    /**
     * Returns a numeric function that returns the node value sum with the node value param.
     *
     * @param field the field to subtract
     * @return the numeric function
     */
    public final NumericFunction<N> minus(NumericFieldInfo<N> field) {
        return numericFunction(minusMetadata(metadata, field),
                (model, context) -> value(model, context)
                        .map(v -> minusFunction().apply(v,
                                Optional.ofNullable(model.<N> get(field.id())).orElse(identity()))));
    }

    /**
     * Returns a numeric function that returns the node value sum with the node value param.
     *
     * @param value to subtract
     * @return the numeric function
     */
    public final NumericFunction<N> minus(N value) {
        return numericFunction(minusMetadata(metadata, value),
                (model, context) -> value(model, context).map(v -> minusFunction().apply(v, value)));
    }

    /**
     * Returns a numeric function that returns the node value sum with the node value param.
     *
     * @param numericFunction function to subtract
     * @return the numeric function
     */
    public final NumericFunction<N> minus(NumericFunction<N> numericFunction) {
        return numericFunction(minusMetadata(metadata, numericFunction.metadata),
                (model, context) -> value(model, context).map(v ->
                        minusFunction().apply(v, numericFunction.value(model, context).orElse(identity()))));
    }

    /**
     * Returns a numeric function that returns the min value of the given field values.
     *
     * @param fields the field values to minimize
     * @return the numeric function
     */
    public final NumericFunction<N> min(List<NumericFieldInfo<N>> fields) {
        return numericFunction(minMetadata(getMetadataForFields(fields)),
                (model, context) -> fields.stream().map(f -> Optional.ofNullable(model.<N> get(f.id())))
                        .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                        .reduce(minFunction()));
    }

    /**
     * Returns a numeric function that returns the min value of the given field values.
     *
     * @param numbers the field values to minimize
     * @return the numeric function
     */
    public final NumericFunction<N> minFunctions(List<NumericFunction<N>> numbers) {
        return numericFunction(minMetadata(getMetadataForConditions(numbers)),
                (model, context) -> numbers.stream().map(f -> Optional.ofNullable(f.read(model, context)))
                        .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                        .reduce(minFunction()));
    }

    /**
     * Returns a numeric function that returns the max value of the given field values.
     *
     * @param fields the field values to maximize
     * @return the numeric function
     */
    public final NumericFunction<N> max(List<NumericFieldInfo<N>> fields) {
        return numericFunction(maxMetadata(getMetadataForFields(fields)),
                (model, context) -> fields.stream().map(f -> Optional.ofNullable(model.<N> get(f.id())))
                        .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                        .reduce(maxFunction()));
    }

    /**
     * Returns a numeric function that returns the max value of the given field values.
     *
     * @param numbers the field values to maximize
     * @return the numeric function
     */
    public final NumericFunction<N> maxFunctions(List<NumericFunction<N>> numbers) {
        return numericFunction(maxMetadata(getMetadataForConditions(numbers)),
                (model, context) -> numbers.stream().map(f -> Optional.ofNullable(f.read(model, context)))
                        .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                        .reduce(maxFunction()));
    }

    /**
     * Returns a numeric function that returns the sum value of the given field values.
     *
     * @param fields the field values to sum
     * @return the numeric function
     */
    public final NumericFunction<N> sum(List<NumericFieldInfo<N>> fields) {
        return numericFunction(sumMetadata(getMetadataForFields(fields)), (model,
                context) -> Optional.of(fields.stream().map(f -> Optional.ofNullable(model.<N> get(f.id())))
                .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                .reduce(identity(), sumFunction())));
    }

    /**
     * Returns a numeric function that returns the sum value of the given condition values.
     *
     * @param conditions the condition values to sum
     * @return the numeric function
     */
    public final NumericFunction<N> sumConditions(List<NumericFunction<N>> conditions) {
        return numericFunction(sumMetadata(getMetadataForConditions(conditions)),
                (model, context) -> Optional.of(conditions.stream().map(c -> c.getFunction().apply(model, context))
                        .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                        .reduce(identity(), sumFunction())));
    }

    private static <N extends Number> List<Metadata> getMetadataForFields(List<NumericFieldInfo<N>> fields) {
        return fields.stream().map(field -> field.getNumericFunction().getMetadata()).collect(toList());
    }

    private static <N extends Number> List<Metadata> getMetadataForConditions(List<NumericFunction<N>> conditions) {
        return conditions.stream().map(condition -> condition.getMetadata()).collect(toList());
    }

    /**
     * Returns a numeric step condition that returns the node value if the condition evaluates to true.
     *
     * @param condition the condition to test
     * @return the numeric condition
     */
    public final NumericFunction<N> when(StepCondition condition) {
        return numericFunction(whenMetadata(metadata, condition),
                (model, context) -> condition.predicate().test(model, context) ? value(model, context)
                        : Optional.empty());
    }
}
