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
import io.doov.core.dsl.field.NumericFieldInfo;
import io.doov.core.dsl.field.TemporalFieldInfo;
import io.doov.core.dsl.lang.StepCondition;

public abstract class TemporalCondition<N extends Temporal> extends DefaultCondition<N> {

    TemporalCondition(DslField field) {
        super(field);
    }

    // with

    public final StepFunction<N> with(TemporalAdjuster adjuster) {
        return function(withMetadata(field, adjuster),
                        (v) -> withFunction(adjuster).apply(v));
    }

    abstract Function<N, N> withFunction(TemporalAdjuster ajuster);

    // minus

    public final StepFunction<N> minus(int value, TemporalUnit unit) {
        return function(minusMetadata(field, value, unit),
                        (v) -> minusFunction(value, unit).apply(v));
    }

    public final StepFunction<N> minus(NumericFieldInfo<Integer> value, TemporalUnit unit) {
        return this.<Integer> function(minusMetadata(field, value, unit),
                        (model, context) -> Optional.ofNullable(model.get(value.id())),
                        (l, r) -> minusFunction(r, unit).apply(l));
    }

    public final StepFunction<N> minus(int value, TemporalUnit unit, TemporalAdjuster ajuster) {
        return function(minusMetadata(field, value, unit),
                        (v) -> minusFunction(value, unit)
                                        .andThen(after -> withFunction(ajuster).apply(after))
                                        .apply(v));
    }

    abstract Function<N, N> minusFunction(int value, TemporalUnit unit);

    // plus

    public final StepFunction<N> plus(int value, TemporalUnit unit) {
        return function(plusMetadata(field, value, unit),
                        (v) -> plusFunction(value, unit).apply(v));
    }

    public final StepFunction<N> plus(NumericFieldInfo<Integer> value, TemporalUnit unit) {
        return this.<Integer> function(plusMetadata(field, value, unit),
                        (model, context) -> Optional.ofNullable(model.get(value.id())),
                        (l, r) -> plusFunction(r, unit).apply(l));
    }

    public final StepFunction<N> plus(int value, TemporalUnit unit, TemporalAdjuster ajuster) {
        return function(plusMetadata(field, value, unit),
                        (v) -> plusFunction(value, unit)
                                        .andThen(after -> withFunction(ajuster).apply(after))
                                        .apply(v));
    }

    abstract Function<N, N> plusFunction(int value, TemporalUnit unit);

    // eq

    public final StepCondition equals(StepFunction<N> value) {
        return predicate(equalsMetadata(field, value),
                        (model, context) -> Optional.ofNullable(value.function.apply(model, context)),
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

    public final StepCondition before(StepFunction<N> value) {
        return predicate(beforeMetadata(field, value.metadata),
                        (model, context) -> Optional.ofNullable(value.function.apply(model, context)),
                        (l, r) -> beforeFunction().apply(l, r));
    }

    public final StepCondition beforeOrEq(N value) {
        return LogicalBinaryCondition.or(before(value), eq(value));
    }

    public final StepCondition beforeOrEq(Supplier<N> value) {
        return LogicalBinaryCondition.or(before(value), eq(value));
    }

    public final StepCondition beforeOrEq(StepFunction<N> value) {
        return LogicalBinaryCondition.or(before(value), equals(value));
    }

    abstract BiFunction<N, N, Boolean> beforeFunction();

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

    public final StepCondition after(StepFunction<N> value) {
        return predicate(afterMetadata(field, value.metadata),
                        (model, context) -> Optional.ofNullable(value.function.apply(model, context)),
                        (l, r) -> afterFunction().apply(l, r));
    }

    public final StepCondition afterOrEq(N value) {
        return LogicalBinaryCondition.or(after(value), eq(value));
    }

    public final StepCondition afterOrEq(Supplier<N> value) {
        return LogicalBinaryCondition.or(after(value), eq(value));
    }

    public final StepCondition afterOrEq(StepFunction<N> value) {
        return LogicalBinaryCondition.or(after(value), equals(value));
    }

    abstract BiFunction<N, N, Boolean> afterFunction();

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

    public final NumericCondition<Integer> ageAt(StepFunction<N> value) {
        return new IntegerCondition(timeBetween(YEARS, value));
    }

    public final NumericCondition<Integer> ageAt(TemporalFieldInfo<N> value, TemporalAdjuster ajuster) {
        return new IntegerCondition(timeBetween(YEARS, value, ajuster));
    }

    public final NumericCondition<Integer> ageAt(StepFunction<N> value, TemporalAdjuster ajuster) {
        return new IntegerCondition(timeBetween(YEARS, value, ajuster));
    }

    public final NumericCondition<Integer> ageAt(Supplier<N> value) {
        return new IntegerCondition(timeBetween(YEARS, value));
    }

    public final NumericCondition<Integer> ageAt(Supplier<N> value, TemporalAdjuster ajuster) {
        return new IntegerCondition(timeBetween(YEARS, value, ajuster));
    }

    // time between

    public final NumericCondition<Long> timeBetween(ChronoUnit unit, N value) {
        return new LongCondition(ageAtMetadata(field, value),
                        (model, context) -> value(model, field)
                                        .flatMap(l -> Optional.ofNullable(value)
                                                        .map(r -> betweenFunction(unit).apply(l, r))));
    }

    public final NumericCondition<Long> timeBetween(ChronoUnit unit, TemporalFieldInfo<N> value) {
        return new LongCondition(ageAtMetadata(field, value),
                        (model, context) -> value(model, field)
                                        .flatMap(l -> value(model, value)
                                                        .map(r -> betweenFunction(unit).apply(l, r))));
    }

    public final NumericCondition<Long> timeBetween(ChronoUnit unit, StepFunction<N> value) {
        return new LongCondition(ageAtMetadata(field, value),
                        (model, context) -> value(model, field)
                                        .flatMap(l -> Optional.ofNullable(value.function.apply(model, context))
                                                        .map(r -> betweenFunction(unit).apply(l, r))));
    }

    public final NumericCondition<Long> timeBetween(ChronoUnit unit, TemporalFieldInfo<N> value, TemporalAdjuster
                    ajuster) {
        return new LongCondition(ageAtMetadata(field, value),
                        (model, context) -> value(model, field)
                                        .map(l -> withFunction(ajuster).apply(l))
                                        .flatMap(l -> value(model, value)
                                                        .map(r -> withFunction(ajuster).apply(r))
                                                        .map(r -> betweenFunction(unit).apply(l, r))));
    }

    public final NumericCondition<Long> timeBetween(ChronoUnit unit, StepFunction<N> value, TemporalAdjuster ajuster) {
        return new LongCondition(ageAtMetadata(field, value),
                        (model, context) -> value(model, field)
                                        .map(l -> withFunction(ajuster).apply(l))
                                        .flatMap(l -> Optional.ofNullable(value.function.apply(model, context))
                                                        .map(r -> withFunction(ajuster).apply(r))
                                                        .map(r -> betweenFunction(unit).apply(l, r))));
    }

    public final NumericCondition<Long> timeBetween(ChronoUnit unit, Supplier<N> value) {
        return new LongCondition(ageAtMetadata(field, value),
                        (model, context) -> value(model, field).flatMap(l -> Optional.ofNullable(value.get())
                                        .map(r -> betweenFunction(unit).apply(l, r))));
    }

    public final NumericCondition<Long> timeBetween(ChronoUnit unit, Supplier<N> value, TemporalAdjuster ajuster) {
        return new LongCondition(ageAtMetadata(field, value),
                        (model, context) -> value(model, field)
                                        .map(l -> withFunction(ajuster).apply(l))
                                        .flatMap(l -> Optional.ofNullable(value.get())
                                                        .map(r -> withFunction(ajuster).apply(r))
                                                        .map(r -> betweenFunction(unit).apply(l, r))));
    }

    abstract BiFunction<N, N, Long> betweenFunction(ChronoUnit unit);

}
