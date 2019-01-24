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
package io.doov.core.dsl.meta;

import static io.doov.core.dsl.meta.MetadataType.TYPE_CONVERTER;
import static io.doov.core.dsl.meta.MetadataType.TYPE_CONVERTER_IDENTITY;

public class ConverterMetadata extends LeafMetadata<ConverterMetadata> {

    public ConverterMetadata(MetadataType type) {
        super(type);
    }

    public static ConverterMetadata metadata(String description) {
        if (description == null || description.isEmpty()) {
            return new ConverterMetadata(TYPE_CONVERTER).valueUnknown(description);
        } else {
            return new ConverterMetadata(TYPE_CONVERTER).valueString(description);
        }
    }

    public static ConverterMetadata identity() {
        return new ConverterMetadata(TYPE_CONVERTER_IDENTITY).valueString("identity");
    }

}
