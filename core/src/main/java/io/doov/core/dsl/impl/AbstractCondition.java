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

import static io.doov.core.dsl.meta.LeafMetadata.fieldMetadata;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.*;

abstract class AbstractCondition<N> implements Readable {

    protected final DslField field;
    protected final PredicateMetadata metadata;
    protected final BiFunction<DslModel, DslField, Optional<N>> value;
    protected final BiFunction<DslModel, Context, Optional<N>> function;

    protected AbstractCondition(DslField field) {
        this.field = field;
        this.metadata = fieldMetadata(field);
        this.value = (model, context) -> valueModel(model, field);
        this.function = (model, context) -> valueModel(model, field);
    }

    protected AbstractCondition(DslField field, PredicateMetadata metadata,
            BiFunction<DslModel, Context, Optional<N>> function) {
        this.field = field;
        this.metadata = metadata;
        this.value = (model, f) -> function.apply(model, null);
        this.function = function;
    }

    protected Optional<N> valueModel(DslModel model, DslField field) {
        return Optional.ofNullable(model.<N> get(field.id()));
    }

    protected final Optional<N> value(DslModel model, DslField field) {
        return value.apply(model, field);
    }

    protected final StepCondition predicate(LeafMetadata metadata,
            Function<N, Boolean> predicate) {
        return new PredicateStepCondition<>(this.metadata.merge(metadata), function, predicate);
    }

    protected final StepCondition predicate(LeafMetadata metadata,
            BiFunction<DslModel, Context, Optional<N>> value,
            BiFunction<N, N, Boolean> predicate) {
        return new PredicateStepCondition<>(this.metadata.merge(metadata), function, value, predicate);
    }
    
    public PredicateMetadata getMetadata() {
        return metadata;
    }

    @Override
    public String readable() {
        return metadata.readable();
    }

}
