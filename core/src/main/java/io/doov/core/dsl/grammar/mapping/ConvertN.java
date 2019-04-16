/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.mapping;

import static io.doov.core.dsl.utils.JsonGrammar.*;

import java.util.List;

import io.doov.core.dsl.grammar.ASTNode;
import io.doov.core.dsl.lang.NaryTypeConverter;

public class ConvertN<I,O> extends ASTNode<O> {

    public final List<ASTNode<I>> inputs;
    public final NaryTypeConverter<O> process;

    public ConvertN(List<ASTNode<I>> inputs, NaryTypeConverter<O> process) {
        this.inputs = inputs;
        this.process = process;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public JNode json() {
        return new JObject(
                new JBind("type",new JString("CONVERT1")),
                new JBind("input", new JArray(inputs.stream().map(ASTNode::json))),
                new JBind("process", new JString(process.metadata().readable()))
        );
    }
}
