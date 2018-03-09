/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import static io.doov.core.dsl.meta.ElementType.FIELD;
import static io.doov.core.dsl.meta.ElementType.OPERATOR;
import static io.doov.core.dsl.meta.MappingOperator.map;
import static io.doov.core.dsl.meta.MappingOperator.to;
import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToString;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.stream.Stream;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.Context;

public class MappingMetadata implements Metadata {

    private final Deque<Element> elements;
    private final MetadataType type;

    public MappingMetadata(MetadataType type) {
        this.elements = new ArrayDeque<>();
        this.type = type;
    }

    private MappingMetadata(Deque<Element> elements, MetadataType type) {
        this.elements = elements;
        this.type = type;
    }

    public static MappingMetadata mappings(MappingOperator operator) {
        return new MappingMetadata(MetadataType.MULTIPLE_MAPPING).operator(operator);
    }

    public static MappingMetadata mapping(DslField inField, DslField outField) {
        return new MappingMetadata(MetadataType.SINGLE_MAPPING)
                .operator(map)
                .field(inField)
                .operator(to)
                .field(outField);
    }

    public static MappingMetadata mapping(List<DslField> inFields, DslField outField) {
        return new MappingMetadata(MetadataType.SINGLE_MAPPING)
                .operator(map)
                .fields(inFields)
                .operator(to)
                .field(outField);
    }

    public static MappingMetadata mapping(Supplier<?> supplier, DslField outField) {
        return new MappingMetadata(MetadataType.SINGLE_MAPPING)
                .operator(map)
                .value(supplier)
                .operator(to)
                .field(outField);
    }

    private MappingMetadata fields(List<DslField> fields) {
        Iterator<DslField> iterator = fields.iterator();
        iterator.forEachRemaining(f -> {
            this.field(f);
            if (iterator.hasNext()) {
                this.operator(MappingOperator.and);
            }
        });
        return this;
    }

    public Stream<Element> stream() {
        return elements.stream();
    }

    private MappingMetadata value(Supplier<?> supplier) {
        return add(new Element(() -> String.valueOf(supplier.get()), ElementType.VALUE));
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
    public String readable() {
        return astToString(this, Locale.getDefault());
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
