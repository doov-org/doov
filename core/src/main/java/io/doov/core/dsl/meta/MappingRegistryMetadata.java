/*
 * Copyright 2018 Courtanet
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

import java.util.List;

public class MappingRegistryMetadata extends NaryMetadata {

    private final MetadataType type;

    public MappingRegistryMetadata(Operator operator, List<Metadata> values, MetadataType type) {
        super(operator, values);
        this.type = type;
    }

    public static MappingRegistryMetadata mappings(List<Metadata> values) {
        return new MappingRegistryMetadata(MappingOperator.mappings, values, MetadataType.MULTIPLE_MAPPING);
    }

    public static MappingRegistryMetadata then(List<Metadata> values) {
        return new MappingRegistryMetadata(MappingOperator.then, values, MetadataType.THEN_MAPPING);
    }

    public static MappingRegistryMetadata otherwise(List<Metadata> values) {
        return new MappingRegistryMetadata(MappingOperator._else, values, MetadataType.ELSE_MAPPING);
    }

    @Override
    public MetadataType type() {
        return type;
    }

}
