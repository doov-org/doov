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

import static io.doov.core.dsl.meta.FieldMetadata.emptyMetadata;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.FieldModel;
import io.doov.core.dsl.field.DefaultFieldInfo;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.FieldMetadata;

class DefaultCondition<F extends DefaultFieldInfo<N>, N> {

    final F field;
    final FieldMetadata metadata;
    final Function<FieldModel, Optional<N>> value;

    DefaultCondition(F field) {
        this.field = field;
        this.metadata = emptyMetadata();
        this.value = fieldModel -> Optional.ofNullable(fieldModel.<N> get(field.id()));
    }

    DefaultCondition(FieldMetadata metadata, Function<FieldModel, Optional<N>> value) {
        this.field = null;
        this.metadata = metadata;
        this.value = value;
    }

    Optional<N> value(FieldModel model, F field) {
        return Optional.ofNullable(model.<N> get(field.id()));
    }

    StepCondition predicate(FieldMetadata metadata, Function<N, Boolean> predicate) {
        return new PredicateStepCondition<>(this.metadata.merge(metadata), this.value, predicate);
    }

    StepCondition predicate(FieldMetadata metadata,
                    Function<FieldModel, Optional<N>> value,
                    BiFunction<N, N, Boolean> predicate) {
        return new PredicateStepCondition<>(this.metadata.merge(metadata), this.value, value, predicate);
    }

    FunctionWithMetadata<N, N> function(FieldMetadata metadata, Function<N, N> function) {
        return new FunctionWithMetadata<>(this.metadata.merge(metadata), function);
    }

}
