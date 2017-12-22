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
package io.doov.core.dsl.impl;

import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.MetadataVisitor;
import io.doov.core.dsl.meta.ast.AstVisitorUtils;

public class DefaultValidationRule implements ValidationRule {

    private final StepWhen stepWhen;
    private final String message;
    private final boolean shortCircuit;

    protected DefaultValidationRule(StepWhen stepWhen) {
        this(stepWhen, null, true);
    }

    protected DefaultValidationRule(StepWhen stepWhen, String message) {
        this(stepWhen, message, true);
    }

    public DefaultValidationRule(StepWhen stepWhen, String message, boolean shortCircuit) {
        this.stepWhen = stepWhen;
        this.message = message;
        this.shortCircuit = shortCircuit;
    }

    protected StepWhen getStepWhen() {
        return stepWhen;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public ValidationRule withMessage(String message) {
        return new DefaultValidationRule(stepWhen, message);
    }

    @Override
    public ValidationRule withShortCircuit(boolean shortCircuit) {
        return new DefaultValidationRule(stepWhen, message, shortCircuit);
    }

    @Override
    public Result executeOn(DslModel model) {
        final DefaultContext context = new DefaultContext(shortCircuit, stepWhen.stepCondition().getMetadata());
        boolean valid = stepWhen.stepCondition().predicate().test(model, context);
        String readable = valid ? null : (message == null ? stepWhen.stepCondition().readable() : message);
        return new DefaultResult(valid, readable, context);
    }

    @Override
    public ValidationRule registerOn(RuleRegistry registry) {
        registry.register(this);
        return this;
    }

    @Override
    public String readable() {
        return AstVisitorUtils.astToString(this);
    }

    @Override
    public void accept(MetadataVisitor visitor) {
        visitor.start(this);
        stepWhen.accept(visitor);
        visitor.visit(this);
        visitor.end(this);
    }

}
