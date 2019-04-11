/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.leaf;

import static io.doov.core.dsl.utils.JsonGrammar.*;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.grammar.Value;
import io.doov.core.dsl.utils.JsonGrammar;

public class FieldValue<T> extends Value<T> {

    public final DslField<T> field;

    public FieldValue(DslField<T> field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "FieldValue(" + field.readable() + ":" + field.type().getSimpleName() + ")";
    }

    @Override
    public JNode jsonNode() {
        return new JObject(
                new JBind("meta", new JString("FIELD")),
                new JBind("class", new JString(field.type().toString())),
                new JBind("data", new JString(field.id().code()))
        );
    }
}
