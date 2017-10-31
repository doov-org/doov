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

import static io.doov.core.dsl.meta.FieldMetadata.greaterOrEqualsMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.greaterThanMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.lesserOrEqualsMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.lesserThanMetadata;

import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.dsl.SimpleFieldId;
import io.doov.core.dsl.SimpleModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.Metadata;

public abstract class NumericCondition<N extends Number>
                extends DefaultCondition<N> {

    NumericCondition(SimpleFieldId<N> field) {
        super(field);
    }

    NumericCondition(Metadata metadata, BiFunction<SimpleModel, Context, Optional<N>> value) {
        super(metadata, value);
    }

    // lesser than

    public final StepCondition lesserThan(N value) {
        return predicate(lesserThanMetadata(field, value),
                        (model, context) -> Optional.ofNullable(value),
                        (l, r) -> lesserThanFunction().apply(l, r));
    }

    public final StepCondition lesserThan(SimpleFieldId<N> value) {
        return predicate(lesserThanMetadata(field, value),
                        (model, context) -> value(model, value),
                        (l, r) -> lesserThanFunction().apply(l, r));
    }

    public final StepCondition lesserOrEquals(N value) {
        return predicate(lesserOrEqualsMetadata(field, value),
                        (model, context) -> Optional.ofNullable(value),
                        (l, r) -> lesserOrEqualsFunction().apply(l, r));
    }

    public final StepCondition lesserOrEquals(SimpleFieldId<N> value) {
        return predicate(lesserOrEqualsMetadata(field, value),
                        (model, context) -> value(model, value),
                        (l, r) -> lesserOrEqualsFunction().apply(l, r));
    }

    public abstract BiFunction<N, N, Boolean> lesserThanFunction();

    public abstract BiFunction<N, N, Boolean> lesserOrEqualsFunction();

    // greater than

    public final StepCondition greaterThan(N value) {
        return predicate(greaterThanMetadata(field, value),
                        (model, context) -> Optional.ofNullable(value),
                        (l, r) -> greaterThanFunction().apply(l, r));
    }

    public final StepCondition greaterThan(SimpleFieldId<N> value) {
        return predicate(greaterThanMetadata(field, value),
                        (model, context) -> value(model, value),
                        (l, r) -> greaterThanFunction().apply(l, r));
    }

    public final StepCondition greaterOrEquals(N value) {
        return predicate(greaterOrEqualsMetadata(field, value),
                        (model, context) -> Optional.ofNullable(value),
                        (l, r) -> greaterOrEqualsFunction().apply(l, r));
    }

    public final StepCondition greaterOrEquals(SimpleFieldId<N> value) {
        return predicate(greaterOrEqualsMetadata(field, value),
                        (model, context) -> value(model, value),
                        (l, r) -> greaterOrEqualsFunction().apply(l, r));
    }

    public abstract BiFunction<N, N, Boolean> greaterThanFunction();

    public abstract BiFunction<N, N, Boolean> greaterOrEqualsFunction();

    // between

    public final StepCondition between(N minIncluded, N maxExcluded) {
        return greaterOrEquals(minIncluded).and(lesserThan(maxExcluded));
    }

    public final StepCondition between(SimpleFieldId<N> minIncluded, SimpleFieldId<N> maxExcluded) {
        return greaterOrEquals(minIncluded).and(lesserThan(maxExcluded));
    }

}
