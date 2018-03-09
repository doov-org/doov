/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.serial;

import java.util.stream.Stream;

/**
 * Registry for type adapters.
 * The getAsString/setAsString will call Type adapters in the order returned by this registry.
 */
public interface TypeAdapterRegistry {

    /**
     * The stream of type adapters
     *
     * @return the stream of type adapters
     */
    Stream<TypeAdapter> stream();
}
