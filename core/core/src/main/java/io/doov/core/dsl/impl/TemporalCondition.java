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
import static java.time.temporal.ChronoUnit.YEARS;

import java.time.temporal.*;
import java.util.Optional;
import java.util.function.*;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.field.NumericFieldInfo;
import io.doov.core.dsl.field.TemporalFieldInfo;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.Metadata;

public abstract class TemporalCondition<N extends Temporal> extends DefaultCondition<N> {

    public TemporalCondition(DslField field) {
        super(field);
    }

    public TemporalCondition(DslField field, Metadata metadata, BiFunction<DslModel, Context, Optional<N>> value) {
        super(field, metadata, value);
    }

    protected abstract TemporalCondition<N> temporalCondition(DslField field, Metadata metadata,
                    BiFunction<DslModel, Context, Optional<N>> value);

    // with

    public final TemporalCondition<N> with(TemporalAdjuster adjuster) {
        return temporalCondition(field, withMetadata(field, adjuster),
                        (model, context) -> value(model, field)
                                        .map(v -> withFunction(adjuster).apply(v)));
    }

    protected abstract Function<N, N> withFunction(TemporalAdjuster adjuster);

    // minus

    public final TemporalCondition<N> minus(int value, TemporalUnit unit) {
        return temporalCondition(field, minusMetadata(field, value, unit),
                        (model, context) -> value(model, field)
                                        .map(v -> minusFunction(value, unit).apply(v)));
    }

    public final TemporalCondition<N> minus(NumericFieldInfo<Integer> value, TemporalUnit unit) {
        return temporalCondition(field, minusMetadata(field, value, unit),
                        (model, context) -> value(model, field)
                                        .flatMap(l -> Optional.ofNullable(model.<Integer> get(value.id()))
                                                        .map(r -> minusFunction(r, unit).apply(l))));
    }

    protected abstract Function<N, N> minusFunction(int value, TemporalUnit unit);

    // plus

    public final TemporalCondition<N> plus(int value, TemporalUnit unit) {
        return temporalCondition(field, plusMetadata(field, value, unit),
                        (model, context) -> value(model, field)
                                        .map(v -> plusFunction(value, unit).apply(v)));
    }

    public final TemporalCondition<N> plus(NumericFieldInfo<Integer> value, TemporalUnit unit) {
        return temporalCondition(field, plusMetadata(field, value, unit),
                        (model, context) -> value(model, field)
                                        .flatMap(l -> Optional.ofNullable(model.<Integer> get(value.id()))
                                                        .map(r -> plusFunction(r, unit).apply(l))));
    }

    protected abstract Function<N, N> plusFunction(int value, TemporalUnit unit);

    // eq

    public final StepCondition eq(TemporalCondition<N> value) {
        return predicate(equalsMetadata(field, value),
                        value.function,
                        Object::equals);
    }

    // before

    public final StepCondition before(N value) {
        return predicate(beforeMetadata(field, value),
                        (model, context) -> Optional.ofNullable(value),
                        (l, r) -> beforeFunction().apply(l, r));
    }

    public final StepCondition before(TemporalFieldInfo<N> value) {
        return predicate(beforeMetadata(field, value),
                        (model, context) -> value(model, value),
                        (l, r) -> beforeFunction().apply(l, r));
    }

    public final StepCondition before(Supplier<N> value) {
        return predicate(beforeMetadata(field, value),
                        (model, context) -> Optional.ofNullable(value.get()),
                        (l, r) -> beforeFunction().apply(l, r));
    }

    public final StepCondition before(TemporalCondition<N> value) {
        return predicate(beforeMetadata(field, value.metadata),
                        value.function,
                        (l, r) -> beforeFunction().apply(l, r));
    }

    public final StepCondition beforeOrEq(N value) {
        return LogicalBinaryCondition.or(before(value), eq(value));
    }

    public final StepCondition beforeOrEq(Supplier<N> value) {
        return LogicalBinaryCondition.or(before(value), eq(value));
    }

    public final StepCondition beforeOrEq(TemporalCondition<N> value) {
        return LogicalBinaryCondition.or(before(value), eq(value));
    }

    protected abstract BiFunction<N, N, Boolean> beforeFunction();

    // after

    public final StepCondition after(N value) {
        return predicate(afterMetadata(field, value),
                        (model, context) -> Optional.ofNullable(value),
                        (l, r) -> afterFunction().apply(l, r));
    }

    public final StepCondition after(TemporalFieldInfo<N> value) {
        return predicate(afterMetadata(field, value),
                        (model, context) -> value(model, value),
                        (l, r) -> afterFunction().apply(l, r));
    }

    public final StepCondition after(Supplier<N> value) {
        return predicate(afterMetadata(field, value),
                        (model, context) -> Optional.ofNullable(value.get()),
                        (l, r) -> afterFunction().apply(l, r));
    }

    public final StepCondition after(TemporalCondition<N> value) {
        return predicate(afterMetadata(field, value.metadata),
                        value.function,
                        (l, r) -> afterFunction().apply(l, r));
    }

    public final StepCondition afterOrEq(N value) {
        return LogicalBinaryCondition.or(after(value), eq(value));
    }

    public final StepCondition afterOrEq(Supplier<N> value) {
        return LogicalBinaryCondition.or(after(value), eq(value));
    }

    public final StepCondition afterOrEq(TemporalCondition<N> value) {
        return LogicalBinaryCondition.or(after(value), eq(value));
    }

    protected abstract BiFunction<N, N, Boolean> afterFunction();

    // between

    public final StepCondition between(N minValueInclusive, N maxValueExclusive) {
        return LogicalBinaryCondition.and(beforeOrEq(maxValueExclusive), afterOrEq(minValueInclusive));
    }

    public final StepCondition between(Supplier<N> minValueInclusive, Supplier<N> maxValueExclusive) {
        return LogicalBinaryCondition.and(beforeOrEq(maxValueExclusive), afterOrEq(minValueInclusive));
    }

    public final StepCondition notBetween(N minValueInclusive, N maxValueExclusive) {
        return LogicalUnaryCondition.negate(between(minValueInclusive, maxValueExclusive));
    }

    // age

    public final NumericCondition<Integer> ageAt(N value) {
        return new IntegerCondition(timeBetween(YEARS, value));
    }

    public final NumericCondition<Integer> ageAt(TemporalFieldInfo<N> value) {
        return new IntegerCondition(timeBetween(YEARS, value));
    }

    public final NumericCondition<Integer> ageAt(TemporalCondition<N> value) {
        return new IntegerCondition(timeBetween(YEARS, value));
    }

    public final NumericCondition<Integer> ageAt(Supplier<N> value) {
        return new IntegerCondition(timeBetween(YEARS, value));
    }

    // time between

    public final NumericCondition<Long> timeBetween(ChronoUnit unit, N value) {
        return new LongCondition(field, ageAtMetadata(field, value),
                        (model, context) -> value(model, field)
                                        .flatMap(l -> Optional.ofNullable(value)
                                                        .map(r -> betweenFunction(unit).apply(l, r))));
    }

    public final NumericCondition<Long> timeBetween(ChronoUnit unit, TemporalFieldInfo<N> value) {
        return new LongCondition(field, ageAtMetadata(field, value),
                        (model, context) -> value(model, field)
                                        .flatMap(l -> valueModel(model, value)
                                                        .map(r -> betweenFunction(unit).apply(l, r))));
    }

    public final NumericCondition<Long> timeBetween(ChronoUnit unit, TemporalCondition<N> value) {
        return new LongCondition(field, ageAtMetadata(field, value),
                        (model, context) -> value(model, field)
                                        .flatMap(l -> value.function.apply(model, context)
                                                        .map(r -> betweenFunction(unit).apply(l, r))));
    }

    public final NumericCondition<Long> timeBetween(ChronoUnit unit, Supplier<N> value) {
        return new LongCondition(field, ageAtMetadata(field, value),
                        (model, context) -> value(model, field)
                                        .flatMap(l -> Optional.ofNullable(value.get())
                                                        .map(r -> betweenFunction(unit).apply(l, r))));
    }

    protected abstract BiFunction<N, N, Long> betweenFunction(ChronoUnit unit);

}
