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
package io.doov.core.dsl.impl;

import static io.doov.core.dsl.meta.NaryMetadata.countMetadata;
import static io.doov.core.dsl.meta.NaryMetadata.matchAllMetadata;
import static io.doov.core.dsl.meta.NaryMetadata.matchAnyMetadata;
import static io.doov.core.dsl.meta.NaryMetadata.matchNoneMetadata;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;

import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.NaryMetadata;

public class LogicalNaryCondition extends AbstractStepCondition {

    private LogicalNaryCondition(NaryMetadata metadata, BiPredicate<DslModel, Context> predicate) {
        super(metadata, predicate);
    }

    // count

    public static IntegerCondition count(List<StepCondition> steps) {
        return new IntegerCondition(null, countMetadata(getMetadatas(steps)),
                (model, context) -> Optional.of((int) steps.stream()
                        .filter(s -> s.predicate().test(model, context))
                        .count()));
    }

    // match any

    public static LogicalNaryCondition matchAny(List<StepCondition> steps) {
        return new LogicalNaryCondition(matchAnyMetadata(getMetadatas(steps)),
                (model, context) -> context.isShortCircuit()
                        ? matchAnyShortCircuit(steps, model, context)
                        : matchAny(steps, model, context));
    }

    private static boolean matchAnyShortCircuit(List<StepCondition> steps, DslModel model, Context context) {
        return steps.stream().anyMatch(s -> s.predicate().test(model, context));
    }

    private static boolean matchAny(List<StepCondition> steps, DslModel model, Context context) {
        List<Boolean> results = steps.stream().map(s -> s.predicate().test(model, context)).collect(toList());
        return results.stream().anyMatch(Boolean::booleanValue);
    }

    // match all

    public static LogicalNaryCondition matchAll(List<StepCondition> steps) {
        return new LogicalNaryCondition(matchAllMetadata(getMetadatas(steps)),
                (model, context) -> context.isShortCircuit()
                        ? matchAllShortCircuit(steps, model, context)
                        : matchAll(steps, model, context));
    }

    private static boolean matchAllShortCircuit(List<StepCondition> steps, DslModel model, Context context) {
        return steps.stream().allMatch(s -> s.predicate().test(model, context));
    }

    private static boolean matchAll(List<StepCondition> steps, DslModel model, Context context) {
        List<Boolean> results = steps.stream().map(s -> s.predicate().test(model, context)).collect(toList());
        return results.stream().allMatch(Boolean::booleanValue);
    }

    // match none

    public static LogicalNaryCondition matchNone(List<StepCondition> steps) {
        return new LogicalNaryCondition(matchNoneMetadata(getMetadatas(steps)),
                (model, context) -> context.isShortCircuit()
                        ? matchNoneShortCircuit(steps, model, context)
                        : matchNone(steps, model, context));
    }

    private static boolean matchNoneShortCircuit(List<StepCondition> steps, DslModel model, Context context) {
        return steps.stream().noneMatch(s -> s.predicate().test(model, context));
    }

    private static boolean matchNone(List<StepCondition> steps, DslModel model, Context context) {
        List<Boolean> results = steps.stream().map(s -> s.predicate().test(model, context)).collect(toList());
        return results.stream().noneMatch(Boolean::booleanValue);
    }

    // static

    private static List<Metadata> getMetadatas(List<StepCondition> steps) {
        return steps.stream().map(StepCondition::getMetadata).collect(toList());
    }

}
