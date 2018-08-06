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

import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToString;

import java.util.Locale;

import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.MetadataVisitor;

public class DefaultValidationRule implements ValidationRule {

    private final StepWhen stepWhen;
    private final boolean shortCircuit;

    public DefaultValidationRule(StepWhen stepWhen) {
        this(stepWhen, true);
    }

    public DefaultValidationRule(StepWhen stepWhen, boolean shortCircuit) {
        this.stepWhen = stepWhen;
        this.shortCircuit = shortCircuit;
    }

    protected boolean isShortCircuit() {
        return shortCircuit;
    }

    protected StepWhen getStepWhen() {
        return stepWhen;
    }

    @Override
    public ValidationRule withShortCircuit(boolean shortCircuit) {
        return new DefaultValidationRule(stepWhen, shortCircuit);
    }

    @Override
    public Result executeOn(DslModel model) {
        return executeOn(model, new DefaultContext(shortCircuit, stepWhen.stepCondition().getMetadata()));
    }

    @Override
    public Result executeOn(DslModel model, Context context) {
        boolean valid = stepWhen.stepCondition().predicate().test(model, context);
        return new DefaultResult(valid, context);
    }

    @Override
    public ValidationRule registerOn(RuleRegistry registry) {
        registry.register(this);
        return this;
    }

    @Override
    public String readable(Locale locale) {
        return astToString(this, locale).trim();
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        visitor.start(this, depth);
        stepWhen.accept(visitor, depth);
        visitor.visit(this, depth);
        visitor.end(this, depth);
    }

    @Override
    public Metadata getRootMetadata() {
        if (stepWhen == null)
            return null;
        if (stepWhen.stepCondition() == null)
            return null;
        return stepWhen.stepCondition().getMetadata();
    }

}
