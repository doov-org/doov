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
package io.doov.core.dsl.mapping.converter;

import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.ConverterMetadata;
import io.doov.core.dsl.meta.Metadata;

public class DefaultBiTypeConverter<I, J, O> extends AbstractDSLBuilder implements BiTypeConverter<I, J, O> {

    private TriFunction<Context, Optional<I>, Optional<J>, O> function;
    private ConverterMetadata metadata;

    public DefaultBiTypeConverter(TriFunction<Context, Optional<I>, Optional<J>, O> function,
                    ConverterMetadata metadata) {
        this.function = function;
        this.metadata = metadata;
    }

    public DefaultBiTypeConverter(TriFunction<Context, Optional<I>, Optional<J>, O> converter, String description) {
        this(converter, ConverterMetadata.metadata(description));
    }

    public DefaultBiTypeConverter(BiFunction<Optional<I>, Optional<J>, O> function, String description) {
        this((c, i, j) -> function.apply(i, j), description);
    }

    @Override
    public Metadata metadata() {
        return metadata;
    }

    @Override
    public O convert(FieldModel fieldModel, Context context, I in, J in2) {
        return function.apply(context, Optional.ofNullable(in), Optional.ofNullable((in2)));
    }
}
