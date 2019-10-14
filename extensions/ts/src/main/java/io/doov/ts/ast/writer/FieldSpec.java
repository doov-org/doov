/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast.writer;

import java.util.Objects;

import io.doov.core.FieldInfo;
import io.doov.core.dsl.DslField;

public class FieldSpec {

    private final String name;
    private final DslField<?> field;
    private final FieldInfo fieldInfo;

    public FieldSpec(String name, DslField<?> field, FieldInfo fieldInfo) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FieldSpec fieldSpec = (FieldSpec) o;
        return name.equals(fieldSpec.name) &&
                field.id().code().equals(fieldSpec.field.id().code());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, field.id().code());
    }

    public String toConstDeclaration() {
        return "const " + name + " = DOOV." + functionTypeString() + "(DOOV.field<" + fieldTypeString() + ">('" + name + "'));";
    }

    public String functionTypeString() {
        if (fieldInfo.type().isEnum()) {
            return "f";
        } else if (fieldInfo.type().equals(String.class)) {
            return "string";
        } else if (Number.class.isAssignableFrom(fieldInfo.type())) {
            return "number";
        } else if (fieldInfo.type().equals(Boolean.class)) {
            return "boolean";
        } else {
            return "any";
        }
    }

    public String fieldTypeString() {
        if (fieldInfo.type().isEnum()) {
            return fieldInfo.type().getSimpleName();
        } else if (fieldInfo.type().equals(String.class)) {
            return "string";
        } else if (Number.class.isAssignableFrom(fieldInfo.type())) {
            return "number";
        } else if (fieldInfo.type().equals(Boolean.class)) {
            return "boolean";
        } else {
            return "any";
        }
    }
}
