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

import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.function.Supplier;

import io.doov.core.dsl.impl.NumericCondition;
import io.doov.core.dsl.impl.TemporalCondition;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.time.TemporalAdjuster;

public interface TemporalFieldInfo<N extends Temporal> extends BaseFieldInfo<N> {
    
    // eq
    
    default StepCondition eq(TemporalCondition<N> value) {
        return getDefaultCondition().eq(value);
    }

    // with

    default TemporalCondition<N> with(TemporalAdjuster ajuster) {
        return getTemporalCondition().with(ajuster);
    }

    // minus

    default TemporalCondition<N> minus(int value, TemporalUnit unit) {
        return getTemporalCondition().minus(value, unit);
    }

    default TemporalCondition<N> minus(NumericFieldInfo<Integer> value, TemporalUnit unit) {
        return getTemporalCondition().minus(value, unit);
    }

    default TemporalCondition<N> minusYears(int value) {
        return getTemporalCondition().minus(value, YEARS);
    }

    // plus

    default TemporalCondition<N> plus(int value, TemporalUnit unit) {
        return getTemporalCondition().plus(value, unit);
    }

    default TemporalCondition<N> plus(NumericFieldInfo<Integer> value, TemporalUnit unit) {
        return getTemporalCondition().plus(value, unit);
    }

    default TemporalCondition<N> plusYears(int value) {
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

    default StepCondition before(TemporalCondition<N> value) {
        return getTemporalCondition().before(value);
    }

    default StepCondition beforeOrEq(N value) {
        return getTemporalCondition().beforeOrEq(value);
    }

    default StepCondition beforeOrEq(Supplier<N> value) {
        return getTemporalCondition().beforeOrEq(value);
    }

    default StepCondition beforeOrEq(TemporalCondition<N> value) {
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

    default StepCondition after(TemporalCondition<N> value) {
        return getTemporalCondition().after(value);
    }

    default StepCondition afterOrEq(Supplier<N> value) {
        return getTemporalCondition().afterOrEq(value);
    }

    default StepCondition afterOrEq(N value) {
        return getTemporalCondition().afterOrEq(value);
    }

    default StepCondition afterOrEq(TemporalCondition<N> value) {
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

    default NumericCondition<Integer> ageAt(TemporalCondition<N> value) {
        return getTemporalCondition().ageAt(value);
    }

    default NumericCondition<Integer> ageAt(Supplier<N> value) {
        return getTemporalCondition().ageAt(value);
    }

    // days between

    default NumericCondition<Integer> daysBetween(N value) {
        return getTemporalCondition().daysBetween(value);
    }

    default NumericCondition<Integer> daysBetween(TemporalFieldInfo<N> value) {
        return getTemporalCondition().daysBetween(value);
    }

    default NumericCondition<Integer> daysBetween(TemporalCondition<N> value) {
        return getTemporalCondition().daysBetween(value);
    }

    default NumericCondition<Integer> daysBetween(Supplier<N> value) {
        return getTemporalCondition().daysBetween(value);
    }

    // month between

    default NumericCondition<Integer> monthsBetween(N value) {
        return getTemporalCondition().monthsBetween(value);
    }

    default NumericCondition<Integer> monthsBetween(TemporalFieldInfo<N> value) {
        return getTemporalCondition().monthsBetween(value);
    }

    default NumericCondition<Integer> monthsBetween(TemporalCondition<N> value) {
        return getTemporalCondition().monthsBetween(value);
    }

    default NumericCondition<Integer> monthsBetween(Supplier<N> value) {
        return getTemporalCondition().monthsBetween(value);
    }

    // year between

    default NumericCondition<Integer> yearsBetween(N value) {
        return getTemporalCondition().yearsBetween(value);
    }

    default NumericCondition<Integer> yearsBetween(TemporalFieldInfo<N> value) {
        return getTemporalCondition().yearsBetween(value);
    }

    default NumericCondition<Integer> yearsBetween(TemporalCondition<N> value) {
        return getTemporalCondition().yearsBetween(value);
    }

    default NumericCondition<Integer> yearsBetween(Supplier<N> value) {
        return getTemporalCondition().yearsBetween(value);
    }

    // abstract

    TemporalCondition<N> getTemporalCondition();

}
