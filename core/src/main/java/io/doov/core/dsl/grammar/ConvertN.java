/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar;

import java.util.List;
import java.util.function.Function;

import io.doov.core.dsl.lang.NaryTypeConverter;

public class ConvertN<I,O> extends Value<O> {

    public final List<Value<I>> inputs;
    public final NaryTypeConverter<O> process;

    public ConvertN(List<Value<I>> inputs, NaryTypeConverter<O> process) {
        this.inputs = inputs;
        this.process = process;
    }
}
