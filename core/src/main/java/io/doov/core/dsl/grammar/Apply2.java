/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar;

import static io.doov.core.dsl.utils.JsonGrammar.JBind;
import static io.doov.core.dsl.utils.JsonGrammar.JNode;
import static io.doov.core.dsl.utils.JsonGrammar.JObject;
import static io.doov.core.dsl.utils.JsonGrammar.JString;

public abstract class Apply2<I,J,O> extends Application<O> {

    public final Value<I> lhs;
    public final Value<J> rhs;

    public Apply2(Class<O> output, Value<I> lhs, Value<J> rhs) {
        super(output);
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + lhs.toString() + ", " + rhs.toString() + ")";
    }

    @Override
    public JNode jsonNode() {
        return new JObject(
                new JBind("meta", new JString("BINARY")),
                new JBind("tag", new JString(this.getClass().getSimpleName())),
                new JBind("lhs", lhs.jsonNode()),
                new JBind("rhs", rhs.jsonNode())
        );
    }
}
