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
package io.doov.core.dsl.impl.time;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

/**
 * Implements {@link LocalDate} functions for the temporal conditions.
 */
public class LocalDateCondition extends TemporalCondition<LocalDate> implements LocalDateOperators {

    public LocalDateCondition(DslField<LocalDate> field) {
        super(field);
    }

    public LocalDateCondition(PredicateMetadata metadata, BiFunction<DslModel, Context, Optional<LocalDate>> value) {
        super(metadata, value);
    }

    @Override
    protected TemporalCondition<LocalDate> temporalCondition(PredicateMetadata metadata,
                    BiFunction<DslModel, Context, Optional<LocalDate>> value) {
        return new LocalDateCondition(metadata, value);
    }
}
