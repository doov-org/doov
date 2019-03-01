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
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

public class DefaultStepCondition extends AbstractDSLBuilder implements StepCondition {

    private final PredicateMetadata metadata;
    private final BiPredicate<DslModel, Context> predicate;

    public DefaultStepCondition(PredicateMetadata metadata, BiPredicate<DslModel, Context> predicate) {
        this.metadata = metadata;
        this.predicate = predicate;
    }

    @Override
    public Metadata metadata() {
        return metadata;
    }

    @Override
    public BiPredicate<DslModel, Context> predicate() {
        return (model, context) -> {
            final boolean test = predicate.test(new ModelInterceptor(model, context), context);
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
}
