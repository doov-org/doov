/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample;

import com.datastax.oss.driver.api.core.type.codec.*;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;

public class EnumNameCodec<E extends Enum<E>> extends MappingCodec<String, E> {
    private final Class<E> type;
    
    protected EnumNameCodec(Class<E> type) {
        super(TypeCodecs.TEXT, GenericType.of(type));
        this.type = type;
    }

    @Override
    protected E innerToOuter(String value) {
        return Enum.valueOf(type, value);
    }

    @Override
    protected String outerToInner(E value) {
        return value.name();
    }

}
