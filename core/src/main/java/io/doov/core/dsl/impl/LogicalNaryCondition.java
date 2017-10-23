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

import static io.doov.core.dsl.meta.NaryMetadata.matchAllMetadata;
import static io.doov.core.dsl.meta.NaryMetadata.matchAnyMetadata;
import static io.doov.core.dsl.meta.NaryMetadata.matchNoneMetadata;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.BiPredicate;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.NaryMetadata;

public class LogicalNaryCondition extends AbstractStepCondition {

    private LogicalNaryCondition(NaryMetadata metadata, BiPredicate<FieldModel, Context> predicate) {
        super(metadata, predicate);
    }

    public static LogicalNaryCondition matchAny(StepCondition... steps) {
        return new LogicalNaryCondition(matchAnyMetadata(getMetadatas(steps)),
                        (model, context) -> stream(steps).anyMatch(s -> s.predicate().test(model, context)));
    }

    public static LogicalNaryCondition matchAll(StepCondition... steps) {
        return new LogicalNaryCondition(matchAllMetadata(getMetadatas(steps)),
                        (model, context) -> stream(steps).allMatch(s -> s.predicate().test(model, context)));
    }

    public static LogicalNaryCondition matchNone(StepCondition... steps) {
        return new LogicalNaryCondition(matchNoneMetadata(getMetadatas(steps)),
                        (model, context) -> stream(steps).noneMatch(s -> s.predicate().test(model, context)));
    }

    private static List<Metadata> getMetadatas(StepCondition[] steps) {
        return stream(steps).map(StepCondition::getMetadata).collect(toList());
    }

}
