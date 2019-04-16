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

import io.doov.core.dsl.grammar.ASTNode;
import io.doov.core.dsl.grammar.When;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.mapping.DefaultConditionalMappingRule;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.WhenMetadata;

public class DefaultStepWhen extends AbstractDSLBuilder<Boolean> implements StepWhen {
    private final WhenMetadata metadata;
    private final StepCondition stepCondition;

    private final ASTNode<Boolean> input;

    public DefaultStepWhen(StepCondition stepCondition) {
        this.metadata = WhenMetadata.when(stepCondition.metadata());
        this.stepCondition = stepCondition;
        this.input = stepCondition.ast();
    }

    @Override
    public ASTNode<Boolean> ast() {
        return new When(input);
    }

    @Override
    public Metadata metadata() {
        return metadata;
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
}
