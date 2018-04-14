/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package io.doov.core.dsl.lang;

import io.doov.core.dsl.meta.SyntaxTree;

/**
 * Interface for the when step that encapsulates the root condition of the syntax tree and the validation rule.
 */
public interface StepWhen extends SyntaxTree {
    /**
     * Returns the root condition of the syntax tree.
     *
     * @return the step condition
     */
    StepCondition stepCondition();

    /**
     * Returns a validation rule.
     *
     * @return the validation rule
     */
    ValidationRule validate();

    /**
     * Return a conditional mapping rule containing all the given mapping rules. The conditional mapping rule will
     * execute when this validation rule is valid.
     *
     * @param mapRule mapping rules
     * @return the conditional mapping rule
     */
    ConditionalMappingRule then(MappingRule... mapRule);

}
