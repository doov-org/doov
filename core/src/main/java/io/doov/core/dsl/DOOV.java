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

import static io.doov.core.dsl.meta.FieldMetadata.minMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.sumMetadata;
import static java.util.Comparator.naturalOrder;
import static java.util.Objects.nonNull;

import java.util.Arrays;
import java.util.Optional;

import io.doov.core.dsl.field.IntegerFieldInfo;
import io.doov.core.dsl.impl.*;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.lang.StepWhen;
import io.doov.core.dsl.meta.FieldMetadata;

public class DOOV {

    public static StepWhen when(StepCondition condition) {
        return new DefaultStepWhen(condition);
    }

    public static StepCondition alwaysTrue() {
        return new DefaultStepCondition(FieldMetadata.trueMetadata(), (model, context) -> true);
    }

    public static IntegerCondition count(StepCondition... steps) {
        return LogicalNaryCondition.count(steps);
    }

    public static StepCondition matchAny(StepCondition... steps) {
        return LogicalNaryCondition.matchAny(steps);
    }

    public static StepCondition matchAll(StepCondition... steps) {
        return LogicalNaryCondition.matchAll(steps);
    }

    public static StepCondition matchNone(StepCondition... steps) {
        return LogicalNaryCondition.matchNone(steps);
    }

    public static IntegerCondition min(IntegerFieldInfo... fields) {
        return new IntegerCondition(
                        minMetadata(fields),
                        (model, context) -> Arrays.stream(fields)
                                        .filter(f -> nonNull(model.<Integer> get(f.id())))
                                        .map(f -> model.<Integer> get(f.id()))
                                        .min(naturalOrder()));
    }

    public static IntegerCondition sum(IntegerFieldInfo... fields) {
        return new IntegerCondition(
                        sumMetadata(fields),
                        (model, context) -> Optional.of(Arrays.stream(fields)
                                        .filter(f -> nonNull(model.<Integer> get(f.id())))
                                        .mapToInt(f -> model.<Integer> get(f.id()))
                                        .sum()));
    }

}
