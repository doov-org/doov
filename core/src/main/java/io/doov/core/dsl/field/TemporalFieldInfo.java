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

import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.function.Supplier;

import io.doov.core.dsl.impl.*;
import io.doov.core.dsl.lang.StepCondition;

public interface TemporalFieldInfo<F extends DefaultFieldInfo<N>, N extends Temporal> {

    // minus plus

    default FunctionWithMetadata<N, N> minus(int value, TemporalUnit unit) {
        return getTemporalCondition().minus(value, unit);
    }

    default FunctionWithMetadata<N, N> plus(int value, TemporalUnit unit) {
        return getTemporalCondition().plus(value, unit);
    }

    // before

    default StepCondition before(N value) {
        return getTemporalCondition().before(value);
    }

    default StepCondition before(F value) {
        return getTemporalCondition().before(value);
    }

    default StepCondition before(Supplier<N> value) {
        return getTemporalCondition().before(value);
    }

    default StepCondition before(FunctionWithMetadata<N, N> value) {
        return getTemporalCondition().before(value);
    }

    default StepCondition beforeOrEq(N value) {
        return getTemporalCondition().beforeOrEq(value);
    }

    // after

    default StepCondition after(N value) {
        return getTemporalCondition().after(value);
    }

    default StepCondition after(F value) {
        return getTemporalCondition().after(value);
    }

    default StepCondition after(Supplier<N> value) {
        return getTemporalCondition().after(value);
    }

    default StepCondition after(FunctionWithMetadata<N, N> value) {
        return getTemporalCondition().after(value);
    }

    default StepCondition afterOrEq(N value) {
        return getTemporalCondition().afterOrEq(value);
    }

    // beetween

    default StepCondition between(N minValue, N maxValue) {
        return getTemporalCondition().between(minValue, maxValue);
    }

    default StepCondition notBetween(N minValue, N maxValue) {
        return getTemporalCondition().notBetween(minValue, maxValue);
    }

    // age

    default NumericCondition<LongFieldInfo, Long> ageAt(N value) {
        return getTemporalCondition().ageAt(value);
    }

    default NumericCondition<LongFieldInfo, Long> ageAt(F value) {
        return getTemporalCondition().ageAt(value);
    }

    default NumericCondition<LongFieldInfo, Long> ageAt(Supplier<N> value) {
        return getTemporalCondition().ageAt(value);
    }

    // abstract

    TemporalCondition<F, N> getTemporalCondition();

}
