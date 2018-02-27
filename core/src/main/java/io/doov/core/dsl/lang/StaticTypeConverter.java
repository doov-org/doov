/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.lang;

import io.doov.core.dsl.meta.SyntaxTree;

public interface StaticTypeConverter<I, O> extends Readable, SyntaxTree {

    O convert(I input);

}