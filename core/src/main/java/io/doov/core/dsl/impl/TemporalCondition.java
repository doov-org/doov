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
import static java.time.temporal.ChronoUnit.YEARS;

import java.time.temporal.*;
import java.util.Optional;
import java.util.function.*;

import io.doov.core.dsl.SimpleFieldId;
import io.doov.core.dsl.lang.StepCondition;

public abstract class TemporalCondition<N extends Temporal> extends DefaultCondition<N> {

    TemporalCondition(SimpleFieldId<N> field) {
        super(field);
    }

    // minus plus

    public final FunctionWithMetadata<N, N> minus(int value, TemporalUnit unit) {
        return function(minusMetadata(field, value, unit),
                        (v) -> minusFunction(value, unit).apply(v));
    }

    public final FunctionWithMetadata<N, N> minus(int value, TemporalUnit unit, TemporalAdjuster ajuster) {
        return function(minusMetadata(field, value, unit),
                        (v) -> minusFunction(value, unit)
                                        .andThen(after -> withFunction(ajuster).apply(after))
                                        .apply(v));
    }

    public final FunctionWithMetadata<N, N> plus(int value, TemporalUnit unit) {
        return function(plusMetadata(field, value, unit),
                        (v) -> plusFunction(value, unit).apply(v));
    }

    public final FunctionWithMetadata<N, N> plus(int value, TemporalUnit unit, TemporalAdjuster ajuster) {
        return function(plusMetadata(field, value, unit),
                        (v) -> plusFunction(value, unit)
                                        .andThen(after -> withFunction(ajuster).apply(after))
                                        .apply(v));
    }

    abstract Function<N, N> minusFunction(int value, TemporalUnit unit);

    abstract Function<N, N> plusFunction(int value, TemporalUnit unit);

    abstract Function<N, N> withFunction(TemporalAdjuster ajuster);

    // before

    public final StepCondition before(N value) {
        return predicate(beforeMetadata(field, value),
                        (model, context) -> Optional.ofNullable(value),
                        (l, r) -> beforeFunction().apply(l, r));
    }

    public final StepCondition before(SimpleFieldId<N> value) {
        return predicate(beforeMetadata(field, value),
                        (model, context) -> value(model, value),
                        (l, r) -> beforeFunction().apply(l, r));
    }

    public final StepCondition before(Supplier<N> value) {
        return predicate(beforeMetadata(field, value),
                        (model, context) -> Optional.ofNullable(value.get()),
                        (l, r) -> beforeFunction().apply(l, r));
    }

    public final StepCondition before(FunctionWithMetadata<N, N> value) {
        return predicate(beforeMetadata(field, value.metadata),
                        (model, context) -> value(model, field).map(value.function),
                        (l, r) -> beforeFunction().apply(l, r));
    }

    public final StepCondition beforeOrEq(N value) {
        return LogicalBinaryCondition.or(before(value), new TypeCondition<>(field).eq(value));
    }

    public final StepCondition beforeOrEq(Supplier<N> value) {
        return LogicalBinaryCondition.or(before(value), new TypeCondition<>(field).eq(value));
    }

    public final StepCondition beforeOrEq(FunctionWithMetadata<N, N> value) {
        return LogicalBinaryCondition.or(before(value), new TypeCondition<>(field).eq(value));
    }

    abstract BiFunction<N, N, Boolean> beforeFunction();

    // after

    public final StepCondition after(N value) {
        return predicate(afterMetadata(field, value),
                        (model, context) -> Optional.ofNullable(value),
                        (l, r) -> afterFunction().apply(l, r));
    }

    public final StepCondition after(SimpleFieldId<N> value) {
        return predicate(afterMetadata(field, value),
                        (model, context) -> value(model, value),
                        (l, r) -> afterFunction().apply(l, r));
    }

    public final StepCondition after(Supplier<N> value) {
        return predicate(afterMetadata(field, value),
                        (model, context) -> Optional.ofNullable(value.get()),
                        (l, r) -> afterFunction().apply(l, r));
    }

    public final StepCondition after(FunctionWithMetadata<N, N> value) {
        return predicate(afterMetadata(field, value.metadata),
                        (model, context) -> value(model, field).map(value.function),
                        (l, r) -> afterFunction().apply(l, r));
    }

    public final StepCondition afterOrEq(N value) {
        return LogicalBinaryCondition.or(after(value), new TypeCondition<>(field).eq(value));
    }

    public final StepCondition afterOrEq(Supplier<N> value) {
        return LogicalBinaryCondition.or(after(value), new TypeCondition<>(field).eq(value));
    }

    public final StepCondition afterOrEq(FunctionWithMetadata<N, N> value) {
        return LogicalBinaryCondition.or(after(value), new TypeCondition<>(field).eq(value));
    }

    abstract BiFunction<N, N, Boolean> afterFunction();

    // between

    public final StepCondition between(N minValue, N maxValue) {
        return LogicalBinaryCondition.and(beforeOrEq(maxValue), afterOrEq(minValue));
    }

    public final StepCondition between(Supplier<N> minValue, Supplier<N> maxValue) {
        return LogicalBinaryCondition.and(beforeOrEq(maxValue), afterOrEq(minValue));
    }

    public final StepCondition notBetween(N minValue, N maxValue) {
        return LogicalUnaryCondition.negate(between(minValue, maxValue));
    }

    abstract BiFunction<N, N, Long> betweenFunction(ChronoUnit unit);

    // age

    public final NumericCondition<Integer> ageAt(N value) {
        return new IntegerCondition(ageAtMetadata(field, value),
                        (model, context) -> value(model, field)
                                        .flatMap(l -> Optional.ofNullable(value)
                                                        .map(r -> betweenFunction(YEARS).apply(l, r)))
                                        .map(Long::intValue));
    }

    public final NumericCondition<Integer> ageAt(SimpleFieldId<N> value) {
        return new IntegerCondition(ageAtMetadata(field, value),
                        (model, context) -> value(model, field)
                                        .flatMap(l -> value(model, value)
                                                        .map(r -> betweenFunction(YEARS).apply(l, r)))
                                        .map(Long::intValue));
    }

    public final NumericCondition<Integer> ageAt(SimpleFieldId<N> value, TemporalAdjuster ajuster) {
        return new IntegerCondition(ageAtMetadata(field, value),
                        (model, context) -> value(model, field)
                                        .map(l -> withFunction(ajuster).apply(l))
                                        .flatMap(l -> value(model, value)
                                                        .map(r -> betweenFunction(YEARS).apply(l, r)))
                                        .map(Long::intValue));
    }

    public final NumericCondition<Integer> ageAt(Supplier<N> value) {
        return new IntegerCondition(ageAtMetadata(field, value),
                        (model, context) -> value(model, field).
                                        flatMap(l -> Optional.ofNullable(value.get())
                                                        .map(r -> betweenFunction(YEARS).apply(l, r)))
                                        .map(Long::intValue));
    }

    public final NumericCondition<Integer> ageAt(Supplier<N> value, TemporalAdjuster ajuster) {
        return new IntegerCondition(ageAtMetadata(field, value),
                        (model, context) -> value(model, field)
                                        .map(l -> withFunction(ajuster).apply(l))
                                        .flatMap(l -> Optional.ofNullable(value.get())
                                                        .map(r -> betweenFunction(YEARS).apply(l, r)))
                                        .map(Long::intValue));
    }

}
