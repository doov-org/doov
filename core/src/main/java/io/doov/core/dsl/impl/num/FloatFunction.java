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
package io.doov.core.dsl.impl.num;

import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

public class FloatFunction extends NumericFunction<Float> implements FloatOperators {

    public FloatFunction(PredicateMetadata metadata, BiFunction<FieldModel, Context, Optional<Float>> value) {
        super(metadata, value);
    }

    @Override
    protected FloatFunction numericFunction(PredicateMetadata metadata,
                    BiFunction<FieldModel, Context, Optional<Float>> value) {
        return new FloatFunction(metadata, value);
    }

}
