/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.mapping;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.doov.core.dsl.grammar.Value;

public class Registry extends Value<Void> {
    public final List<Value> registry;

    public Registry(List<Value> registry) {
        this.registry = registry;
    }

    @Override
    public String toString() {
        return "Registry(" + registry.stream().map(Objects::toString).collect(Collectors.joining(", ")) + ")";
    }
}
