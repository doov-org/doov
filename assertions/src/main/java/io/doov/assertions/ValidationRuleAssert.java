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

import io.doov.core.FieldModel;
import org.assertj.core.api.AbstractAssert;

import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.ValidationRule;

/**
 * Assertion for {@link ValidationRule}.
 */
public class ValidationRuleAssert extends AbstractAssert<ValidationRuleAssert, ValidationRule> {

    ValidationRuleAssert(ValidationRule rule, Class<?> selfType) {
        super(rule, selfType);
    }

    /**
     * Verifies that the result is true for the given model.
     *
     * @param model the model
     * @return the assert
     * @see ResultAssert#isTrue()
     */
    public ResultAssert validates(FieldModel model) {
        Result result = actual.executeOn(model);
        ResultAssert resultAssert = new ResultAssert(result, ResultAssert.class);
        resultAssert.isTrue();
        return resultAssert;
    }

    /**
     * Verifies that the result is false for the given model.
     *
     * @param model the model
     * @return the assert
     * @see ResultAssert#isFalse()
     */
    public ResultAssert doesNotValidate(FieldModel model) {
        Result result = actual.executeOn(model);
        ResultAssert resultAssert = new ResultAssert(result, ResultAssert.class);
        resultAssert.isFalse();
        return resultAssert;
    }

    /**
     * Verifies that the result is true for the given model.
     * <p>
     * TODO move to LesFurets
     *
     * @param model the model
     * @see #validates(FieldModel)
     */
    public void excludes(FieldModel model) {
        validates(model);
    }

    /**
     * Verifies that the result is false for the given model.
     * <p>
     * TODO move to LesFurets
     *
     * @param model the model
     * @see #doesNotValidate(FieldModel)
     */
    public void doesNotExclude(FieldModel model) {
        doesNotValidate(model);
    }

}
