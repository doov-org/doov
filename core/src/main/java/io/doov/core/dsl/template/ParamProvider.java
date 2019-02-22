/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import java.util.function.BiFunction;

import io.doov.core.FieldInfo;
import io.doov.core.dsl.DslField;

public interface ParamProvider<P,T extends FieldInfo & DslField<P>>
        extends BiFunction<ParameterNamespace,String,T> {
}
