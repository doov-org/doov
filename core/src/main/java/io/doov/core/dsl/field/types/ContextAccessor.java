/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field.types;

import java.util.Optional;

import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;

public interface ContextAccessor<T> {
    
    Optional<T> value(DslModel model, Context context);
}
