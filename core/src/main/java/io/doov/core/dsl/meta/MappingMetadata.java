/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import static io.doov.core.dsl.meta.ElementType.UNKNOWN;
import static io.doov.core.dsl.meta.MetadataType.MAPPING_LEAF;
import static io.doov.core.dsl.meta.MetadataType.MULTIPLE_MAPPING;

import java.util.*;
import java.util.function.Supplier;

import io.doov.core.dsl.DslField;

public class MappingMetadata extends LeafMetadata<MappingMetadata> {

    private MappingMetadata(MetadataType type) {
        super(type);
    }

    public static MappingMetadata mappings(MappingOperator operator) {
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

    public static MappingMetadata metadataInput(Metadata... metadata) {
        return new MappingMetadata(MAPPING_LEAF).mergeMetadata(metadata);
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

    private MappingMetadata mergeMetadata(Metadata... metadata) {
        Iterator<Metadata> iterator = Arrays.asList(metadata).iterator();
        while (iterator.hasNext()) {
            MappingMetadata m = (MappingMetadata) iterator.next();
            m.elements().forEach(this::add);
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
