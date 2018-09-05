/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.lang;

import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToString;

import java.util.Locale;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.SyntaxTree;

/**
 * Mapping output
 *
 * @param <T> output value type
 */
public interface MappingOutput<T> extends SyntaxTree {

    /**
     * Writes the output value
     *
     * @param outModel out model
     * @param context  context
     * @param value    value to write
     */
    void write(DslModel outModel, Context context, T value);

    /**
     * Verifies the output for the given out model
     *
     * @param outModel out model
     * @return true if the output can write a value to the model
     */
    boolean validate(FieldModel outModel);

    /**
     * Returns the metadata to describe this node.
     *
     * @return the metadata
     */
    Metadata getMetadata();

    @Override
    default String readable(Locale locale) {
        return astToString(this, locale);
    }
}
