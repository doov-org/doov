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
import io.doov.core.dsl.impl.BooleanCondition;
import io.doov.core.dsl.lang.StepCondition;

/**
 * Base interface for logical field info.
 * <p>
 * It contains default methods for common checks, which are calls to methods on {@link BooleanCondition}.
 */
public interface LogicalFieldInfo extends BaseFieldInfo<Boolean> {

    /**
     * See {@link BooleanCondition#not()}
     *
     * @return the step condition
     * @see BooleanCondition#not()
     */
    default StepCondition not() {
        return getBooleanCondition().not();
    }

    /**
     * See {@link BooleanCondition#and(boolean)}
     *
     * @param value the right value
     * @return the step condition
     * @see BooleanCondition#and(boolean)
     */
    default StepCondition and(boolean value) {
        return getBooleanCondition().and(value);
    }

    /**
     * See {@link BooleanCondition#and(LogicalFieldInfo)}
     *
     * @param value the right field value
     * @return the step condition
     * @see BooleanCondition#and(LogicalFieldInfo)
     */
    default StepCondition and(LogicalFieldInfo value) {
        return getBooleanCondition().and(value);
    }

    /**
     * See {@link BooleanCondition#or(boolean)}
     *
     * @param value the right value
     * @return the step condition
     * @see BooleanCondition#or(boolean)
     */
    default StepCondition or(boolean value) {
        return getBooleanCondition().or(value);
    }

    /**
     * See {@link BooleanCondition#or(LogicalFieldInfo)}
     *
     * @param value the right field value
     * @return the step condition
     * @see BooleanCondition#or(LogicalFieldInfo)
     */
    default StepCondition or(LogicalFieldInfo value) {
        return getBooleanCondition().or(value);
    }

    /**
     * See {@link BooleanCondition#xor(boolean)}
     *
     * @param value the right value
     * @return the step condition
     * @see BooleanCondition#xor(boolean)
     */
    default StepCondition xor(boolean value) {
        return getBooleanCondition().xor(value);
    }

    /**
     * See {@link BooleanCondition#xor(LogicalFieldInfo)}
     *
     * @param value the right field value
     * @return the step condition
     * @see BooleanCondition#xor(LogicalFieldInfo)
     */
    default StepCondition xor(LogicalFieldInfo value) {
        return getBooleanCondition().xor(value);
    }

    /**
     * See {@link BooleanCondition#isTrue()}
     *
     * @return the step condition
     * @see BooleanCondition#isTrue()
     */
    default StepCondition isTrue() {
        return getBooleanCondition().isTrue();
    }

    /**
     * See {@link BooleanCondition#isFalse()}
     *
     * @return the step condition
     * @see BooleanCondition#isFalse()
     */
    default StepCondition isFalse() {
        return getBooleanCondition().isFalse();
    }

    /**
     * Returns a new boolean condition that will use this as a field.
     *
     * @return the boolean condition
     */
    BooleanCondition getBooleanCondition();

}
