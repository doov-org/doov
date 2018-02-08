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

import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.SyntaxTree;

/**
 * Interface for the validation rule that encapsulates the validation algorithm and data.
 * <p>
 * This class should be used when keeping references to specific rules.
 */
public interface ValidationRule extends Readable, SyntaxTree {

    /**
     * Returns a validation rule with the given short circuit.
     *
     * @param shortCircuit the short circuit
     * @return the validation rule
     */
    ValidationRule withShortCircuit(boolean shortCircuit);

    /**
     * Executes the validation rule on the given model.
     *
     * @param model the model
     * @return the result
     */
    Result executeOn(DslModel model);

    /**
     * Registers this rule on the given registry.
     *
     * @param registry the registry
     * @return the validation rule
     */
    ValidationRule registerOn(RuleRegistry registry);

    /**
     * Returns the root node metadata of this validation rule.
     *
     * @return the root metadata
     */
    Metadata getRootMetadata();

}
