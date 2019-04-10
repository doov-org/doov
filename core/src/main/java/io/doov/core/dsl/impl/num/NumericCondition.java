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
package io.doov.core.dsl.impl.num;

import static io.doov.core.dsl.meta.function.NumericFunctionMetadata.greaterOrEqualsMetadata;
import static io.doov.core.dsl.meta.function.NumericFunctionMetadata.greaterThanMetadata;
import static io.doov.core.dsl.meta.function.NumericFunctionMetadata.lesserOrEqualsMetadata;
import static io.doov.core.dsl.meta.function.NumericFunctionMetadata.lesserThanMetadata;

import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.FieldInfo;
import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.field.types.NumericFieldInfo;
import io.doov.core.dsl.grammar.leaf.Constant;
import io.doov.core.dsl.grammar.leaf.NotYetImplemented;
import io.doov.core.dsl.grammar.numeric.*;
import io.doov.core.dsl.impl.DefaultCondition;
import io.doov.core.dsl.impl.LeafStepCondition;
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

    protected NumericCondition(PredicateMetadata metadata, BiFunction<FieldModel, Context, Optional<N>> value) {
        super(metadata, value);
    }

    /**
     * Returns a step condition checking if the node value is lesser than the given value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition lesserThan(N value) {
        return LeafStepCondition.stepCondition(lesserThanMetadata(metadata, value),
                new Lesser<>(ast, new Constant<>(value)),
                getFunction(), value,
                (l, r) -> lesserThanFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is lesser than the given field value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition lesserThan(NumericFieldInfo<N> value) {
        return LeafStepCondition.stepCondition(
                lesserThanMetadata(metadata, value),
                new NotYetImplemented<>(NumericFieldInfo.class),
                getFunction(), value,
                (l, r) -> lesserThanFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is lesser or equals the given value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition lesserOrEquals(N value) {
        return LeafStepCondition.stepCondition(
                lesserOrEqualsMetadata(metadata, value),
                new LesserEq<>(ast, new Constant<>(value)),
                getFunction(), value,
                (l, r) -> lesserOrEqualsFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is lesser or equals the given field value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition lesserOrEquals(NumericFieldInfo<N> value) {
        return LeafStepCondition.stepCondition(
                lesserOrEqualsMetadata(metadata, value),
                new NotYetImplemented<>(NumericFieldInfo.class),
                getFunction(), value,
                (l, r) -> lesserOrEqualsFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is greater than the given value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition greaterThan(N value) {
        return LeafStepCondition.stepCondition(
                greaterThanMetadata(metadata, value),
                new Greater<>(ast, new Constant<>(value)),
                getFunction(), value,
                (l, r) -> greaterThanFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is greater than the given field value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition greaterThan(NumericFieldInfo<N> value) {
        return LeafStepCondition.stepCondition(
                greaterThanMetadata(metadata, value),
                new NotYetImplemented<>(NumericFieldInfo.class),
                getFunction(), value,
                (l, r) -> greaterThanFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is greater or equals the given value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition greaterOrEquals(N value) {
        return LeafStepCondition.stepCondition(greaterOrEqualsMetadata(metadata, value),
                new GreaterEq<>(ast, new Constant<>(value)),
                getFunction(), value,
                (l, r) -> greaterOrEqualsFunction().apply(l, r));
    }

    /**
     * Returns a step condition checking if the node value is greater or equals the given field value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition greaterOrEquals(NumericFieldInfo<N> value) {
        return LeafStepCondition.stepCondition(greaterOrEqualsMetadata(metadata, value),
                new NotYetImplemented<>(NumericFieldInfo.class),
                getFunction(), value,
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

}
