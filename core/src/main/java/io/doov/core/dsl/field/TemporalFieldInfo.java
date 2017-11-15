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
package io.doov.core.dsl.field;

import static java.time.temporal.ChronoUnit.YEARS;

import java.time.temporal.*;
import java.util.function.Supplier;

import io.doov.core.dsl.impl.*;
import io.doov.core.dsl.lang.StepCondition;

public interface TemporalFieldInfo<N extends Temporal> extends BaseFieldInfo<N> {

    // with

    default StepFunction<N> with(TemporalAdjuster ajuster) {
        return getTemporalCondition().with(ajuster);
    }

    // minus

    default StepFunction<N> minus(int value, TemporalUnit unit) {
        return getTemporalCondition().minus(value, unit);
    }

    default StepFunction<N> minus(NumericFieldInfo<Integer> value, TemporalUnit unit) {
        return getTemporalCondition().minus(value, unit);
    }

    default StepFunction<N> minus(int value, TemporalUnit unit, TemporalAdjuster ajuster) {
        return getTemporalCondition().minus(value, unit, ajuster);
    }

    default StepFunction<N> minusYears(int value) {
        return getTemporalCondition().minus(value, YEARS);
    }

    // plus

    default StepFunction<N> plus(int value, TemporalUnit unit) {
        return getTemporalCondition().plus(value, unit);
    }

    default StepFunction<N> plus(NumericFieldInfo<Integer> value, TemporalUnit unit) {
        return getTemporalCondition().plus(value, unit);
    }

    default StepFunction<N> plus(int value, TemporalUnit unit, TemporalAdjuster ajuster) {
        return getTemporalCondition().plus(value, unit, ajuster);
    }

    default StepFunction<N> plusYears(int value) {
        return getTemporalCondition().plus(value, YEARS);
    }

    // before

    default StepCondition before(N value) {
        return getTemporalCondition().before(value);
    }

    default StepCondition before(TemporalFieldInfo<N> value) {
        return getTemporalCondition().before(value);
    }

    default StepCondition before(Supplier<N> value) {
        return getTemporalCondition().before(value);
    }

    default StepCondition before(StepFunction<N> value) {
        return getTemporalCondition().before(value);
    }

    default StepCondition beforeOrEq(N value) {
        return getTemporalCondition().beforeOrEq(value);
    }

    default StepCondition beforeOrEq(Supplier<N> value) {
        return getTemporalCondition().beforeOrEq(value);
    }

    default StepCondition beforeOrEq(StepFunction<N> value) {
        return getTemporalCondition().beforeOrEq(value);
    }

    // after

    default StepCondition after(N value) {
        return getTemporalCondition().after(value);
    }

    default StepCondition after(TemporalFieldInfo<N> value) {
        return getTemporalCondition().after(value);
    }

    default StepCondition after(Supplier<N> value) {
        return getTemporalCondition().after(value);
    }

    default StepCondition after(StepFunction<N> value) {
        return getTemporalCondition().after(value);
    }

    default StepCondition afterOrEq(Supplier<N> value) {
        return getTemporalCondition().afterOrEq(value);
    }

    default StepCondition afterOrEq(N value) {
        return getTemporalCondition().afterOrEq(value);
    }

    default StepCondition afterOrEq(StepFunction<N> value) {
        return getTemporalCondition().afterOrEq(value);
    }

    // beetween

    default StepCondition between(N minValueInclusive, N maxValueExclusive) {
        return getTemporalCondition().between(minValueInclusive, maxValueExclusive);
    }

    default StepCondition between(Supplier<N> minValueInclusive, Supplier<N> maxValueExclusive) {
        return getTemporalCondition().between(minValueInclusive, maxValueExclusive);
    }

    default StepCondition notBetween(N minValueInclusive, N maxValueExclusive) {
        return getTemporalCondition().notBetween(minValueInclusive, maxValueExclusive);
    }

    // age

    default NumericCondition<Integer> ageAt(N value) {
        return getTemporalCondition().ageAt(value);
    }

    default NumericCondition<Integer> ageAt(TemporalFieldInfo<N> value) {
        return getTemporalCondition().ageAt(value);
    }

    default NumericCondition<Integer> ageAt(StepFunction<N> value) {
        return getTemporalCondition().ageAt(value);
    }

    default NumericCondition<Integer> ageAt(TemporalFieldInfo<N> value, TemporalAdjuster ajuster) {
        return getTemporalCondition().ageAt(value, ajuster);
    }

    default NumericCondition<Integer> ageAt(StepFunction<N> value, TemporalAdjuster ajuster) {
        return getTemporalCondition().ageAt(value, ajuster);
    }

    default NumericCondition<Integer> ageAt(Supplier<N> value) {
        return getTemporalCondition().ageAt(value);
    }

    default NumericCondition<Integer> ageAt(Supplier<N> value, TemporalAdjuster ajuster) {
        return getTemporalCondition().ageAt(value, ajuster);
    }

    // time between

    default NumericCondition<Long> timeBetween(ChronoUnit unit, N value) {
        return getTemporalCondition().timeBetween(unit, value);
    }

    default NumericCondition<Long> timeBetween(ChronoUnit unit, TemporalFieldInfo<N> value) {
        return getTemporalCondition().timeBetween(unit, value);
    }

    default NumericCondition<Long> timeBetween(ChronoUnit unit, StepFunction<N> value) {
        return getTemporalCondition().timeBetween(unit, value);
    }

    default NumericCondition<Long> timeBetween(ChronoUnit unit, TemporalFieldInfo<N> value, TemporalAdjuster ajuster) {
        return getTemporalCondition().timeBetween(unit, value, ajuster);
    }

    default NumericCondition<Long> timeBetween(ChronoUnit unit, StepFunction<N> value, TemporalAdjuster ajuster) {
        return getTemporalCondition().timeBetween(unit, value, ajuster);
    }

    default NumericCondition<Long> timeBetween(ChronoUnit unit, Supplier<N> value) {
        return getTemporalCondition().timeBetween(unit, value);
    }

    default NumericCondition<Long> timeBetween(ChronoUnit unit, Supplier<N> value, TemporalAdjuster ajuster) {
        return getTemporalCondition().timeBetween(unit, value, ajuster);
    }

    // abstract

    TemporalCondition<N> getTemporalCondition();

}
