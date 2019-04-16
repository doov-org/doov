/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.mapping;

import static io.doov.core.dsl.utils.JsonGrammar.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.doov.core.dsl.grammar.ASTNode;

public class Registry extends ASTNode<Void> {
    public final List<ASTNode> registry;

    public Registry(List<ASTNode> registry) {
        this.registry = registry;
    }

    @Override
    public String toString() {
        return "Registry(" + registry.stream().map(Objects::toString).collect(Collectors.joining(", ")) + ")";
    }

    @Override
    public JNode json() {
        return new JObject(
                new JBind("meta", new JString("REGISTRY")),
                new JBind("data", new JArray(registry.stream().map(ASTNode::json)))
        );
    }
}