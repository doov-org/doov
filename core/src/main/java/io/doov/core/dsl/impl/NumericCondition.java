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

import static io.doov.core.dsl.meta.FieldMetadata.greaterOrEqualsMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.greaterThanMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.lesserOrEqualsMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.lesserThanMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.timesMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.whenMetadata;
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
import io.doov.core.dsl.field.NumericFieldInfo;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.PredicateMetadata;

public abstract class NumericCondition<N extends Number> extends DefaultCondition<N> {

    NumericCondition(DslField field) {
        super(field);
    }

    NumericCondition(DslField field, PredicateMetadata metadata, BiFunction<DslModel, Context, Optional<N>> value) {
        super(field, metadata, value);
    }

    abstract NumericCondition<N> numericCondition(DslField field, PredicateMetadata metadata,
                    BiFunction<DslModel, Context, Optional<N>> value);

    // lesser than

    public final StepCondition lesserThan(N value) {
        return predicate(lesserThanMetadata(field, value),
                        (model, context) -> Optional.ofNullable(value),
                        (l, r) -> lesserThanFunction().apply(l, r));
    }

    public final StepCondition lesserThan(NumericFieldInfo<N> value) {
        return predicate(lesserThanMetadata(field, value),
                        (model, context) -> valueModel(model, value),
                        (l, r) -> lesserThanFunction().apply(l, r));
    }

    public final StepCondition lesserOrEquals(N value) {
        return predicate(lesserOrEqualsMetadata(field, value),
                        (model, context) -> Optional.ofNullable(value),
                        (l, r) -> lesserOrEqualsFunction().apply(l, r));
    }

    public final StepCondition lesserOrEquals(NumericFieldInfo<N> value) {
        return predicate(lesserOrEqualsMetadata(field, value),
                        (model, context) -> valueModel(model, value),
                        (l, r) -> lesserOrEqualsFunction().apply(l, r));
    }

    public abstract BiFunction<N, N, Boolean> lesserThanFunction();

    public abstract BiFunction<N, N, Boolean> lesserOrEqualsFunction();

    // greater than

    public final StepCondition greaterThan(N value) {
        return predicate(greaterThanMetadata(field, value),
                        (model, context) -> Optional.ofNullable(value),
                        (l, r) -> greaterThanFunction().apply(l, r));
    }

    public final StepCondition greaterThan(NumericFieldInfo<N> value) {
        return predicate(greaterThanMetadata(field, value),
                        (model, context) -> valueModel(model, value),
                        (l, r) -> greaterThanFunction().apply(l, r));
    }

    public final StepCondition greaterOrEquals(N value) {
        return predicate(greaterOrEqualsMetadata(field, value),
                        (model, context) -> Optional.ofNullable(value),
                        (l, r) -> greaterOrEqualsFunction().apply(l, r));
    }

    public final StepCondition greaterOrEquals(NumericFieldInfo<N> value) {
        return predicate(greaterOrEqualsMetadata(field, value),
                        (model, context) -> valueModel(model, value),
                        (l, r) -> greaterOrEqualsFunction().apply(l, r));
    }

    public abstract BiFunction<N, N, Boolean> greaterThanFunction();

    public abstract BiFunction<N, N, Boolean> greaterOrEqualsFunction();

    // between

    public final StepCondition between(N minIncluded, N maxExcluded) {
        return greaterOrEquals(minIncluded).and(lesserThan(maxExcluded));
    }

    public final StepCondition between(NumericFieldInfo<N> minIncluded, NumericFieldInfo<N> maxExcluded) {
        return greaterOrEquals(minIncluded).and(lesserThan(maxExcluded));
    }

    // min

    public final NumericCondition<N> min(List<NumericFieldInfo<N>> fields) {
        return numericCondition(null, minMetadata(getMetadataForFields(fields)),
                        (model, context) -> fields.stream()
                                        .map(f -> Optional.ofNullable(model.<N> get(f.id())))
                                        .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                                        .reduce(minFunction()));
    }

    abstract BinaryOperator<N> minFunction();

    // sum

    public final NumericCondition<N> sum(List<NumericFieldInfo<N>> fields) {
        return numericCondition(null, sumMetadata(getMetadataForFields(fields)),
                        (model, context) -> Optional.of(fields.stream()
                                        .map(f -> Optional.ofNullable(model.<N> get(f.id())))
                                        .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                                        .reduce(identity(), sumFunction())));
    }

    public final NumericCondition<N> sumConditions(List<NumericCondition<N>> conditions) {
        return numericCondition(null, sumMetadata(getMetadataForConditions(conditions)),
                        (model, context) -> Optional.of(conditions.stream()
                                        .map(c -> c.function.apply(model, context))
                                        .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                                        .reduce(identity(), sumFunction())));
    }

    abstract BinaryOperator<N> sumFunction();

    // times

    public final NumericCondition<N> times(int multiplier) {
        return numericCondition(field, timesMetadata(field, multiplier),
                        (model, context) -> valueModel(model, field)
                                        .map(v -> timesFunction().apply(v, multiplier)));
    }

    abstract BiFunction<N, Integer, N> timesFunction();

    // when

    public final NumericCondition<N> when(StepCondition condition) {
        return numericCondition(field, whenMetadata(field, condition),
                        (model, context) -> condition.predicate().test(model, context)
                                        ? valueModel(model, field)
                                        : Optional.empty());
    }

    // identity

    abstract N identity();

    // static

    private static <N extends Number> List<Metadata> getMetadataForFields(List<NumericFieldInfo<N>> fields) {
        return fields.stream().map(field -> field.getNumericCondition().metadata).collect(toList());
    }

    private static <N extends Number> List<Metadata> getMetadataForConditions(List<NumericCondition<N>> conditions) {
        return conditions.stream().map(condition -> condition.metadata).collect(toList());
    }

}
