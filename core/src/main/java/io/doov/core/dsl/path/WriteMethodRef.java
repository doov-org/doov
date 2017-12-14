/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.path;

@FunctionalInterface
public interface WriteMethodRef<T, R> {
    void call(T t, R a1);
}