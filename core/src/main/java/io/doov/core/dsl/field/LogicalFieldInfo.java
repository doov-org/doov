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
package io.doov.core.dsl.field;

import io.doov.core.dsl.impl.BooleanCondition;
import io.doov.core.dsl.lang.StepCondition;

public interface LogicalFieldInfo extends BaseFieldInfo<Boolean> {

    // not

    default StepCondition not() {
        return getBooleanCondition().not();
    }

    // and

    default StepCondition and(boolean value) {
        return getBooleanCondition().and(value);
    }

    default StepCondition and(LogicalFieldInfo value) {
        return getBooleanCondition().and(value);
    }

    // or

    default StepCondition or(boolean value) {
        return getBooleanCondition().or(value);
    }

    default StepCondition or(LogicalFieldInfo value) {
        return getBooleanCondition().or(value);
    }

    // xor

    default StepCondition xor(boolean value) {
        return getBooleanCondition().xor(value);
    }

    default StepCondition xor(LogicalFieldInfo value) {
        return getBooleanCondition().xor(value);
    }

    // true

    default StepCondition isTrue() {
        return getBooleanCondition().isTrue();
    }

    // false

    default StepCondition isFalse() {
        return getBooleanCondition().isFalse();
    }

    // implementation

    BooleanCondition getBooleanCondition();

}
