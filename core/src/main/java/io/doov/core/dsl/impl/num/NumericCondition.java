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
package io.doov.core.dsl.impl.num;

import static io.doov.core.dsl.impl.LeafStepCondition.predicate;
import static io.doov.core.dsl.meta.function.NumericFunctionMetadata.greaterOrEqualsMetadata;
import static io.doov.core.dsl.meta.function.NumericFunctionMetadata.greaterThanMetadata;
import static io.doov.core.dsl.meta.function.NumericFunctionMetadata.lesserOrEqualsMetadata;
import static io.doov.core.dsl.meta.function.NumericFunctionMetadata.lesserThanMetadata;
import static io.doov.core.dsl.meta.predicate.LeafPredicateMetadata.whenMetadata;

import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.field.types.NumericFieldInfo;
import io.doov.core.dsl.impl.DefaultCondition;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

/**
 * Base class for numeric conditions.
 * <p>
 * It contains a {@link DslField} to get the value from the model, a {@link PredicateMetadata} to describe this node,
 * and a {@link BiFunction} to take the value from the model and return an optional value.
 *
 * @param <N> the type of the field value
 */
public abstract class NumericCondition<N extends Number> extends DefaultCondition<N> implements NumericOperators<N> {

    protected NumericCondition(DslField<N> field) {
        super(field);
    }

    protected NumericCondition(PredicateMetadata metadata, BiFunction<DslModel, Context, Optional<N>> value) {
        super(metadata, value);
    }

    protected abstract NumericCondition<N> numericCondition(PredicateMetadata metadata,
            BiFunction<DslModel, Context, Optional<N>> value);

    /**
     * Returns a step condition checking if the node value is lesser than the given value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition lesserThan(N value) {
        return predicate(this, lesserThanMetadata(metadata, value), value,
                (l, r) -> lesserThanFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is lesser than the given field value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition lesserThan(NumericFieldInfo<N> value) {
        return predicate(this, lesserThanMetadata(metadata, value), value,
                (l, r) -> lesserThanFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is lesser or equals the given value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition lesserOrEquals(N value) {
        return predicate(this, lesserOrEqualsMetadata(metadata, value), value,
                (l, r) -> lesserOrEqualsFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is lesser or equals the given field value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition lesserOrEquals(NumericFieldInfo<N> value) {
        return predicate(this, lesserOrEqualsMetadata(metadata, value), value,
                (l, r) -> lesserOrEqualsFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is greater than the given value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition greaterThan(N value) {
        return predicate(this, greaterThanMetadata(metadata, value), value,
                (l, r) -> greaterThanFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is greater than the given field value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition greaterThan(NumericFieldInfo<N> value) {
        return predicate(this, greaterThanMetadata(metadata, value), value,
                (l, r) -> greaterThanFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is greater or equals the given value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition greaterOrEquals(N value) {
        return predicate(this, greaterOrEqualsMetadata(metadata, value), value,
                (l, r) -> greaterOrEqualsFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is greater or equals the given field value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition greaterOrEquals(NumericFieldInfo<N> value) {
        return predicate(this, greaterOrEqualsMetadata(metadata, value), value,
                (l, r) -> greaterOrEqualsFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is between the given min included and max excluded values.
     *
     * @param minIncluded the min value included
     * @param maxExcluded the max value excluded
     * @return the step condition
     */
    public final StepCondition between(N minIncluded, N maxExcluded) {
        return greaterOrEquals(minIncluded).and(lesserThan(maxExcluded));
    }

    /**
     * Returns a step condition checking if the node value is between the given min included and max excluded field
     * values.
     *
     * @param minIncluded the min value included
     * @param maxExcluded the max value excluded
     * @return the step condition
     */
    public final StepCondition between(NumericFieldInfo<N> minIncluded, NumericFieldInfo<N> maxExcluded) {
        return greaterOrEquals(minIncluded).and(lesserThan(maxExcluded));
    }

    /**
     * Returns a numeric step condition that returns the node value if the condition evaluates to true.
     *
     * @param condition the condition to test
     * @return the numeric condition
     */
    public final NumericCondition<N> when(StepCondition condition) {
        return numericCondition(whenMetadata(metadata, condition),
                (model, context) -> condition.predicate().test(model, context) ? value(model, context)
                        : Optional.empty());
    }
}
