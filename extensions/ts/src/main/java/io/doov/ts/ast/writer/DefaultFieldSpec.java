/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast.writer;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

import io.doov.core.FieldInfo;
import io.doov.core.dsl.DslField;

public class DefaultFieldSpec implements FieldSpec {

    private final String name;
    private final DslField<?> field;
    private final FieldInfo fieldInfo;

    public DefaultFieldSpec(String name, DslField<?> field, FieldInfo fieldInfo) {
        this.name = name;
        this.field = field;
        this.fieldInfo = fieldInfo;
    }

    public String name() {
        return name;
    }

    public DslField<?> field() {
        return field;
    }

    public FieldInfo fieldInfo() {
        return fieldInfo;
    }

    public String toConstDeclaration() {
        return "const " + name + " = DOOV." + functionTypeString() + "(DOOV.field<" + fieldTypeString() + ">('" + name + "'));";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DefaultFieldSpec fieldSpec = (DefaultFieldSpec) o;
        return name.equals(fieldSpec.name) &&
                field.id().code().equals(fieldSpec.field.id().code());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, field.id().code());
    }

    public String functionTypeString() {
        if (fieldInfo.type().isEnum()) {
            return "f";
        } else if (Iterable.class.isAssignableFrom(fieldInfo.type())) {
            return "iterable";
        } else if (fieldInfo.type().equals(String.class)) {
            return "string";
        } else if (Number.class.isAssignableFrom(fieldInfo.type())
                || fieldInfo.type().equals(Integer.TYPE)
                || fieldInfo.type().equals(Double.TYPE)
                || fieldInfo.type().equals(Long.TYPE)
        ) {
            return "number";
        } else if (fieldInfo.type().equals(Boolean.class) || fieldInfo.type().equals(Boolean.TYPE)) {
            return "boolean";
        } else if (fieldInfo.type().equals(LocalDate.class) || fieldInfo.type().equals(Date.class)) {
            return "date";
        } else {
            return "f";
        }
    }

    public String fieldTypeString() {
        if (Iterable.class.isAssignableFrom(fieldInfo.type())) {
            if (fieldInfo.genericTypes().length > 0) {
                return fieldType(fieldInfo.genericTypes()[0]) + "[]";
            } else {
                return "unknown[]";
            }
        } else {
            return fieldType(fieldInfo.type());
        }
    }

    public String fieldType(Class<?> type) {
        if (type == null) {
            return "unknown";
        }
        if (type.isEnum()) {
            return type.getSimpleName();
        } else if (type.equals(String.class)) {
            return "string";
        } else if (Number.class.isAssignableFrom(type)
                || type.equals(Integer.TYPE)
                || type.equals(Double.TYPE)
                || type.equals(Long.TYPE)) {
            return "number";
        } else if (type.equals(Boolean.class) || type.equals(Boolean.TYPE)) {
            return "boolean";
        } else if (type.equals(LocalDate.class) || type.equals(Date.class)) {
            return "Date";
        } else {
            return "unknown";
        }
    }
}
