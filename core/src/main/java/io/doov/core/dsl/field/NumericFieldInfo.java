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

import io.doov.core.dsl.impl.NumericCondition;
import io.doov.core.dsl.lang.StepCondition;

public interface NumericFieldInfo<F extends DefaultFieldInfo<N>, N extends Number> {

    // lesser than

    default StepCondition lesserThan(N value) {
        return getNumericCondition().lesserThan(value);
    }

    default StepCondition lesserOrEquals(N value) {
        return getNumericCondition().lesserOrEquals(value);
    }

    default StepCondition lesserThan(F field) {
        return getNumericCondition().lesserThan(field);
    }

    default StepCondition lesserOrEquals(F field) {
        return getNumericCondition().lesserOrEquals(field);
    }

    // greater than

    default StepCondition greaterThan(N value) {
        return getNumericCondition().greaterThan(value);
    }

    default StepCondition greaterThan(F field) {
        return getNumericCondition().greaterThan(field);
    }

    default StepCondition greaterOrEquals(N value) {
        return getNumericCondition().greaterOrEquals(value);
    }

    default StepCondition greaterOrEquals(F field) {
        return getNumericCondition().greaterOrEquals(field);
    }

    // between

    default StepCondition between(N minIncluded, N maxExcluded) {
        return getNumericCondition().between(minIncluded, maxExcluded);
    }

    default StepCondition between(F minIncluded, F maxExcluded) {
        return getNumericCondition().between(minIncluded, maxExcluded);
    }

    // abstract

    NumericCondition<F, N> getNumericCondition();

}
