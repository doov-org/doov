/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar;

import static io.doov.core.dsl.utils.JsonGrammar.*;

import java.util.List;
import java.util.function.Function;

import io.doov.core.dsl.lang.NaryTypeConverter;
import io.doov.core.dsl.utils.JsonGrammar;

public class ConvertN<I,O> extends Value<O> {

    public final List<Value<I>> inputs;
    public final NaryTypeConverter<O> process;

    public ConvertN(List<Value<I>> inputs, NaryTypeConverter<O> process) {
        this.inputs = inputs;
        this.process = process;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public JNode jsonNode() {
        return new JObject(
                new JBind("type",new JString("CONVERT1")),
                new JBind("input", new JArray(inputs.stream().map(Value::jsonNode))),
                new JBind("process", new JString(process.metadata().readable()))
        );
    }
}
