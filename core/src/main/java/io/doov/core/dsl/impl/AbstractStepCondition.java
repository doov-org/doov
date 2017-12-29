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

import static io.doov.core.dsl.meta.DefaultOperator.match_any;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;

import java.util.function.BiPredicate;

import io.doov.core.dsl.*;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.*;
import io.doov.core.dsl.meta.ast.AstVisitorUtils;

abstract class AbstractStepCondition implements StepCondition {

    private final PredicateMetadata metadata;
    private final BiPredicate<DslModel, Context> predicate;

    protected AbstractStepCondition(PredicateMetadata metadata, BiPredicate<DslModel, Context> predicate) {
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
            // TODO improve speed and safety
            if (metadata.type() == FIELD_PREDICATE) {
                final FieldMetadata fmd = (FieldMetadata) metadata;
                if (fmd.stream().anyMatch(e -> e.getReadable() == match_any)) {
                    final Element field = fmd.stream().findFirst().orElse(null);
                    if (field != null && field.getType() == ElementType.FIELD) {
                        final DslId id = ((DslField) field.getReadable()).id();
                        context.addEvalValue(id, model.get(id));
                    }
                }
            }
            final boolean test = predicate.test(model, context);
            if (test) {
                metadata.incTrueEval();
                context.addEvalTrue(metadata);
            } else {
                metadata.incFalseEval();
                context.addEvalFalse(metadata);
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
