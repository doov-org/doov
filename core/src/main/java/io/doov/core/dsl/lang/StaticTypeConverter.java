/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.lang;

import java.util.Locale;

import io.doov.core.dsl.meta.SyntaxTree;

/**
 * Converter for static values
 *
 * @param <I> in type
 * @param <O> out type
 */
public interface StaticTypeConverter<I, O> extends Readable, SyntaxTree {
    @Override
    default String readable() {
        return readable(Locale.getDefault());
    }

    /**
     * Returns the human readable version of this object.
     *
     * @return the readable string
     */
    String readable(Locale locale);

    /**
     * Return the converted value
     *
     * @param input input
     * @return output
     */
    O convert(I input);

}