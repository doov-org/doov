/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast.writer;

import io.doov.core.FieldInfo;
import io.doov.core.dsl.DslField;

public interface FieldSpec {

    String name();
    DslField<?> field();
    FieldInfo fieldInfo();
    String toConstDeclaration();
}
