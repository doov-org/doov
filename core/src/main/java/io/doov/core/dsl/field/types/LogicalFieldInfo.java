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
import io.doov.core.dsl.impl.base.BooleanFunction;
import io.doov.core.dsl.lang.StepCondition;

/**
 * Base interface for logical field info.
 * <p>
 * It contains default methods for common checks, which are calls to methods on {@link BooleanFunction}.
 */
public interface LogicalFieldInfo extends BaseFieldInfo<Boolean> {

    /**
     * See {@link BooleanFunction#not()}
     *
     * @return the step condition
     * @see BooleanFunction#not()
     */
    default StepCondition not() {
        return getBooleanFunction().not();
    }

    /**
     * See {@link BooleanFunction#and(boolean)}
     *
     * @param value the right value
     * @return the step condition
     * @see BooleanFunction#and(boolean)
     */
    default StepCondition and(boolean value) {
        return getBooleanFunction().and(value);
    }

    /**
     * See {@link BooleanFunction#and(LogicalFieldInfo)}
     *
     * @param value the right field value
     * @return the step condition
     * @see BooleanFunction#and(LogicalFieldInfo)
     */
    default StepCondition and(LogicalFieldInfo value) {
        return getBooleanFunction().and(value);
    }

    /**
     * See {@link BooleanFunction#or(boolean)}
     *
     * @param value the right value
     * @return the step condition
     * @see BooleanFunction#or(boolean)
     */
    default StepCondition or(boolean value) {
        return getBooleanFunction().or(value);
    }

    /**
     * See {@link BooleanFunction#or(LogicalFieldInfo)}
     *
     * @param value the right field value
     * @return the step condition
     * @see BooleanFunction#or(LogicalFieldInfo)
     */
    default StepCondition or(LogicalFieldInfo value) {
        return getBooleanFunction().or(value);
    }

    /**
     * See {@link BooleanFunction#isTrue()}
     *
     * @return the step condition
     * @see BooleanFunction#isTrue()
     */
    default StepCondition isTrue() {
        return getBooleanFunction().isTrue();
    }

    /**
     * See {@link BooleanFunction#isFalse()}
     *
     * @return the step condition
     * @see BooleanFunction#isFalse()
     */
    default StepCondition isFalse() {
        return getBooleanFunction().isFalse();
    }

    /**
     * Returns a new boolean condition that will use this as a field.
     *
     * @return the boolean condition
     */
    BooleanFunction getBooleanFunction();

    @Override
    default BooleanFunction getDefaultFunction() {
        return getBooleanFunction();
    }

}
