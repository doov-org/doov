/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class PredicateMetadata implements Metadata, PredicateMonitor {
    private final AtomicInteger validated = new AtomicInteger();
    private final AtomicInteger invalidated = new AtomicInteger();

    @Override
    public int incrementValidated() {
        return validated.incrementAndGet();
    }

    @Override
    public int incrementInvalidated() {
        return invalidated.incrementAndGet();
    }

    @Override
    public int validatedCount() {
        return validated.get();
    }

    @Override
    public int invalidatedCount() {
        return invalidated.get();
    }
}
