/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.leaf;

import io.doov.core.dsl.grammar.Value;

public class NotYetImplemented<T> extends Value<T> {

    public final Class<?> tag;

    public NotYetImplemented(Class<?> tag) {
        super(null);
        this.tag = tag;
    }

    public String toString() {
        return "NotYetImplemented(" + tag + ")";
    }
}
