/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.doov.core.dsl.impl;

import static io.doov.core.dsl.meta.predicate.BinaryPredicateMetadata.andMetadata;
import static io.doov.core.dsl.meta.predicate.BinaryPredicateMetadata.orMetadata;

import java.util.function.BiPredicate;

import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.predicate.BinaryPredicateMetadata;

/**
 * Implement logical conditions like and, or.
 */
public class LogicalBinaryCondition extends DefaultStepCondition {

    private LogicalBinaryCondition(BinaryPredicateMetadata metadata, BiPredicate<DslModel, Context> predicate) {
        super(metadata, predicate);
    }

    /**
     * Returns a binary condition that returns true if the given left and right conditions evaluate to true.
     * <p>
     * This operation depends on {@link Context#isShortCircuit()}. It won't change the predicate value, but might
     * impact performance and tree evaluation.
     *
     * @param left  the left condition
     * @param right the right condition
     * @return the binary condition
     */
    public static LogicalBinaryCondition and(StepCondition left, StepCondition right) {
        return new LogicalBinaryCondition(andMetadata(left.metadata(), right.metadata()),
                        (model, context) -> context.isShortCircuit()
                                        ? andShortCircuit(left, right, model, context)
                                        : and(left, right, model, context));
    }

    private static boolean and(StepCondition left, StepCondition right, DslModel model, Context context) {
        boolean leftResult = left.predicate().test(model, context);
        boolean rightResult = right.predicate().test(model, context);
        return leftResult && rightResult;
    }

    private static boolean andShortCircuit(StepCondition left, StepCondition right, DslModel model, Context context) {
        return left.predicate().and(right.predicate()).test(model, context);
    }

    /**
     * Returns a binary condition that returns true if the given left or right conditions evaluate to true.
     * <p>
     * This operation depends on {@link Context#isShortCircuit()}. It won't change the predicate value, but might
     * impact performance and tree evaluation.
     *
     * @param left  the left condition
     * @param right the right condition
     * @return the binary condition
     */
    public static LogicalBinaryCondition or(StepCondition left, StepCondition right) {
        return new LogicalBinaryCondition(orMetadata(left.metadata(), right.metadata()),
                        (model, context) -> context.isShortCircuit()
                                        ? orShortCircuit(left, right, model, context)
                                        : or(left, right, model, context));
    }

    private static boolean or(StepCondition left, StepCondition right, DslModel model, Context context) {
        boolean leftResult = left.predicate().test(model, context);
        boolean rightResult = right.predicate().test(model, context);
        return leftResult || rightResult;
    }

    private static boolean orShortCircuit(StepCondition left, StepCondition right, DslModel model, Context context) {
        return left.predicate().or(right.predicate()).test(model, context);
    }

}
