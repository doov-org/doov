/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar;

import static io.doov.core.dsl.utils.JsonGrammar.*;

import java.util.function.BiFunction;

import io.doov.core.dsl.lang.BiTypeConverter;
import io.doov.core.dsl.utils.JsonGrammar;

public class Convert2<I,J,O> extends Value<O> {

    public final Value<I> lhs;
    public final Value<J> rhs;
    public final BiTypeConverter<I,J,O> process;

    public Convert2(Value<I> lhs, Value<J> rhs, BiTypeConverter<I,J,O> process) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.process = process;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public JNode jsonNode() {
        return new JObject(
                new JBind("meta", new JString("CONVERT2")),
                new JBind("lhs", lhs.jsonNode()),
                new JBind("rhs", lhs.jsonNode()),
                new JBind("process", new JString(process.metadata().readable()))
        );
    }
}
