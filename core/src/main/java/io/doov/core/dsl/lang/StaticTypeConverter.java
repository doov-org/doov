/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.lang;

import io.doov.core.dsl.meta.SyntaxTree;

/**
 * Converter for static values
 *
 * @param <I> in type
 * @param <O> out type
 */
public interface StaticTypeConverter<I, O> extends Readable, SyntaxTree {

    /**
     * Return the converted value
     *
     * @param input input
     * @return output
     */
    O convert(I input);

}