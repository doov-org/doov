/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.leaf;

import static io.doov.core.dsl.utils.JsonGrammar.JBind;
import static io.doov.core.dsl.utils.JsonGrammar.JNode;
import static io.doov.core.dsl.utils.JsonGrammar.JObject;
import static io.doov.core.dsl.utils.JsonGrammar.JString;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.grammar.ASTNode;

public class FieldValue<T> extends ASTNode<T> {

    public final DslField<T> field;

    public FieldValue(DslField<T> field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "FieldValue(" + field.readable() + ":" + field.type().getSimpleName() + ")";
    }

    @Override
    public JNode json() {
        return new JObject(
                new JBind("meta", new JString("FIELD")),
                new JBind("class", new JString(field.type().toString())),
                new JBind("data", new JString(field.id().code()))
        );
    }
}