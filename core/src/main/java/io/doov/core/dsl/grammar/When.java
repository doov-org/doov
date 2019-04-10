/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar;

public class When extends Value<Boolean> {

    public final Value<Boolean> predicate;

    public When(Value<Boolean> predicate) {
        super(Boolean.class);
        this.predicate = predicate;
    }
}
