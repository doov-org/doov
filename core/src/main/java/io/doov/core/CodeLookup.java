/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Enum type interface with overridable {@link #parseCode} implementation.
 *
 * @param <E> Enum type
 */
public interface CodeLookup<E extends Enum<E> & CodeValuable> extends CodeValuable {

    default E parseCode(String code) {
        return stream().filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
    }

    @SuppressWarnings("unchecked")
    default Stream<E> stream() {
        return Arrays.stream((E[]) getClass().getEnumConstants());
    }

}
