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
package io.doov.assertions;

import org.assertj.core.api.AbstractAssert;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.lang.ValidationRule;

/**
 * Assertion for {@link StepCondition}.
 */
public class StepConditionAssert extends AbstractAssert<StepConditionAssert, StepCondition> {

    StepConditionAssert(StepCondition condition, Class<?> selfType) {
        super(condition, selfType);
    }

    /**
     * Verifies that the result is true for the given model.
     *
     * @param model the model
     * @return the assert
     * @see ValidationRuleAssert#validates(DslModel)
     */
    public ValidationRuleAssert validates(DslModel model) {
        ValidationRule rule = DOOV.when(actual).validate();
        ValidationRuleAssert validationRuleAssert = new ValidationRuleAssert(rule, ValidationRuleAssert.class);
        validationRuleAssert.validates(model);
        return validationRuleAssert;
    }

    /**
     * Verifies that the result is false for the given model.
     *
     * @param model the model
     * @return the assert
     * @see ValidationRuleAssert#doesNotValidate(DslModel)
     */
    public ValidationRuleAssert doesNotValidate(DslModel model) {
        ValidationRule rule = DOOV.when(actual).validate();
        ValidationRuleAssert validationRuleAssert = new ValidationRuleAssert(rule, ValidationRuleAssert.class);
        validationRuleAssert.doesNotValidate(model);
        return validationRuleAssert;
    }

}
