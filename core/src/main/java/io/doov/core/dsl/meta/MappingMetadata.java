/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import static io.doov.core.dsl.meta.ElementType.FIELD;
import static io.doov.core.dsl.meta.ElementType.OPERATOR;
import static io.doov.core.dsl.meta.ElementType.UNKNOWN;
import static io.doov.core.dsl.meta.ElementType.VALUE;
import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToString;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.Readable;

public class MappingMetadata implements Metadata {

    private final Deque<Element> elements;
    private final MetadataType type;

    public MappingMetadata(MetadataType type) {
        this.elements = new ArrayDeque<>();
        this.type = type;
    }

    public static MappingMetadata mappings(MappingOperator operator) {
        return new MappingMetadata(MetadataType.MULTIPLE_MAPPING).operator(operator);
    }

    public static MappingMetadata mapping() {
        return new MappingMetadata(MetadataType.SINGLE_MAPPING);
    }

    public static MappingMetadata inputMetadata() {
        return new MappingMetadata(MetadataType.MAPPING_INPUT);
    }

    public static MappingMetadata inputMetadata(String readable) {
        return new MappingMetadata(MetadataType.MAPPING_INPUT).valueReadable(() -> readable);
    }

    public static MappingMetadata valueInput(Supplier<?> supplier) {
        return inputMetadata().value(supplier);
    }

    public static MappingMetadata fieldsInput(List<DslField> fields) {
        return inputMetadata().fields(fields);
    }

    public static MappingMetadata functionInput() {
        return inputMetadata().function();
    }

    public static MappingMetadata fieldInput(DslField field) {
        return inputMetadata().field(field);
    }

    public static MappingMetadata metadataInput(Metadata... metadata) {
        return inputMetadata().mergeMetadata(metadata);
    }

    public static MappingMetadata outputMetadata() {
        return new MappingMetadata(MetadataType.MAPPING_OUTPUT);
    }

    public static MappingMetadata outputMetadata(String readable) {
        return new MappingMetadata(MetadataType.MAPPING_OUTPUT).valueReadable(() -> readable);
    }

    public static MappingMetadata fieldOutput(DslField field) {
        return outputMetadata().field(field);
    }

    public static MappingMetadata functionOutput() {
        return outputMetadata().function();
    }

    private MappingMetadata fields(List<DslField> fields) {
        Iterator<DslField> iterator = fields.iterator();
        while(iterator.hasNext()) {
            DslField f = iterator.next();
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
            Metadata m = iterator.next();
            m.flatten().forEach(this::add);
            if (iterator.hasNext()) {
                this.operator(MappingOperator.and);
            }
        }
        return this;
    }

    public Stream<Element> stream() {
        return elements.stream();
    }

    private MappingMetadata value(Supplier<?> supplier) {
        return add(new Element(() -> String.valueOf(supplier.get()), ElementType.VALUE));
    }

    private MappingMetadata function() {
        return add(new Element(() -> "-function-", UNKNOWN));
    }

    public MappingMetadata valueReadable(Readable readable) {
        return add(readable == null ? null : new Element(readable, VALUE));
    }

    public MappingMetadata valueUnknown(String readable) {
        return add(readable == null ? null : new Element(() -> "-function- " + readable, UNKNOWN));
    }

    private MappingMetadata field(DslField readable) {
        return add(readable == null ? null : new Element(readable, FIELD));
    }

    private MappingMetadata operator(MappingOperator op) {
        return add(op == null ? null : new Element(op, OPERATOR));
    }

    private MappingMetadata add(Element element) {
        if (element != null) {
            elements.add(element);
        }
        return this;
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        visitor.start(this, depth);
        visitor.visit(this, depth);
        visitor.end(this, depth);
    }

    @Override
    public List<Element> flatten() {
        return new ArrayList<>(elements);
    }

    @Override
    public List<Metadata> children() {
        return Collections.emptyList();
    }

    @Override
    public String readable(Locale locale) {
        return astToString(this, locale);
    }

    @Override
    public MetadataType type() {
        return type;
    }

    @Override
    public Metadata message(Context context) {
        return this;
    }
}
