/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.path;

import java.util.List;

import io.doov.core.FieldId;
import io.doov.core.PathConstraint;

public class DefaultFieldPath implements FieldPath {

    private final Class<?> baseClass;
    private final List<ReadMethodRef<?, ?>> path;
    private final FieldId fieldId;
    private final PathConstraint constraint;
    private final String readable;
    private final boolean _transient;
    private final ReadMethodRef readMethodReference;
    private final WriteMethodRef writeMethodReference;

    public DefaultFieldPath(Class<?> baseClass, List<ReadMethodRef<?, ?>> path, FieldId fieldId,
                    PathConstraint constraint, String readable, boolean _transient, ReadMethodRef readMethodReference,
                    WriteMethodRef writeMethodReference) {
        this.baseClass = baseClass;
        this.path = path;
        this.fieldId = fieldId;
        this.constraint = constraint;
        this.readable = readable;
        this._transient = _transient;
        this.readMethodReference = readMethodReference;
        this.writeMethodReference = writeMethodReference;
    }

    @Override
    public Class<?> getBaseClass() {
        return baseClass;
    }

    @Override
    public List<ReadMethodRef<?, ?>> getPath() {
        return path;
    }

    @Override
    public FieldId getFieldId() {
        return fieldId;
    }

    @Override
    public PathConstraint getConstraint() {
        return constraint;
    }

    @Override
    public String getReadable() {
        return readable;
    }

    @Override
    public boolean isTransient() {
        return _transient;
    }

    @Override
    public ReadMethodRef getReadMethod() {
        return readMethodReference;
    }

    @Override
    public WriteMethodRef getWriteMethod() {
        return writeMethodReference;
    }

    @Override
    public String toString() {
        return "DefaultFieldPath{" +
                        "baseClass=" + baseClass +
                        ", path=" + path +
                        ", fieldId=" + fieldId +
                        ", constraint=" + constraint +
                        ", readable='" + readable + '\'' +
                        ", _transient=" + _transient +
                        ", readMethodReference=" + readMethodReference +
                        ", writeMethodReference=" + writeMethodReference +
                        '}';
    }
}
