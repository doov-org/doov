/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.mapping;

import static io.doov.core.dsl.utils.JsonGrammar.JBind;
import static io.doov.core.dsl.utils.JsonGrammar.JNode;
import static io.doov.core.dsl.utils.JsonGrammar.JObject;
import static io.doov.core.dsl.utils.JsonGrammar.JString;

import io.doov.core.dsl.grammar.ASTNode;
import io.doov.core.dsl.lang.BiTypeConverter;

public class Convert2<I,J,O> extends ASTNode<O> {

    public final ASTNode<I> lhs;
    public final ASTNode<J> rhs;
    public final BiTypeConverter<I,J,O> process;

    public Convert2(ASTNode<I> lhs, ASTNode<J> rhs, BiTypeConverter<I,J,O> process) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.process = process;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public JNode json() {
        return new JObject(
                new JBind("meta", new JString("CONVERT2")),
                new JBind("lhs", lhs.json()),
                new JBind("rhs", lhs.json()),
                new JBind("process", new JString(process.metadata().readable()))
        );
    }
}
