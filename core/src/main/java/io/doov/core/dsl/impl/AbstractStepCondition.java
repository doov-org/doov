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
import java.util.function.BiPredicate;

import io.doov.core.dsl.DslId;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.*;

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
            final boolean test = predicate.test(new Interceptor(model, context), context);
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

    private static final class Interceptor implements DslModel {
        private final DslModel model;
        private final Context context;

        Interceptor(DslModel model, Context context) {
            this.model = model;
            this.context = context;
        }

        @Override
        public <T> T get(DslId id) {
            final T value = model.get(id);
            context.addEvalValue(id, value);
            return value;
        }
    }

    @Override
    public String readable() {
        return astToString(this, Locale.getDefault());
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        visitor.visit(this, depth);
        metadata.accept(visitor, depth);
    }

}
