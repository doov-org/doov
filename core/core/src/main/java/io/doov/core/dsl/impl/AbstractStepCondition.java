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

import java.util.function.BiPredicate;

import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.MetadataVisitor;
import io.doov.core.dsl.meta.ast.AstVisitorUtils;

abstract class AbstractStepCondition implements StepCondition {

    private final Metadata metadata;
    private final BiPredicate<DslModel, Context> predicate;

    protected AbstractStepCondition(Metadata metadata, BiPredicate<DslModel, Context> predicate) {
        this.metadata = metadata;
        this.predicate = predicate;
    }

    @Override
    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public BiPredicate<DslModel, Context> predicate() {
        return (model, context) -> {
            boolean test = predicate.test(model, context);
            if (!test) {
                context.failed(metadata);
            }
            return test;
        };
    }

    @Override
    public String readable() {
        return AstVisitorUtils.astToString(this);
    }

    @Override
    public void accept(MetadataVisitor visitor) {
        visitor.visit(this);
        metadata.accept(visitor);
    }

}
