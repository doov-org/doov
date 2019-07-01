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
package io.doov.core.dsl.field.types;

import static java.time.temporal.ChronoUnit.YEARS;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.function.Supplier;

import io.doov.core.dsl.field.BaseFieldInfo;
import io.doov.core.dsl.impl.num.NumericFunction;
import io.doov.core.dsl.impl.time.TemporalFunction;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.time.TemporalAdjuster;

/**
 * Base interface for temporal field info.
 * <p>
 * It contains default methods for common checks, which are calls to methods on {@link TemporalFunction}.
 *
 * @param <N> the type of the field value
 */
public interface TemporalFieldInfo<N extends Temporal> extends BaseFieldInfo<N> {

    /**
     * See {@link TemporalFunction#eq(TemporalFunction)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalFunction#eq(TemporalFunction)
     */
    default StepCondition eq(TemporalFunction<N> value) {
        return getTemporalFunction().eq(value);
    }

    /**
     * See {@link TemporalFunction#with(TemporalAdjuster)}
     *
     * @param adjuster the adjuster
     * @return the step condition
     * @see TemporalFunction#with(TemporalAdjuster)
     */
    default TemporalFunction<N> with(TemporalAdjuster adjuster) {
        return getTemporalFunction().with(adjuster);
    }

    /**
     * See {@link TemporalFunction#minus(int, TemporalUnit)}
     *
     * @param value the minus value
     * @param unit the minus unit
     * @return the step condition
     * @see TemporalFunction#minus(int, TemporalUnit)
     */
    default TemporalFunction<N> minus(int value, TemporalUnit unit) {
        return getTemporalFunction().minus(value, unit);
    }

    /**
     * See {@link TemporalFunction#minus(NumericFieldInfo, TemporalUnit)}
     *
     * @param value the minus field value
     * @param unit the minus unit
     * @return the step condition
     * @see TemporalFunction#minus(NumericFieldInfo, TemporalUnit)
     */
    default TemporalFunction<N> minus(NumericFieldInfo<Integer> value, TemporalUnit unit) {
        return getTemporalFunction().minus(value, unit);
    }

    /**
     * See {@link TemporalFunction#minus(NumericFunction, TemporalUnit)}
     *
     * @param function the minus function
     * @param unit the minus unit
     * @return the step condition
     * @see TemporalFunction#minus(NumericFunction, TemporalUnit)
     */
    default TemporalFunction<N> minus(NumericFunction<Integer> function, TemporalUnit unit) {
        return getTemporalFunction().minus(function, unit);
    }

    /**
     * See {@link TemporalFunction#minus(NumericFieldInfo, TemporalUnit)}
     *
     * @param value the years to remove
     * @return the step condition
     * @see TemporalFunction#minus(NumericFieldInfo, TemporalUnit)
     */
    default TemporalFunction<N> minusYears(int value) {
        return getTemporalFunction().minus(value, YEARS);
    }

    /**
     * See {@link TemporalFunction#plus(int, TemporalUnit)}
     *
     * @param value the plus value
     * @param unit the plus unit
     * @return the step condition
     * @see TemporalFunction#plus(int, TemporalUnit)
     */
    default TemporalFunction<N> plus(int value, TemporalUnit unit) {
        return getTemporalFunction().plus(value, unit);
    }

    /**
     * See {@link TemporalFunction#plus(NumericFieldInfo, TemporalUnit)}
     *
     * @param value the plus field value
     * @param unit the plus unit
     * @return the step condition
     * @see TemporalFunction#plus(NumericFieldInfo, TemporalUnit)
     */
    default TemporalFunction<N> plus(NumericFieldInfo<Integer> value, TemporalUnit unit) {
        return getTemporalFunction().plus(value, unit);
    }

    /**
     * See {@link TemporalFunction#plus(NumericFunction, TemporalUnit)}
     *
     * @param function the plus function
     * @param unit the plus unit
     * @return the step condition
     * @see TemporalFunction#plus(NumericFunction, TemporalUnit)
     */
    default TemporalFunction<N> plus(NumericFunction<Integer> function, TemporalUnit unit) {
        return getTemporalFunction().plus(function, unit);
    }

    /**
     * See {@link TemporalFunction#plus(int, TemporalUnit)}
     *
     * @param value the years to add
     * @return the step condition
     * @see TemporalFunction#plus(int, TemporalUnit)
     */
    default TemporalFunction<N> plusYears(int value) {
        return getTemporalFunction().plus(value, YEARS);
    }

    /**
     * See {@link TemporalFunction#before(Temporal)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalFunction#before(Temporal)
     */
    default StepCondition before(N value) {
        return getTemporalFunction().before(value);
    }

    /**
     * See {@link TemporalFunction#before(TemporalFieldInfo)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalFunction#before(TemporalFieldInfo)
     */
    default StepCondition before(TemporalFieldInfo<N> value) {
        return getTemporalFunction().before(value);
    }

    /**
     * See {@link TemporalFunction#before(Supplier)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalFunction#before(Supplier)
     */
    default StepCondition before(Supplier<N> value) {
        return getTemporalFunction().before(value);
    }

    /**
     * See {@link TemporalFunction#before(TemporalFunction)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalFunction#before(TemporalFunction)
     */
    default StepCondition before(TemporalFunction<N> value) {
        return getTemporalFunction().before(value);
    }

    /**
     * See {@link TemporalFunction#beforeOrEq(Temporal)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalFunction#beforeOrEq(Temporal)
     */
    default StepCondition beforeOrEq(N value) {
        return getTemporalFunction().beforeOrEq(value);
    }

    /**
     * See {@link TemporalFunction#beforeOrEq(Temporal)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalFunction#beforeOrEq(Temporal)
     */
    default StepCondition beforeOrEq(TemporalFieldInfo<N> value) {
        return getTemporalFunction().beforeOrEq(value);
    }
    
    /**
     * See {@link TemporalFunction#beforeOrEq(Supplier)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalFunction#beforeOrEq(Supplier)
     */
    default StepCondition beforeOrEq(Supplier<N> value) {
        return getTemporalFunction().beforeOrEq(value);
    }

    /**
     * See {@link TemporalFunction#beforeOrEq(TemporalFunction)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalFunction#beforeOrEq(TemporalFunction)
     */
    default StepCondition beforeOrEq(TemporalFunction<N> value) {
        return getTemporalFunction().beforeOrEq(value);
    }

    /**
     * See {@link TemporalFunction#after(Temporal)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalFunction#after(Temporal)
     */
    default StepCondition after(N value) {
        return getTemporalFunction().after(value);
    }

    /**
     * See {@link TemporalFunction#after(TemporalFieldInfo)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalFunction#after(TemporalFieldInfo)
     */
    default StepCondition after(TemporalFieldInfo<N> value) {
        return getTemporalFunction().after(value);
    }

    /**
     * See {@link TemporalFunction#after(Supplier)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalFunction#after(Supplier)
     */
    default StepCondition after(Supplier<N> value) {
        return getTemporalFunction().after(value);
    }

    /**
     * See {@link TemporalFunction#after(TemporalFunction)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalFunction#after(TemporalFunction)
     */
    default StepCondition after(TemporalFunction<N> value) {
        return getTemporalFunction().after(value);
    }

    /**
     * See {@link TemporalFunction#afterOrEq(Temporal)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalFunction#afterOrEq(Temporal)
     */
    default StepCondition afterOrEq(N value) {
        return getTemporalFunction().afterOrEq(value);
    }
    
    /**
     * See {@link TemporalFunction#afterOrEq(Temporal)}
     *
     * @param field the right side value
     * @return the step condition
     * @see TemporalFunction#afterOrEq(TemporalFieldInfo)
     */
    default StepCondition afterOrEq(TemporalFieldInfo<N> field) {
        return getTemporalFunction().afterOrEq(field);
    }

    /**
     * See {@link TemporalFunction#afterOrEq(Supplier)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalFunction#afterOrEq(Supplier)
     */
    default StepCondition afterOrEq(Supplier<N> value) {
        return getTemporalFunction().afterOrEq(value);
    }

    /**
     * See {@link TemporalFunction#afterOrEq(TemporalFunction)}
     *
     * @param value the right side value
     * @return the step condition
     * @see TemporalFunction#afterOrEq(TemporalFunction)
     */
    default StepCondition afterOrEq(TemporalFunction<N> value) {
        return getTemporalFunction().afterOrEq(value);
    }

    /**
     * See {@link TemporalFunction#between(Temporal, Temporal)}
     *
     * @param minIncluded the min value included
     * @param maxExcluded the max value excluded
     * @return the step condition
     * @see TemporalFunction#between(Temporal, Temporal)
     */
    default StepCondition between(N minIncluded, N maxExcluded) {
        return getTemporalFunction().between(minIncluded, maxExcluded);
    }

    /**
     * See {@link TemporalFunction#between(Supplier, Supplier)}
     *
     * @param minIncluded the min value included
     * @param maxExcluded the max value excluded
     * @return the step condition
     * @see TemporalFunction#between(Supplier, Supplier)
     */
    default StepCondition between(Supplier<N> minIncluded, Supplier<N> maxExcluded) {
        return getTemporalFunction().between(minIncluded, maxExcluded);
    }

    /**
     * See {@link TemporalFunction#notBetween(Temporal, Temporal)}
     *
     * @param minIncluded the min value included
     * @param maxExcluded the max value excluded
     * @return the step condition
     * @see TemporalFunction#notBetween(Temporal, Temporal)
     */
    default StepCondition notBetween(N minIncluded, N maxExcluded) {
        return getTemporalFunction().notBetween(minIncluded, maxExcluded);
    }

    /**
     * See {@link TemporalFunction#ageAt(Temporal)}
     *
     * @param value the right side value
     * @return the numeric function
     * @see TemporalFunction#ageAt(Temporal)
     */
    default NumericFunction<Integer> ageAt(N value) {
        return getTemporalFunction().ageAt(value);
    }

    /**
     * See {@link TemporalFunction#ageAt(TemporalFieldInfo)}
     *
     * @param value the right side value
     * @return the numeric function
     * @see TemporalFunction#ageAt(TemporalFieldInfo)
     */
    default NumericFunction<Integer> ageAt(TemporalFieldInfo<N> value) {
        return getTemporalFunction().ageAt(value);
    }

    /**
     * See {@link TemporalFunction#ageAt(TemporalFunction)}
     *
     * @param value the right side value
     * @return the numeric function
     * @see TemporalFunction#ageAt(TemporalFunction)
     */
    default NumericFunction<Integer> ageAt(TemporalFunction<N> value) {
        return getTemporalFunction().ageAt(value);
    }

    /**
     * See {@link TemporalFunction#ageAt(Supplier)}
     *
     * @param value the right side value
     * @return the numeric function
     * @see TemporalFunction#ageAt(Supplier)
     */
    default NumericFunction<Integer> ageAt(Supplier<N> value) {
        return getTemporalFunction().ageAt(value);
    }

    /**
     * See {@link TemporalFunction#daysBetween(Temporal)}
     *
     * @param value the right side value
     * @return the numeric function
     * @see TemporalFunction#daysBetween(Temporal)
     */
    default NumericFunction<Integer> daysBetween(N value) {
        return getTemporalFunction().daysBetween(value);
    }

    /**
     * See {@link TemporalFunction#daysBetween(TemporalFieldInfo)}
     *
     * @param value the right side value
     * @return the numeric function
     * @see TemporalFunction#daysBetween(TemporalFieldInfo)
     */
    default NumericFunction<Integer> daysBetween(TemporalFieldInfo<N> value) {
        return getTemporalFunction().daysBetween(value);
    }

    /**
     * See {@link TemporalFunction#daysBetween(TemporalFunction)}
     *
     * @param value the right side value
     * @return the numeric function
     * @see TemporalFunction#daysBetween(TemporalFunction)
     */
    default NumericFunction<Integer> daysBetween(TemporalFunction<N> value) {
        return getTemporalFunction().daysBetween(value);
    }

    /**
     * See {@link TemporalFunction#daysBetween(Supplier)}
     *
     * @param value the right side value
     * @return the numeric function
     * @see TemporalFunction#daysBetween(Supplier)
     */
    default NumericFunction<Integer> daysBetween(Supplier<N> value) {
        return getTemporalFunction().daysBetween(value);
    }

    /**
     * See {@link TemporalFunction#monthsBetween(Temporal)}
     *
     * @param value the right side value
     * @return the numeric function
     * @see TemporalFunction#monthsBetween(Temporal)
     */
    default NumericFunction<Integer> monthsBetween(N value) {
        return getTemporalFunction().monthsBetween(value);
    }

    /**
     * See {@link TemporalFunction#monthsBetween(TemporalFieldInfo)}
     *
     * @param value the right side value
     * @return the numeric function
     * @see TemporalFunction#monthsBetween(TemporalFieldInfo)
     */
    default NumericFunction<Integer> monthsBetween(TemporalFieldInfo<N> value) {
        return getTemporalFunction().monthsBetween(value);
    }

    /**
     * See {@link TemporalFunction#monthsBetween(TemporalFunction)}
     *
     * @param value the right side value
     * @return the numeric function
     * @see TemporalFunction#monthsBetween(TemporalFunction)
     */
    default NumericFunction<Integer> monthsBetween(TemporalFunction<N> value) {
        return getTemporalFunction().monthsBetween(value);
    }

    /**
     * See {@link TemporalFunction#monthsBetween(Supplier)}
     *
     * @param value the right side value
     * @return the numeric function
     * @see TemporalFunction#monthsBetween(Supplier)
     */
    default NumericFunction<Integer> monthsBetween(Supplier<N> value) {
        return getTemporalFunction().monthsBetween(value);
    }

    /**
     * See {@link TemporalFunction#yearsBetween(Temporal)}
     *
     * @param value the right side value
     * @return the numeric function
     * @see TemporalFunction#yearsBetween(Temporal)
     */
    default NumericFunction<Integer> yearsBetween(N value) {
        return getTemporalFunction().yearsBetween(value);
    }

    /**
     * See {@link TemporalFunction#yearsBetween(TemporalFieldInfo)}
     *
     * @param value the right side value
     * @return the numeric function
     * @see TemporalFunction#yearsBetween(TemporalFieldInfo)
     */
    default NumericFunction<Integer> yearsBetween(TemporalFieldInfo<N> value) {
        return getTemporalFunction().yearsBetween(value);
    }

    /**
     * See {@link TemporalFunction#yearsBetween(TemporalFunction)}
     *
     * @param value the right side value
     * @return the numeric function
     * @see TemporalFunction#yearsBetween(TemporalFunction)
     */
    default NumericFunction<Integer> yearsBetween(TemporalFunction<N> value) {
        return getTemporalFunction().yearsBetween(value);
    }

    /**
     * See {@link TemporalFunction#yearsBetween(Supplier)}
     *
     * @param value the right side value
     * @return the numeric function
     * @see TemporalFunction#yearsBetween(Supplier)
     */
    default NumericFunction<Integer> yearsBetween(Supplier<N> value) {
        return getTemporalFunction().yearsBetween(value);
    }

    /**
     * Returns a new temporal function that will use this as a field.
     *
     * @return the temporal function
     */
    TemporalFunction<N> getTemporalFunction();

    @Override
    default TemporalFunction<N> getDefaultFunction() {
        return getTemporalFunction();
    }
}
