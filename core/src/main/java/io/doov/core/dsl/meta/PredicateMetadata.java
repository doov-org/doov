/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class PredicateMetadata implements Metadata, PredicateMonitor {

    private final AtomicInteger trueEval = new AtomicInteger();
    private final AtomicInteger falseEval = new AtomicInteger();

    @Override
    public int incTrueEval() {
        return trueEval.incrementAndGet();
    }

    @Override
    public int incFalseEval() {
        return falseEval.incrementAndGet();
    }

    @Override
    public int trueEvalCount() {
        return trueEval.get();
    }

    @Override
    public int falseEvalCount() {
        return falseEval.get();
    }

    @Override
    public String toString() {
        return readable();
    }

}
