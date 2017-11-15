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

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.Metadata;

public class StepFunction<N> {

    final Metadata metadata;
    final BiFunction<DslModel, Context, N> function;

    StepFunction(Metadata metadata,
                    BiFunction<DslModel, Context, Optional<N>> value,
                    Function<N, N> function) {
        this.metadata = metadata;
        this.function = (model, context) -> value.apply(model, context).map(function).orElse(null);
    }

    <T> StepFunction(Metadata metadata,
                    BiFunction<DslModel, Context, Optional<N>> left,
                    BiFunction<DslModel, Context, Optional<T>> right,
                    BiFunction<N, T, N> function) {
        this.metadata = metadata;
        this.function = (model, context) ->
                        left.apply(model, context)
                                        .flatMap(l -> right.apply(model, context).map(r -> function.apply(l, r)))
                                        .orElse(null);
    }

}
