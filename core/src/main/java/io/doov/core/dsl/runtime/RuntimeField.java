/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.runtime;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import io.doov.core.*;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.impl.DefaultCondition;

/**
 * Runtime implementation for {@link FieldInfo} and {@link DslField}.
 * {@link Function#apply(Object)} method gets the value of the field from the model.
 * {@link BiConsumer#accept(Object, Object)} sets the value of the field to the model.
 *
 * @param <B> model entry type
 * @param <R> field type
 */
public class RuntimeField<B, R> implements DslField<R>, FieldInfo, Function<B, R>, BiConsumer<B, R> {

    private final List<PathMethod<Object, Object>> chain;
    private final PathMethod<Object, R> lastLink;
    private final FieldId id;
    private final String readable;
    private FieldId[] siblings;
    private Class<R> type;
    private final Class<?>[] genericTypes;
    private boolean isCodeLookup;
    private boolean isCodeValuable;
    private boolean isTransient;

    public RuntimeField(List<PathMethod<Object, Object>> chain,
                    PathMethod<Object, R> lastLink,
                    FieldId id,
                    String readable,
                    FieldId[] siblings,
                    Class<R> type,
                    Class<?>[] genericTypes,
                    boolean isTransient) {
        this.chain = chain;
        this.lastLink = lastLink;
        this.id = id;
        this.readable = readable;
        this.siblings = siblings;
        this.type = type;
        this.genericTypes = genericTypes;
        this.isCodeLookup = CodeLookup.class.isAssignableFrom(type);
        this.isCodeValuable = CodeValuable.class.isAssignableFrom(type);
        this.isTransient = isTransient;
    }

    @Override
    public FieldId id() {
        return id;
    }

    @Override
    public String readable() {
        return readable;
    }

    @Override
    public DefaultCondition<R> getDefaultFunction() {
        return new DefaultCondition<>(this);
    }

    @Override
    public R apply(B b) {
        return get(b);
    }

    public R get(B model) {
        if (model == null) {
            return null;
        }
        Object next = model;
        for (PathMethod<Object, Object> m : chain) {
            next = m.get(next);
            if (next == null) {
                return null;
            }
        }
        return lastLink.get(next);
    }

    @Override
    public void accept(B b, R r) {
        set(b, r);
    }

    public void set(B model, R value) {
        if (model == null) {
            return;
        }
        Object nextNode = model;
        for (PathMethod<Object, Object> method : chain) {
            Object methodReturn = method.get(nextNode);
            if (methodReturn == null) {
                if (value != null) { // create the path to insert value
                    nextNode = method.create(nextNode);
                } else { // Don't create the path for a null value
                    return;
                }
            } else { // Continue on path
                nextNode = methodReturn;
            }
        }
        lastLink.set(nextNode, value);
    }

    @SuppressWarnings("unchecked")
    public RuntimeField<B, R> register(List<RuntimeField<B, Object>> registry) {
        registry.add((RuntimeField<B, Object>) this);
        return this;
    }

    @Override
    public FieldId[] siblings() {
        return siblings;
    }

    @Override
    public Class<R> type() {
        return type;
    }

    @Override
    public Class<?>[] genericTypes() {
        return genericTypes;
    }

    @Override
    public boolean isCodeLookup() {
        return isCodeLookup;
    }

    @Override
    public boolean isCodeValuable() {
        return isCodeValuable;
    }

    @Override
    public boolean isTransient() {
        return isTransient;
    }

}
