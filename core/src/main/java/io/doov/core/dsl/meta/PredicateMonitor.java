/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

public interface PredicateMonitor {

    int incTrueEval();

    int incFalseEval();

    int trueEvalCount();

    int falseEvalCount();

}
