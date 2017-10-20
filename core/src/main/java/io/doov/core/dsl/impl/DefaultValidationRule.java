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

import static java.text.MessageFormat.format;

import java.util.ArrayList;
import java.util.List;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.Metadata;

public class DefaultValidationRule implements ValidationRule {

    private static final String READABLE_VALIDATE_WITH_EMPTY_MESSAGE = "{0}, validate with empty message";
    private static final String READABLE_VALIDATE_WITH_MESSAGE = "{0}, validate with message \"{1}\"";

    private final StepWhen stepWhen;
    private final String message;

    DefaultValidationRule(StepWhen stepWhen) {
        this(stepWhen, null);
    }

    private DefaultValidationRule(StepWhen stepWhen, String message) {
        this.stepWhen = stepWhen;
        this.message = message;
    }

    @Override
    public ValidationRule withMessage(String message) {
        return new DefaultValidationRule(stepWhen, message);
    }

    @Override
    public String readable() {
        String pattern = message == null ? READABLE_VALIDATE_WITH_EMPTY_MESSAGE : READABLE_VALIDATE_WITH_MESSAGE;
        return format(pattern, stepWhen.readable(), message);
    }

    @Override
    public Result executeOn(FieldModel model) {
        List<Metadata> metadatas = new ArrayList<>();
        boolean valid = stepWhen.stepCondition().predicate().test(model, metadatas::add);
        String readable = valid ? null : (message == null ? stepWhen.stepCondition().readable() : message);
        return new DefaultResult(valid, readable, metadatas);
    }

    @Override
    public ValidationRule registerOn(RuleRegistry registry) {
        registry.register(this);
        return this;
    }

    @Override
    public ValidationRule registerOn(RuleRegistry registry, RuleId id) {
        registry.register(this, id);
        return this;
    }

}
