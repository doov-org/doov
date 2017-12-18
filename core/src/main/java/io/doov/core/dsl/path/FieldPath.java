/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.path;

import java.util.List;

import io.doov.core.FieldId;
import io.doov.core.PathConstraint;

public interface FieldPath {

    Class<?> getBaseClass();

    List<ReadMethodRef<?, ?>> getPath();

    FieldId getFieldId();

    PathConstraint getConstraint();

    String getReadable();

    ReadMethodRef getReadMethod();

    WriteMethodRef getWriteMethod();

}
