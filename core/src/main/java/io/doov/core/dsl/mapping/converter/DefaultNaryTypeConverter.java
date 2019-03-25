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

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.ConverterMetadata;
import io.doov.core.dsl.meta.Metadata;

public class DefaultNaryTypeConverter<O> extends AbstractDSLBuilder implements NaryTypeConverter<O> {

    private final Function3<FieldModel, Context, List<DslField<?>>, O> function;
    private final ConverterMetadata metadata;

    public DefaultNaryTypeConverter(Function3<FieldModel, Context, List<DslField<?>>, O> function,
                    ConverterMetadata metadata) {
        this.function = function;
        this.metadata = metadata;
    }

    public DefaultNaryTypeConverter(Function3<FieldModel, Context, List<DslField<?>>, O> function, String description) {
        this(function, ConverterMetadata.metadata(description));
    }

    public DefaultNaryTypeConverter(BiFunction<FieldModel, List<DslField<?>>, O> function, String description) {
        this((m, c, f) -> function.apply(m, f), description);
    }

    @Override
    public Metadata metadata() {
        return metadata;
    }

    @Override
    public O convert(FieldModel dslModel, Context context, DslField<?>... fieldInfos) {
        return function.apply(dslModel, context, Arrays.asList(fieldInfos));
    }
}
