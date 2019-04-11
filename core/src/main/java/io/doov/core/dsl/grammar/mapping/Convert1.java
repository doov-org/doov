/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.mapping;

import static io.doov.core.dsl.utils.JsonGrammar.JBind;
import static io.doov.core.dsl.utils.JsonGrammar.JNode;
import static io.doov.core.dsl.utils.JsonGrammar.JObject;
import static io.doov.core.dsl.utils.JsonGrammar.JString;

import java.util.function.Function;

import io.doov.core.FieldModel;
import io.doov.core.dsl.grammar.Value;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.TypeConverter;
import io.doov.core.dsl.meta.ConverterMetadata;
import io.doov.core.dsl.meta.Metadata;

public class Convert1<I,O> extends Value<O> {

    public final Value<I> input;
    public final TypeConverter<I,O> process;

    public Convert1(Value<I> input, TypeConverter<I,O> process) {
        this.input = input;
        this.process = process;
    }

    public Convert1(Value<I> input, Function<I,O> process) {
        this.input = input;
        this.process = lift(process);
    }

    private static <I,O> TypeConverter<I,O> lift(Function<I,O> f) {
        return new TypeConverter<I, O>() {
            @Override
            public O convert(FieldModel fieldModel, Context context, I input) {
                return f.apply(input);
            }

            @Override
            public Metadata metadata() {
                return ConverterMetadata.metadata("Converted from lambda");
            }
        };
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public JNode jsonNode() {
        return new JObject(
                new JBind("type",new JString("CONVERT1")),
                new JBind("input", input.jsonNode()),
                new JBind("process", new JString(process.metadata().readable()))
        );
    }
}
