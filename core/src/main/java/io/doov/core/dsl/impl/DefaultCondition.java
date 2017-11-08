/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package io.doov.core.dsl.impl;

import static io.doov.core.dsl.meta.FieldMetadata.emptyMetadata;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.FieldMetadata;
import io.doov.core.dsl.meta.Metadata;

class DefaultCondition<N> {

    final DslField field;
    final Metadata metadata;
    final BiFunction<DslModel, Context, Optional<N>> value;

    DefaultCondition(DslField field) {
        this.field = field;
        metadata = emptyMetadata();
        value = (model, context) -> value(model, field);
    }

    DefaultCondition(Metadata metadata, BiFunction<DslModel, Context, Optional<N>> value) {
        field = null;
        this.metadata = metadata;
        this.value = value;
    }

    protected Optional<N> value(DslModel model, DslField field) {
        return Optional.ofNullable(model.<N> get(field.id()));
    }

    StepCondition predicate(FieldMetadata metadata,
                    Function<N, Boolean> predicate) {
        return new PredicateStepCondition<>(this.metadata.merge(metadata), value, predicate);
    }

    StepCondition predicate(FieldMetadata metadata,
                    BiFunction<DslModel, Context, Optional<N>> value,
                    BiFunction<N, N, Boolean> predicate) {
        return new PredicateStepCondition<>(this.metadata.merge(metadata), this.value, value, predicate);
    }

    <T> StepFunction<N, T> function(FieldMetadata metadata,
                    Function<N, N> function) {
        return new StepFunction<>(this.metadata.merge(metadata), value, function);
    }

    <T> StepFunction<N, T> function(FieldMetadata metadata,
                    BiFunction<DslModel, Context, Optional<T>> value,
                    BiFunction<N, T, N> function) {
        return new StepFunction<>(this.metadata.merge(metadata), this.value, value, function);
    }

}
