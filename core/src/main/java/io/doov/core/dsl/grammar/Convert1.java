/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar;

import java.util.function.Function;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.TypeConverter;
import io.doov.core.dsl.meta.*;

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
}
