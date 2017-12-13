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
package io.doov.core.dsl;

import static io.doov.core.dsl.meta.FieldMetadata.falseMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.trueMetadata;
import static java.util.Arrays.asList;

import java.util.Arrays;
import java.util.Objects;

import io.doov.core.dsl.field.NumericFieldInfo;
import io.doov.core.dsl.impl.*;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.lang.StepWhen;

public class DOOV {

    private DOOV() {
        // static
    }

    // when

    public static StepWhen when(StepCondition condition) {
        return new DefaultStepWhen(condition);
    }

    // always

    public static StepCondition alwaysTrue() {
        return new DefaultStepCondition(trueMetadata(), (model, context) -> true);
    }

    public static StepCondition alwaysFalse() {
        return new DefaultStepCondition(falseMetadata(), (model, context) -> false);
    }

    // count

    public static IntegerCondition count(StepCondition... steps) {
        return LogicalNaryCondition.count(asList(steps));
    }

    // match

    public static StepCondition matchAny(StepCondition... steps) {
        return LogicalNaryCondition.matchAny(asList(steps));
    }

    public static StepCondition matchAll(StepCondition... steps) {
        return LogicalNaryCondition.matchAll(asList(steps));
    }

    public static StepCondition matchNone(StepCondition... steps) {
        return LogicalNaryCondition.matchNone(asList(steps));
    }

    // min

    @SafeVarargs
    public static <N extends Number> NumericCondition<N> min(NumericFieldInfo<N>... fields) {
        return Arrays.stream(fields)
                .filter(Objects::nonNull)
                .findFirst()
                .map(NumericFieldInfo::getNumericCondition)
                .map(c -> c.min(Arrays.asList(fields)))
                .orElseThrow(IllegalArgumentException::new);
    }

    // sum

    @SafeVarargs
    public static <N extends Number> NumericCondition<N> sum(NumericFieldInfo<N>... fields) {
        return Arrays.stream(fields)
                .filter(Objects::nonNull)
                .findFirst()
                .map(NumericFieldInfo::getNumericCondition)
                .map(c -> c.sum(Arrays.asList(fields)))
                .orElseThrow(IllegalArgumentException::new);
    }

    @SafeVarargs
    public static <N extends Number> NumericCondition<N> sum(NumericCondition<N>... conditions) {
        return Arrays.stream(conditions)
                .filter(Objects::nonNull)
                .findFirst()
                .map(c -> c.sumConditions(Arrays.asList(conditions)))
                .orElseThrow(IllegalArgumentException::new);
    }

}
