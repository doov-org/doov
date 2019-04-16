/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar;

import static io.doov.core.dsl.utils.JsonGrammar.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class ApplyN<I,O> extends Application<O> {

    public final List<ASTNode<I>> inputs;

    public ApplyN(Class<O> output, List<ASTNode<I>> inputs) {
        super(output);
        this.inputs = inputs;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + inputs.stream()
                .map(Objects::toString)
                .collect(Collectors.joining(", ")) + ")";
    }

    @Override
    public JNode json() {
        return new JObject(
                new JBind("meta", new JString("NARY")),
                new JBind("tag", new JString(this.getClass().getSimpleName())),
                new JBind("inputs", new JArray(inputs.stream().map(ASTNode::json)))
        );
    }
}
