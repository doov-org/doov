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
package io.doov.core.dsl.lang;

import java.util.function.BiPredicate;

import io.doov.core.FieldModel;
import io.doov.core.dsl.grammar.Value;
import io.doov.core.dsl.impl.LogicalBinaryCondition;
import io.doov.core.dsl.impl.LogicalUnaryCondition;

/**
 * Interface for the condition which corresponds to a node in the syntax tree.
 * <p>
 * A condition is represented by a predicate {@link #predicate()} and a {@link #metadata()} describing the node.
 */
public interface StepCondition extends DSLBuilder<Boolean> {

    /**
     * Returns the predicate for this node value.
     *
     * @return the predicate
     */
    BiPredicate<FieldModel, Context> predicate();

    /**
     * Returns a condition checking if the node predicate and the given condition predicate evaluate to true.
     *
     * @param condition the right side condition
     * @return the step condition
     */
    default StepCondition and(StepCondition condition) {
        return LogicalBinaryCondition.and(this, condition);
    }

    /**
     * Returns a condition checking if the node predicate or the given condition predicate evaluate to true.
     *
     * @param condition the right side condition
     * @return the step condition
     */
    default StepCondition or(StepCondition condition) {
        return LogicalBinaryCondition.or(this, condition);
    }

    /**
     * Returns a condition checking if the node predicate does not evaluate to true.
     *
     * @return the step condition
     */
    default StepCondition not() {
        return LogicalUnaryCondition.negate(this);
    }
}
