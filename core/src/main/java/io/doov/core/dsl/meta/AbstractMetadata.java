/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

public abstract class AbstractMetadata implements Metadata {

    @Override
    public String toString() {
        return readable();
    }
}
