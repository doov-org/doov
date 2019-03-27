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
package io.doov.core.dsl.mapping;

import static io.doov.core.dsl.meta.MappingInputMetadata.inputMetadata;
import static io.doov.core.dsl.meta.MappingMetadata.metadataInput;

import io.doov.core.FieldModel;
import io.doov.core.Single;
import io.doov.core.dsl.field.types.ContextAccessor;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.MappingInputMetadata;
import io.doov.core.dsl.meta.Metadata;

public class BiConverterInput<U, S, T> implements ContextAccessor<T> {

    private final MappingInputMetadata metadata;
    private final ContextAccessor<U> mappingInput1;
    private final ContextAccessor<S> mappingInput2;
    private final BiTypeConverter<U, S, T> converter;

    public BiConverterInput(
            ContextAccessor<U> mappingInput1,
            ContextAccessor<S> mappingInput2,
            BiTypeConverter<U, S, T> converter
    ) {
        this.mappingInput1 = mappingInput1;
        this.mappingInput2 = mappingInput2;
        this.converter = converter;
        this.metadata = inputMetadata(metadataInput(mappingInput1.metadata(), mappingInput2.metadata()), converter.metadata());
    }

    @Override
    public Metadata metadata() {
        return metadata;
    }

    @Override
    public Single<T> value(FieldModel model, Context context) {
        return Single.combine(
                (l,r) -> converter.convert(model,context,l,r),
                mappingInput1.value(model,context),
                mappingInput2.value(model,context));
    }
}
