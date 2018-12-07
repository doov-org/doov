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

import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.field.types.Function;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.Metadata;

abstract class AbstractFunction<N, M extends Metadata> implements Function<N> {

    protected static <T> Optional<T> valueModel(DslModel model, DslField<T> field) {
        return Optional.ofNullable(model.get(field.id()));
    }

    protected final M metadata;
    protected final BiFunction<DslModel, Context, Optional<N>> function;

    protected AbstractFunction(M metadata, BiFunction<DslModel, Context, Optional<N>> function) {
        this.metadata = metadata;
        this.function = function;
    }

    public Optional<N> value(DslModel model, Context context) {
        return function.apply(model, context);
    }

    public M getMetadata() {
        return metadata;
    }

    public BiFunction<DslModel, Context, Optional<N>> getFunction() {
        return function;
    }

    @Override
    public String readable() {
        return metadata.readable();
    }

}
