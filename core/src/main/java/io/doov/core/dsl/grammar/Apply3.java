/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar;

import static io.doov.core.dsl.utils.JsonGrammar.JBind;
import static io.doov.core.dsl.utils.JsonGrammar.JNode;
import static io.doov.core.dsl.utils.JsonGrammar.JObject;
import static io.doov.core.dsl.utils.JsonGrammar.JString;

public abstract class Apply3<I,J,K,O> extends Application<O> {

    public final ASTNode<I> input1;
    public final ASTNode<J> input2;
    public final ASTNode<K> input3;

    public Apply3(Class<O> output, ASTNode<I> input1, ASTNode<J> input2, ASTNode<K> input3) {
        super(output);
        this.input1 = input1;
        this.input2 = input2;
        this.input3 = input3;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + input1.toString() + ", " + input2.toString() + ", " + input3.toString() + ")";
    }

    @Override
    public JNode json() {
        return new JObject(
                new JBind("meta", new JString("TERNARY")),
                new JBind("tag", new JString(this.getClass().getSimpleName())),
                new JBind("input", input1.json()),
                new JBind("input", input2.json()),
                new JBind("input", input3.json())
        );
    }
}
