/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.lang;

public abstract class AbstractDSLBuilder implements DSLBuilder {

    @Override
    public String toString() {
        return readable();
    }
}
