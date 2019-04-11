/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar;

public class When extends Value<Boolean> {

    public final Value<Boolean> predicate;

    public When(Value<Boolean> predicate) {
        this.predicate = predicate;
    }

    public String toString() {
        return "When(" + predicate.toString() + ")";
    }
}
