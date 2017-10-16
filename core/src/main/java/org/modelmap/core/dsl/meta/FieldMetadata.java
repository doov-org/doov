/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.meta;

import static java.util.stream.Collectors.joining;

import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Stream;

import org.modelmap.core.FieldInfo;
import org.modelmap.core.dsl.field.*;

public class FieldMetadata extends AbstractMetadata {

    private static final FieldMetadata EMPTY = new FieldMetadata(null, null, null);

    private final String field;
    private final String operator;
    private final String value;

    private FieldMetadata(Object field, String operator, Object value) {
        this.field = field == null ? null : field.toString();
        this.operator = operator;
        this.value = value == null ? null : value.toString();
    }

    private FieldMetadata(Readable field, String operator, Object value) {
        this.field = field == null ? null : field.readable();
        this.operator = operator;
        this.value = value == null ? null : value.toString();
    }

    private FieldMetadata(Readable field, String operator, Readable value) {
        this.field = field == null ? null : field.readable();
        this.operator = operator;
        this.value = value == null ? null : value.readable();
    }

    public static FieldMetadata empty() {
        return EMPTY;
    }

    public static FieldMetadata equals(FieldInfo field, Object value) {
        return new FieldMetadata(field, "equals", value);
    }

    public static FieldMetadata notEquals(FieldInfo field, Object value) {
        return new FieldMetadata(field, "not equals", value);
    }

    public static FieldMetadata isNull(FieldInfo field, Object value) {
        return new FieldMetadata(field, "is null", value);
    }

    public static FieldMetadata isNotNull(FieldInfo field, Object value) {
        return new FieldMetadata(field, "is not null", value);
    }

    public static FieldMetadata after(LocalDateFieldInfo field, LocalDate value) {
        return new FieldMetadata(field, "after", value);
    }

    public static FieldMetadata before(LocalDateFieldInfo field, LocalDate value) {
        return new FieldMetadata(field, "before", value);
    }

    public static FieldMetadata matches(StringFieldInfo field, String value) {
        return new FieldMetadata(field, "matches", value);
    }

    public static FieldMetadata contains(StringFieldInfo field, String value) {
        return new FieldMetadata(field, "contains", value);
    }

    public static FieldMetadata startsWith(StringFieldInfo field, String value) {
        return new FieldMetadata(field, "starts with", value);
    }

    public static FieldMetadata endsWith(StringFieldInfo field, String value) {
        return new FieldMetadata(field, "ends with", value);
    }

    public static FieldMetadata is(BooleanFieldInfo field, boolean value) {
        return new FieldMetadata(field, "is", value);
    }

    public static <T extends Number> FieldMetadata lesserThan(NumericFieldInfo<T> field, T value) {
        return new FieldMetadata(field, "lesser than", value);
    }

    public static <T extends Number> FieldMetadata lesserOrEquals(NumericFieldInfo<T> field, T value) {
        return new FieldMetadata(field, "lesser or equals", value);
    }

    public static <T extends Number> FieldMetadata greaterThan(NumericFieldInfo<T> field, T value) {
        return new FieldMetadata(field, "greater than", value);
    }

    public static <T extends Number> FieldMetadata greaterOrEquals(NumericFieldInfo<T> field, T value) {
        return new FieldMetadata(field, "greater or equals", value);
    }

    public static <T extends Number> FieldMetadata between(NumericFieldInfo<T> field, Number min, T max) {
        return new FieldMetadata(field, "between", min + " and " + max);
    }

    public static FieldMetadata lengthIs(StringFieldInfo field) {
        return new FieldMetadata(field, "length is", null);
    }

    public FieldMetadata merge(FieldMetadata metadata) {
        if (this.equals(EMPTY)) {
            return metadata;
        }
        return new FieldMetadata(this.field, this.operator + " " + metadata.operator, metadata.value);
    }

    @Override
    public String readable() {
        return Stream.of("'" + field + "'", operator, value)
                        .filter(Objects::nonNull)
                        .map(Objects::toString)
                        .collect(joining(" "));
    }

}
