/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.serial;

import java.util.stream.Stream;

public interface TypeAdapterRegistry {

    Stream<TypeAdapter> stream();
}
