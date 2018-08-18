/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.lang;

import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToString;

import java.util.Locale;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.meta.MappingMetadata;
import io.doov.core.dsl.meta.SyntaxTree;

/**
 * Mapping input
 *
 * @param <T> input value type
 */
public interface MappingInput<T> extends SyntaxTree {

    /**
     * Reads the input value
     *
     * @param inModel model
     * @param context context
     * @return input value
     */
    T read(DslModel inModel, Context context);

    /**
     * Verifies the input for given in model
     *
     * @param inModel in model
     * @return true if the input can read a value from the model
     */
    boolean validate(FieldModel inModel);

    /**
     * Returns the metadata to describe this node.
     *
     * @return the metadata
     */
    MappingMetadata getMetadata();

    @Override
    default String readable(Locale locale) {
        return astToString(this, locale);
    }
}
