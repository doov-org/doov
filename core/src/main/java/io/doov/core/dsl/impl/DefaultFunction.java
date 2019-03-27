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

import java.util.function.BiFunction;

import io.doov.core.FieldModel;
import io.doov.core.Try;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.field.types.ContextAccessor;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.Metadata;

public class DefaultFunction<N, M extends Metadata> implements ContextAccessor<N> {

    protected static <T> Try<T> valueModel(FieldModel model, DslField<T> field) {
        return Try.success(model.get(field.id()));
    }

    protected final M metadata;
    protected final BiFunction<FieldModel, Context, Try<N>> function;

    protected DefaultFunction(M metadata, BiFunction<FieldModel, Context, Try<N>> function) {
        this.metadata = metadata;
        this.function = function;
    }

    public Try<N> value(FieldModel model, Context context) {
        return function.apply(model, context);
    }

    public M metadata() {
        return metadata;
    }

    public BiFunction<FieldModel, Context, Try<N>> getFunction() {
        return function;
    }

    @Override
    public String readable() {
        return metadata.readable();
    }

}
