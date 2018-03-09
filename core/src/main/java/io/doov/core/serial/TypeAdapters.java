/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.serial;

import java.util.*;
import java.util.stream.Stream;

import io.doov.core.serial.adapter.*;

/**
 * Default implementation of Type adapter registry
 */
public class TypeAdapters implements TypeAdapterRegistry {

    public static TypeAdapters INSTANCE = new TypeAdapters();

    protected static List<TypeAdapter> TYPE_ADAPTERS = Arrays.asList(
                    new BooleanTypeAdapter(),
                    new IntegerTypeAdapter(),
                    new DoubleTypeAdapter(),
                    new LongTypeAdapter(),
                    new FloatTypeAdapter(),
                    new StringTypeAdapter(),
                    new DateTypeAdapter(),
                    new LocalDateTypeAdapter(),
                    new CodeValuableEnumTypeAdapter()
    );

    @Override
    public Stream<TypeAdapter> stream() {
        return TYPE_ADAPTERS.stream();
    }

}
