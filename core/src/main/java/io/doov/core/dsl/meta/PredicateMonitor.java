/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

public interface PredicateMonitor {
    int incrementValidated();
    
    int incrementInvalidated();
    
    int validatedCount();

    int invalidatedCount();
}
