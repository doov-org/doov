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
package io.doov.core.dsl.field.types;

import static java.time.temporal.ChronoUnit.YEARS;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.function.Supplier;

import io.doov.core.dsl.field.BaseFieldInfo;
import io.doov.core.dsl.impl.DefaultCondition;
import io.doov.core.dsl.impl.NumericCondition;
import io.doov.core.dsl.impl.TemporalCondition;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.time.TemporalAdjuster;

/**
 * Base interface for temporal field info.
 * <p>
 * It contains default methods for common checks, which are calls to methods on {@link TemporalCondition}.
 *
 * @param <N> the type of the field value
 */
public interface TemporalFieldInfo<N extends Temporal> extends BaseFieldInfo<N> {

    /**
     * See {@link TemporalCondition#eq(TemporalCondition)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalCondition#eq(TemporalCondition)
     */
    default StepCondition eq(TemporalCondition<N> value) {
        return getTemporalCondition().eq(value);
    }

    /**
     * See {@link TemporalCondition#with(TemporalAdjuster)}
     *
     * @param adjuster the adjuster
     * @return the step condition
     * @see TemporalCondition#with(TemporalAdjuster)
     */
    default TemporalCondition<N> with(TemporalAdjuster adjuster) {
        return getTemporalCondition().with(adjuster);
    }

    /**
     * See {@link TemporalCondition#minus(int, TemporalUnit)}
     *
     * @param value the minus value
     * @param unit  the minus unit
     * @return the step condition
     * @see TemporalCondition#minus(int, TemporalUnit)
     */
    default TemporalCondition<N> minus(int value, TemporalUnit unit) {
        return getTemporalCondition().minus(value, unit);
    }

    /**
     * See {@link TemporalCondition#minus(NumericFieldInfo, TemporalUnit)}
     *
     * @param value the minus field value
     * @param unit  the minus unit
     * @return the step condition
     * @see TemporalCondition#minus(NumericFieldInfo, TemporalUnit)
     */
    default TemporalCondition<N> minus(NumericFieldInfo<Integer> value, TemporalUnit unit) {
        return getTemporalCondition().minus(value, unit);
    }

    /**
     * See {@link TemporalCondition#minus(NumericFieldInfo, TemporalUnit)}
     *
     * @param value the years to remove
     * @return the step condition
     * @see TemporalCondition#minus(NumericFieldInfo, TemporalUnit)
     */
    default TemporalCondition<N> minusYears(int value) {
        return getTemporalCondition().minus(value, YEARS);
    }

    /**
     * See {@link TemporalCondition#plus(int, TemporalUnit)}
     *
     * @param value the plus value
     * @param unit  the plus unit
     * @return the step condition
     * @see TemporalCondition#plus(int, TemporalUnit)
     */
    default TemporalCondition<N> plus(int value, TemporalUnit unit) {
        return getTemporalCondition().plus(value, unit);
    }

    /**
     * See {@link TemporalCondition#plus(NumericFieldInfo, TemporalUnit)}
     *
     * @param value the plus field value
     * @param unit  the plus unit
     * @return the step condition
     * @see TemporalCondition#plus(NumericFieldInfo, TemporalUnit)
     */
    default TemporalCondition<N> plus(NumericFieldInfo<Integer> value, TemporalUnit unit) {
        return getTemporalCondition().plus(value, unit);
    }

    /**
     * See {@link TemporalCondition#plus(int, TemporalUnit)}
     *
     * @param value the years to add
     * @return the step condition
     * @see TemporalCondition#plus(int, TemporalUnit)
     */
    default TemporalCondition<N> plusYears(int value) {
        return getTemporalCondition().plus(value, YEARS);
    }

    /**
     * See {@link TemporalCondition#before(Temporal)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalCondition#before(Temporal)
     */
    default StepCondition before(N value) {
        return getTemporalCondition().before(value);
    }

    /**
     * See {@link TemporalCondition#before(TemporalFieldInfo)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalCondition#before(TemporalFieldInfo)
     */
    default StepCondition before(TemporalFieldInfo<N> value) {
        return getTemporalCondition().before(value);
    }

    /**
     * See {@link TemporalCondition#before(Supplier)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalCondition#before(Supplier)
     */
    default StepCondition before(Supplier<N> value) {
        return getTemporalCondition().before(value);
    }

    /**
     * See {@link TemporalCondition#before(TemporalCondition)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalCondition#before(TemporalCondition)
     */
    default StepCondition before(TemporalCondition<N> value) {
        return getTemporalCondition().before(value);
    }

    /**
     * See {@link TemporalCondition#beforeOrEq(Temporal)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalCondition#beforeOrEq(Temporal)
     */
    default StepCondition beforeOrEq(N value) {
        return getTemporalCondition().beforeOrEq(value);
    }

    /**
     * See {@link TemporalCondition#beforeOrEq(Supplier)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalCondition#beforeOrEq(Supplier)
     */
    default StepCondition beforeOrEq(Supplier<N> value) {
        return getTemporalCondition().beforeOrEq(value);
    }

    /**
     * See {@link TemporalCondition#beforeOrEq(TemporalCondition)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalCondition#beforeOrEq(TemporalCondition)
     */
    default StepCondition beforeOrEq(TemporalCondition<N> value) {
        return getTemporalCondition().beforeOrEq(value);
    }

    /**
     * See {@link TemporalCondition#after(Temporal)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalCondition#after(Temporal)
     */
    default StepCondition after(N value) {
        return getTemporalCondition().after(value);
    }

    /**
     * See {@link TemporalCondition#after(TemporalFieldInfo)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalCondition#after(TemporalFieldInfo)
     */
    default StepCondition after(TemporalFieldInfo<N> value) {
        return getTemporalCondition().after(value);
    }

    /**
     * See {@link TemporalCondition#after(Supplier)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalCondition#after(Supplier)
     */
    default StepCondition after(Supplier<N> value) {
        return getTemporalCondition().after(value);
    }

    /**
     * See {@link TemporalCondition#after(TemporalCondition)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalCondition#after(TemporalCondition)
     */
    default StepCondition after(TemporalCondition<N> value) {
        return getTemporalCondition().after(value);
    }

    /**
     * See {@link TemporalCondition#afterOrEq(Temporal)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalCondition#afterOrEq(Temporal)
     */
    default StepCondition afterOrEq(N value) {
        return getTemporalCondition().afterOrEq(value);
    }

    /**
     * See {@link TemporalCondition#afterOrEq(Supplier)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalCondition#afterOrEq(Supplier)
     */
    default StepCondition afterOrEq(Supplier<N> value) {
        return getTemporalCondition().afterOrEq(value);
    }

    /**
     * See {@link TemporalCondition#afterOrEq(TemporalCondition)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalCondition#afterOrEq(TemporalCondition)
     */
    default StepCondition afterOrEq(TemporalCondition<N> value) {
        return getTemporalCondition().afterOrEq(value);
    }

    /**
     * See {@link TemporalCondition#between(Temporal, Temporal)}
     *
     * @param minIncluded the min value included
     * @param maxExcluded the max value excluded
     * @return the step condition
     * @see TemporalCondition#between(Temporal, Temporal)
     */
    default StepCondition between(N minIncluded, N maxExcluded) {
        return getTemporalCondition().between(minIncluded, maxExcluded);
    }

    /**
     * See {@link TemporalCondition#between(Supplier, Supplier)}
     *
     * @param minIncluded the min value included
     * @param maxExcluded the max value excluded
     * @return the step condition
     * @see TemporalCondition#between(Supplier, Supplier)
     */
    default StepCondition between(Supplier<N> minIncluded, Supplier<N> maxExcluded) {
        return getTemporalCondition().between(minIncluded, maxExcluded);
    }

    /**
     * See {@link TemporalCondition#notBetween(Temporal, Temporal)}
     *
     * @param minIncluded the min value included
     * @param maxExcluded the max value excluded
     * @return the step condition
     * @see TemporalCondition#notBetween(Temporal, Temporal)
     */
    default StepCondition notBetween(N minIncluded, N maxExcluded) {
        return getTemporalCondition().notBetween(minIncluded, maxExcluded);
    }

    /**
     * See {@link TemporalCondition#ageAt(Temporal)}
     *
     * @param value the right side value
     * @return the numeric condition
     * @see TemporalCondition#ageAt(Temporal)
     */
    default NumericCondition<Integer> ageAt(N value) {
        return getTemporalCondition().ageAt(value);
    }

    /**
     * See {@link TemporalCondition#ageAt(TemporalFieldInfo)}
     *
     * @param value the right side value
     * @return the numeric condition
     * @see TemporalCondition#ageAt(TemporalFieldInfo)
     */
    default NumericCondition<Integer> ageAt(TemporalFieldInfo<N> value) {
        return getTemporalCondition().ageAt(value);
    }

    /**
     * See {@link TemporalCondition#ageAt(TemporalCondition)}
     *
     * @param value the right side value
     * @return the numeric condition
     * @see TemporalCondition#ageAt(TemporalCondition)
     */
    default NumericCondition<Integer> ageAt(TemporalCondition<N> value) {
        return getTemporalCondition().ageAt(value);
    }

    /**
     * See {@link TemporalCondition#ageAt(Supplier)}
     *
     * @param value the right side value
     * @return the numeric condition
     * @see TemporalCondition#ageAt(Supplier)
     */
    default NumericCondition<Integer> ageAt(Supplier<N> value) {
        return getTemporalCondition().ageAt(value);
    }

    /**
     * See {@link TemporalCondition#daysBetween(Temporal)}
     *
     * @param value the right side value
     * @return the numeric condition
     * @see TemporalCondition#daysBetween(Temporal)
     */
    default NumericCondition<Integer> daysBetween(N value) {
        return getTemporalCondition().daysBetween(value);
    }

    /**
     * See {@link TemporalCondition#daysBetween(TemporalFieldInfo)}
     *
     * @param value the right side value
     * @return the numeric condition
     * @see TemporalCondition#daysBetween(TemporalFieldInfo)
     */
    default NumericCondition<Integer> daysBetween(TemporalFieldInfo<N> value) {
        return getTemporalCondition().daysBetween(value);
    }

    /**
     * See {@link TemporalCondition#daysBetween(TemporalCondition)}
     *
     * @param value the right side value
     * @return the numeric condition
     * @see TemporalCondition#daysBetween(TemporalCondition)
     */
    default NumericCondition<Integer> daysBetween(TemporalCondition<N> value) {
        return getTemporalCondition().daysBetween(value);
    }

    /**
     * See {@link TemporalCondition#daysBetween(Supplier)}
     *
     * @param value the right side value
     * @return the numeric condition
     * @see TemporalCondition#daysBetween(Supplier)
     */
    default NumericCondition<Integer> daysBetween(Supplier<N> value) {
        return getTemporalCondition().daysBetween(value);
    }

    /**
     * See {@link TemporalCondition#monthsBetween(Temporal)}
     *
     * @param value the right side value
     * @return the numeric condition
     * @see TemporalCondition#monthsBetween(Temporal)
     */
    default NumericCondition<Integer> monthsBetween(N value) {
        return getTemporalCondition().monthsBetween(value);
    }

    /**
     * See {@link TemporalCondition#monthsBetween(TemporalFieldInfo)}
     *
     * @param value the right side value
     * @return the numeric condition
     * @see TemporalCondition#monthsBetween(TemporalFieldInfo)
     */
    default NumericCondition<Integer> monthsBetween(TemporalFieldInfo<N> value) {
        return getTemporalCondition().monthsBetween(value);
    }

    /**
     * See {@link TemporalCondition#monthsBetween(TemporalCondition)}
     *
     * @param value the right side value
     * @return the numeric condition
     * @see TemporalCondition#monthsBetween(TemporalCondition)
     */
    default NumericCondition<Integer> monthsBetween(TemporalCondition<N> value) {
        return getTemporalCondition().monthsBetween(value);
    }

    /**
     * See {@link TemporalCondition#monthsBetween(Supplier)}
     *
     * @param value the right side value
     * @return the numeric condition
     * @see TemporalCondition#monthsBetween(Supplier)
     */
    default NumericCondition<Integer> monthsBetween(Supplier<N> value) {
        return getTemporalCondition().monthsBetween(value);
    }

    /**
     * See {@link TemporalCondition#yearsBetween(Temporal)}
     *
     * @param value the right side value
     * @return the numeric condition
     * @see TemporalCondition#yearsBetween(Temporal)
     */
    default NumericCondition<Integer> yearsBetween(N value) {
        return getTemporalCondition().yearsBetween(value);
    }

    /**
     * See {@link TemporalCondition#yearsBetween(TemporalFieldInfo)}
     *
     * @param value the right side value
     * @return the numeric condition
     * @see TemporalCondition#yearsBetween(TemporalFieldInfo)
     */
    default NumericCondition<Integer> yearsBetween(TemporalFieldInfo<N> value) {
        return getTemporalCondition().yearsBetween(value);
    }

    /**
     * See {@link TemporalCondition#yearsBetween(TemporalCondition)}
     *
     * @param value the right side value
     * @return the numeric condition
     * @see TemporalCondition#yearsBetween(TemporalCondition)
     */
    default NumericCondition<Integer> yearsBetween(TemporalCondition<N> value) {
        return getTemporalCondition().yearsBetween(value);
    }

    /**
     * See {@link TemporalCondition#yearsBetween(Supplier)}
     *
     * @param value the right side value
     * @return the numeric condition
     * @see TemporalCondition#yearsBetween(Supplier)
     */
    default NumericCondition<Integer> yearsBetween(Supplier<N> value) {
        return getTemporalCondition().yearsBetween(value);
    }

    /**
     * Returns a new temporal condition that will use this as a field.
     *
     * @return the temporal condition
     */
    TemporalCondition<N> getTemporalCondition();

    @Override
    default TemporalCondition<N> getDefaultCondition() {
        return getTemporalCondition();
    }
}
