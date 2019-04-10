/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar;

public abstract class Application<O> extends Value<O> {

    public final Class<O> output;

    public Application(Class<O> output) {
        this.output = output;
    }
}
