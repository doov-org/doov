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

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

public class LocalDateTimeFunction extends TemporalFunction<LocalDateTime> implements LocalDateTimeOperators {

    public LocalDateTimeFunction(PredicateMetadata metadata,
                    BiFunction<FieldModel, Context, Optional<LocalDateTime>> value) {
        super(metadata, value);
    }

    @Override
    protected LocalDateTimeFunction temporalFunction(PredicateMetadata metadata,
                    BiFunction<FieldModel, Context, Optional<LocalDateTime>> value) {
        return new LocalDateTimeFunction(metadata, value);
    }
}
