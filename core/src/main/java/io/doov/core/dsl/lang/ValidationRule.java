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

import io.doov.core.FieldModel;
import io.doov.core.dsl.grammar.Value;

/**
 * Interface for the validation rule that encapsulates the validation algorithm and data.
 * <p>
 * This class should be used when keeping references to specific rules.
 */
public interface ValidationRule extends DSLBuilder<Boolean> {

    /**
     * Returns the root when node of this rule.
     *
     * @return the step when
     */
    StepWhen getStepWhen();

    @Override
    Value<Boolean> ast();

    /**
     * Returns a validation rule with the given short circuit.
     *
     * @param shortCircuit the short circuit
     * @return the validation rule
     */
    ValidationRule withShortCircuit(boolean shortCircuit);

    /**
     * Executes the validation rule on a null model.
     *
     * @return the result
     */
    default Result execute() {
        return executeOn(null);
    }

    /**
     * Executes the validation rule on the given model.
     *
     * @param model the model
     * @return the result
     */
    Result executeOn(FieldModel model);

    /**
     * Executes the validation rule on the given model.
     *
     * @param model the model
     * @param context custom context
     * @return the result
     */
    Result executeOn(FieldModel model, Context context);

}
