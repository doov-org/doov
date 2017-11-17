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
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.meta.FieldMetadata;
import io.doov.core.dsl.meta.Metadata;

abstract class AbstractCondition<N> implements Readable {

    protected final DslField field;
    protected final Metadata metadata;
    protected final BiFunction<DslModel, DslField, Optional<N>> value;
    protected final BiFunction<DslModel, Context, Optional<N>> function;

    protected AbstractCondition(DslField field) {
        this.field = field;
        this.metadata = emptyMetadata();
        this.value = null;
        this.function = (model, context) -> value(model, field);
    }

    protected AbstractCondition(Metadata metadata,
                    BiFunction<DslModel, Context, Optional<N>> function) {
        this.field = null;
        this.metadata = metadata;
        this.value = (model, field) -> function.apply(model, null);
        this.function = function;
    }

    protected AbstractCondition(DslField field, Metadata metadata,
                    BiFunction<DslModel, Context, Optional<N>> function) {
        this.field = field;
        this.metadata = metadata;
        this.value = null;
        this.function = function;
    }

    protected Optional<N> value(DslModel model, DslField field) {
        return value == null ? Optional.ofNullable(model.<N> get(field.id())) : value.apply(model, field);
    }

    // TODO move to builder
    protected StepCondition predicate(FieldMetadata metadata,
                    Function<N, Boolean> predicate) {
        return new PredicateStepCondition<>(this.metadata.merge(metadata), function, predicate);
    }

    // TODO move to builder
    protected StepCondition predicate(FieldMetadata metadata,
                    BiFunction<DslModel, Context, Optional<N>> value,
                    BiFunction<N, N, Boolean> predicate) {
        return new PredicateStepCondition<>(this.metadata.merge(metadata), function, value, predicate);
    }

    @Override
    public String readable() {
        return metadata.readable();
    }

}
