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

import io.doov.core.dsl.field.BaseFieldInfo;
import io.doov.core.dsl.impl.num.NumericFunction;
import io.doov.core.dsl.lang.StepCondition;

/**
 * Base interface for numeric field info.
 * <p>
 * It contains default methods for common checks, which are calls to methods on {@link NumericFunction}.
 *
 * @param <N> the type of the field value
 */
public interface NumericFieldInfo<N extends Number> extends BaseFieldInfo<N> {

    /**
     * See {@link NumericFunction#lesserThan(Number)}
     *
     * @param value the right side value
     * @return the step condition
     * @see NumericFunction#lesserThan(Number)
     */
    default StepCondition lesserThan(N value) {
        return getNumericFunction().lesserThan(value);
    }

    /**
     * See {@link NumericFunction#lesserThan(NumericFieldInfo)}
     *
     * @param value the right side value
     * @return the step condition
     * @see NumericFunction#lesserThan(NumericFieldInfo)
     */
    default StepCondition lesserThan(NumericFieldInfo<N> value) {
        return getNumericFunction().lesserThan(value);
    }

    /**
     * See {@link NumericFunction#lesserThan(NumericFunction)}
     *
     * @param function the right side function
     * @return the step condition
     * @see NumericFunction#lesserThan(NumericFunction)
     */
    default StepCondition lesserThan(NumericFunction<N> function) {
        return getNumericFunction().lesserThan(function);
    }

    /**
     * See {@link NumericFunction#lesserOrEquals(Number)}
     *
     * @param value the right side value
     * @return the step condition
     * @see NumericFunction#lesserOrEquals(Number)
     */
    default StepCondition lesserOrEquals(N value) {
        return getNumericFunction().lesserOrEquals(value);
    }

    /**
     * See {@link NumericFunction#lesserOrEquals(NumericFieldInfo)}
     *
     * @param value the right side value
     * @return the step condition
     * @see NumericFunction#lesserOrEquals(NumericFieldInfo)
     */
    default StepCondition lesserOrEquals(NumericFieldInfo<N> value) {
        return getNumericFunction().lesserOrEquals(value);
    }

    /**
     * See {@link NumericFunction#lesserOrEquals(NumericFunction)}
     *
     * @param function the right side function
     * @return the step condition
     * @see NumericFunction#lesserOrEquals(NumericFunction)
     */
    default StepCondition lesserOrEquals(NumericFunction<N> function) {
        return getNumericFunction().lesserOrEquals(function);
    }

    /**
     * See {@link NumericFunction#greaterThan(Number)}
     *
     * @param value the right side value
     * @return the step condition
     * @see NumericFunction#greaterThan(Number)
     */
    default StepCondition greaterThan(N value) {
        return getNumericFunction().greaterThan(value);
    }

    /**
     * See {@link NumericFunction#greaterThan(NumericFieldInfo)}
     *
     * @param value the right side value
     * @return the step condition
     * @see NumericFunction#greaterThan(NumericFieldInfo)
     */
    default StepCondition greaterThan(NumericFieldInfo<N> value) {
        return getNumericFunction().greaterThan(value);
    }

    /**
     * See {@link NumericFunction#greaterThan(NumericFunction)}
     *
     * @param function the right side function
     * @return the step condition
     * @see NumericFunction#greaterThan(NumericFunction)
     */
    default StepCondition greaterThan(NumericFunction<N> function) {
        return getNumericFunction().greaterThan(function);
    }

    /**
     * See {@link NumericFunction#greaterOrEquals(Number)}
     *
     * @param value the right side value
     * @return the step condition
     * @see NumericFunction#greaterOrEquals(Number)
     */
    default StepCondition greaterOrEquals(N value) {
        return getNumericFunction().greaterOrEquals(value);
    }

    /**
     * See {@link NumericFunction#greaterOrEquals(NumericFieldInfo)}
     *
     * @param value the right side value
     * @return the step condition
     * @see NumericFunction#greaterOrEquals(NumericFieldInfo)
     */
    default StepCondition greaterOrEquals(NumericFieldInfo<N> value) {
        return getNumericFunction().greaterOrEquals(value);
    }

    /**
     * See {@link NumericFunction#greaterOrEquals(NumericFunction)}
     *
     * @param function the right side function
     * @return the step condition
     * @see NumericFunction#greaterOrEquals(NumericFunction)
     */
    default StepCondition greaterOrEquals(NumericFunction<N> function) {
        return getNumericFunction().greaterOrEquals(function);
    }

    /**
     * See {@link NumericFunction#between(Number, Number)}
     *
     * @param minIncluded the min value included
     * @param maxExcluded the max value excluded
     * @return the step condition
     * @see NumericFunction#between(Number, Number)
     */
    default StepCondition between(N minIncluded, N maxExcluded) {
        return getNumericFunction().between(minIncluded, maxExcluded);
    }

    /**
     * See {@link NumericFunction#between(NumericFieldInfo, NumericFieldInfo)}
     *
     * @param minIncluded the min value included
     * @param maxExcluded the max value excluded
     * @return the step condition
     * @see NumericFunction#between(NumericFieldInfo, NumericFieldInfo)
     */
    default StepCondition between(NumericFieldInfo<N> minIncluded, NumericFieldInfo<N> maxExcluded) {
        return getNumericFunction().between(minIncluded, maxExcluded);
    }

    /**
     * See {@link NumericFunction#between(NumericFunction, NumericFunction)}
     *
     * @param minIncluded the min value included
     * @param maxExcluded the max value excluded
     * @return the step condition
     * @see NumericFunction#between(NumericFunction, NumericFunction)
     */
    default StepCondition between(NumericFunction<N> minIncluded, NumericFunction<N> maxExcluded) {
        return getNumericFunction().between(minIncluded, maxExcluded);
    }

    /**
     * See {@link NumericFunction#times(int)}
     *
     * @param multiplier the multiplier
     * @return the numeric function
     * @see NumericFunction#times(int)
     */
    default NumericFunction<N> times(int multiplier) {
        return getNumericFunction().times(multiplier);
    }

    /**
     * See {@link NumericFunction#plus(NumericFieldInfo)}
     *
     * @param field another field to sum
     * @return the numeric function
     * @see NumericFunction#plus(NumericFieldInfo)
     */
    default NumericFunction<N> plus(NumericFieldInfo<N> field) {
        return getNumericFunction().plus(field);
    }

    /**
     * See {@link NumericFunction#plus(NumericFieldInfo)}
     *
     * @param value to sum
     * @return the numeric function
     * @see NumericFunction#plus(NumericFieldInfo)
     */
    default NumericFunction<N> plus(N value) {
        return getNumericFunction().plus(value);
    }

    /**
     * See {@link NumericFunction#plus(NumericFunction)}
     *
     * @param function to sum
     * @return the numeric function
     * @see NumericFunction#plus(NumericFunction)
     */
    default NumericFunction<N> plus(NumericFunction<N> function) {
        return getNumericFunction().plus(function);
    }

    /**
     * See {@link NumericFunction#plus(NumericFieldInfo)}
     *
     * @param field to subtract
     * @return the numeric function
     * @see NumericFunction#plus(NumericFieldInfo)
     */
    default NumericFunction<N> minus(NumericFieldInfo<N> field) {
        return getNumericFunction().minus(field);
    }

    /**
     * See {@link NumericFunction#plus(NumericFieldInfo)}
     *
     * @param value to subtract
     * @return the numeric function
     * @see NumericFunction#plus(NumericFieldInfo)
     */
    default NumericFunction<N> minus(N value) {
        return getNumericFunction().minus(value);
    }

    /**
     * See {@link NumericFunction#plus(NumericFunction)}
     *
     * @param function to subtract
     * @return the numeric function
     * @see NumericFunction#plus(NumericFieldInfo)
     */
    default NumericFunction<N> minus(NumericFunction<N> function) {
        return getNumericFunction().minus(function);
    }

    /**
     * See {@link NumericFunction#when(StepCondition)}
     *
     * @param condition the condition to evaluate
     * @return the numeric function
     * @see NumericFunction#when(StepCondition)
     */
    default NumericFunction<N> when(StepCondition condition) {
        return getNumericFunction().when(condition);
    }

    /**
     * Returns a new numeric condition that will use this as a value.
     *
     * @return the numeric function
     */
    NumericFunction<N> getNumericFunction();

    @Override
    default NumericFunction<N> getDefaultFunction() {
        return getNumericFunction();
    }
}
