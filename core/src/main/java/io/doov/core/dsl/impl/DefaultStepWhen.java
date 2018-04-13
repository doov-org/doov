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

import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToString;

import java.util.Locale;

import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.mapping.DefaultConditionalMappingRule;
import io.doov.core.dsl.meta.MetadataVisitor;

public class DefaultStepWhen implements StepWhen {

    private final StepCondition stepCondition;

    public DefaultStepWhen(StepCondition stepCondition) {
        this.stepCondition = stepCondition;
    }

    @Override
    public StepCondition stepCondition() {
        return stepCondition;
    }

    @Override
    public ValidationRule validate() {
        return new DefaultValidationRule(this);
    }

    @Override
    public ConditionalMappingRule then(MappingRule... mappingRule) {
        return new DefaultConditionalMappingRule(this, mappingRule);
    }

    @Override
    public String readable(Locale locale) {
        return astToString(this, locale);
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        visitor.start(this, depth);
        visitor.visit(this, depth);
        stepCondition.accept(visitor, depth);
        visitor.end(this, depth);
    }

}
