/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.path;

import java.util.List;

import io.doov.core.FieldId;
import io.doov.core.PathConstraint;

/**
 * Contains the same information as the {@link io.doov.core.Path} annotation.
 * Field paths are used for associating a field with a {@link FieldId} without using annotations.
 * This is used at code generation for generating {@link io.doov.core.FieldInfo} class.
 */
public interface FieldPath {

    /**
     * Entry point of the annotated model.
     *
     * @return base class of the model
     */
    Class<?> getBaseClass();

    /**
     * List of read method references to access the field
     *
     * @return list of read method references
     */
    List<ReadMethodRef<?, ?>> getPath();

    /**
     * The field Id
     *
     * @return field id
     */
    FieldId getFieldId();

    /**
     * The path constraint
     *
     * @return path constraint
     */
    PathConstraint getConstraint();

    /**
     * {@link io.doov.core.dsl.lang.Readable}
     *
     * @return the readable string
     */
    String getReadable();

    /**
     * The read access method for the field
     *
     * @return read method reference
     */
    ReadMethodRef getReadMethod();

    /**
     * The write access method for the field
     *
     * @return write method reference
     */
    WriteMethodRef getWriteMethod();

    /**
     * Returns whether the field is transient
     *
     * @return true if field is transient
     */
    boolean isTransient();

}
