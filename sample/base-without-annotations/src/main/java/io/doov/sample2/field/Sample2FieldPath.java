/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample2.field;

import java.util.ArrayList;
import java.util.List;

import io.doov.core.dsl.path.FieldPath;
import io.doov.core.dsl.path.FieldPathProvider;

public class Sample2FieldPath implements FieldPathProvider {

    private static final List<FieldPath> ALL = new ArrayList<>();

    static {
        ALL.addAll(Sample2IdFieldPaths.getFieldPaths());
        ALL.addAll(Sample2LoginFieldPaths.getFieldPaths());
        ALL.addAll(Sample2PasswordFieldPaths.getFieldPaths());
        ALL.addAll(Sample2EmailFieldPaths.getFieldPaths());
    }

    @Override
    public List<FieldPath> values() {
        return ALL;
    }
}
