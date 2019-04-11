/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.leaf;

import static io.doov.core.dsl.utils.JsonGrammar.JBind;
import static io.doov.core.dsl.utils.JsonGrammar.JNode;
import static io.doov.core.dsl.utils.JsonGrammar.JObject;
import static io.doov.core.dsl.utils.JsonGrammar.JString;

import io.doov.core.dsl.grammar.Value;

public class NotYetImplemented<T> extends Value<T> {

    public final Class<?> tag;

    public NotYetImplemented(Class<?> tag) {
        this.tag = tag;
    }

    public String toString() {
        return "NotYetImplemented(" + tag + ")";
    }

    @Override
    public JNode jsonNode() {
        return new JObject(
                new JBind("meta", new JString("-- NOT YET IMPLEMENTED --")),
                new JBind("class", new JString(tag.toString()))
        );
    }
}
