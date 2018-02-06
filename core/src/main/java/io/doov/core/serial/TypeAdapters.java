/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.serial;

import java.util.*;
import java.util.stream.Stream;

import io.doov.core.serial.adapter.*;

public class TypeAdapters implements TypeAdapterRegistry {

    protected static List<TypeAdapter> TYPE_ADAPTERS = Arrays.asList(
                    new BooleanTypeAdapter(),
                    new IntegerTypeAdapter(),
                    new DoubleTypeAdapter(),
                    new LongTypeAdapter(),
                    new FloatTypeAdapter(),
                    new StringTypeAdapter(),
                    new DateTypeAdapter(),
                    new LocalDateTypeAdapter()
    );

    @Override
    public Stream<TypeAdapter> stream() {
        return TYPE_ADAPTERS.stream();
    }

}
