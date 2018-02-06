/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.path;

import java.util.*;

import io.doov.core.FieldId;
import io.doov.core.PathConstraint;

public class PathBuilder<B, C, T> {

    private final Class<B> baseClass;
    private final List<ReadMethodRef<?, ?>> pathList;
    private FieldId fieldId;
    private PathConstraint constraint;
    private String readable;
    private boolean _transient = false;
    private ReadMethodRef<C, ?> readMethodRef;
    private WriteMethodRef<C, T> writeMethodRef;

    public static <B> PathBuilder<B, B, B> from(Class<B> baseClass) {
        return new PathBuilder<>(baseClass);
    }

    private PathBuilder(Class<B> baseClass) {
        this.baseClass = baseClass;
        this.pathList = new ArrayList<>();
    }

    public PathBuilder<B, C, T> fieldId(FieldId fieldId) {
        this.fieldId = fieldId;
        return this;
    }

    public PathBuilder<B, C, T> constraint(PathConstraint constraint) {
        this.constraint = constraint;
        return this;
    }

    public PathBuilder<B, C, T> readable(String readable) {
        this.readable = readable;
        return this;
    }

    public PathBuilder<B, C, T> _transient(boolean _transient) {
        this._transient = _transient;
        return this;
    }

    public <R> PathBuilder<B, T, R> get(ReadMethodRef<T, R> readMethodRef) {
        pathList.add(readMethodRef);
        PathBuilder<B, T, R> pathBuilder = (PathBuilder<B, T, R>) this;
        pathBuilder.readMethodRef = readMethodRef;
        return pathBuilder;
    }

    public <R> PathBuilder<B, T, R> iterable(ReadMethodRef<T, Iterable<R>> readMethodRef) {
        pathList.add(readMethodRef);
        PathBuilder<B, T, R> pathBuilder = (PathBuilder<B, T, R>) this;
        pathBuilder.readMethodRef = readMethodRef;
        return pathBuilder;
    }

    public <R> PathBuilder<B, T, R> field(ReadMethodRef<T, R> readMethodRef, WriteMethodRef<T, R> writeMethodRef) {
        pathList.add(readMethodRef);
        PathBuilder<B, T, R> pathBuilder = (PathBuilder<B, T, R>) this;
        pathBuilder.readMethodRef = readMethodRef;
        pathBuilder.writeMethodRef = writeMethodRef;
        return pathBuilder;
    }

    public PathBuilder<B, C, T> set(WriteMethodRef<C, T> writeMethodRef) {
        this.writeMethodRef = writeMethodRef;
        return this;
    }

    public DefaultFieldPath build() {
        return new DefaultFieldPath(baseClass, Collections.unmodifiableList(pathList), fieldId,
                        constraint, readable, _transient, readMethodRef, writeMethodRef);
    }

    public DefaultFieldPath build(List<FieldPath> pathList) {
        DefaultFieldPath build = build();
        pathList.add(build);
        return build;
    }
}
