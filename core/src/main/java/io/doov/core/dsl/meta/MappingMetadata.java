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

import static io.doov.core.dsl.meta.ElementType.UNKNOWN;
import static io.doov.core.dsl.meta.MetadataType.MAPPING_LEAF;
import static io.doov.core.dsl.meta.MetadataType.MULTIPLE_MAPPING;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import io.doov.core.dsl.DslField;

public class MappingMetadata extends LeafMetadata<MappingMetadata> {

    private MappingMetadata(MetadataType type) {
        super(type);
    }

    public static MappingMetadata mappings(Operator operator) {
        return new MappingMetadata(MULTIPLE_MAPPING).operator(operator);
    }

    public static MappingMetadata inputMetadata(String readable) {
        return new MappingMetadata(MAPPING_LEAF).valueReadable(() -> readable);
    }

    public static MappingMetadata valueInput(Supplier<?> supplier) {
        return new MappingMetadata(MAPPING_LEAF).valueSupplier(supplier);
    }

    public static MappingMetadata fieldsInput(List<DslField<?>> fields) {
        return new MappingMetadata(MAPPING_LEAF).fields(fields);
    }

    public static MappingMetadata functionInput() {
        return new MappingMetadata(MAPPING_LEAF).function();
    }

    public static MappingMetadata fieldInput(DslField<?> field) {
        return new MappingMetadata(MAPPING_LEAF).field(field);
    }

    public static MappingMetadata outputMetadata(String readable) {
        return new MappingMetadata(MAPPING_LEAF).valueReadable(() -> readable);
    }

    public static MappingMetadata fieldOutput(DslField<?> field) {
        return new MappingMetadata(MAPPING_LEAF).field(field);
    }

    public static MappingMetadata functionOutput() {
        return new MappingMetadata(MAPPING_LEAF).function();
    }

    private MappingMetadata fields(List<DslField<?>> fields) {
        Iterator<DslField<?>> iterator = fields.iterator();
        while (iterator.hasNext()) {
            DslField<?> f = iterator.next();
            this.field(f);
            if (iterator.hasNext()) {
                this.operator(MappingOperator.and);
            }
        }
        return this;
    }

    private MappingMetadata function() {
        return add(new Element(() -> "-function-", UNKNOWN));
    }

}
