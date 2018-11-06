/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.lang;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslModel;

/**
 * Mapping output
 *
 * @param <T> output value type
 */
public interface MappingOutput<T> extends DSLBuilder {

    /**
     * Writes the output value
     *
     * @param outModel out model
     * @param context context
     * @param value value to write
     */
    void write(DslModel outModel, Context context, T value);

    /**
     * Verifies the output for the given out model
     *
     * @param outModel out model
     * @return true if the output can write a value to the model
     */
    boolean validate(FieldModel outModel);
}
