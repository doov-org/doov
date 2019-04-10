/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class ApplyN<I,O> extends Application<O> {

    public final List<Value<I>> inputs;

    public ApplyN(Class<O> output, List<Value<I>> inputs) {
        super(output);
        this.inputs = inputs;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + inputs.stream()
                .map(Objects::toString)
                .collect(Collectors.joining(", ")) + ")";
    }
}
