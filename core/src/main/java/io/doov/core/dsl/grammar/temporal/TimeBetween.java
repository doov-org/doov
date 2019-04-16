/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.temporal;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

import io.doov.core.dsl.grammar.Apply3;
import io.doov.core.dsl.grammar.ASTNode;

public class TimeBetween<N extends Temporal> extends Apply3<N,N, ChronoUnit,Long> {

    public TimeBetween(ASTNode<N> input1, ASTNode<N> input2, ASTNode<ChronoUnit> input3) {
        super(Long.class, input1, input2, input3);
    }
}
