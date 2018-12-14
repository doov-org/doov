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
package io.doov.core.dsl.impl.time;

import static io.doov.core.dsl.meta.predicate.BinaryPredicateMetadata.equalsMetadata;

import java.time.temporal.Temporal;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.field.types.TemporalFieldInfo;
import io.doov.core.dsl.impl.*;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.function.TemporalBiFunctionMetadata;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

/**
 * Base class for temporal conditions.
 * <p>
 * It contains a {@link DslField} to get the value from the model, a {@link PredicateMetadata} to describe this node,
 * and a {@link BiFunction} to take the value from the model and return an optional value.
 *
 * @param <N> the type of the field value
 */
public abstract class TemporalCondition<N extends Temporal> extends DefaultCondition<N>
        implements TemporalOperators<N> {

    public TemporalCondition(DslField<N> field) {
        super(field);
    }

    public TemporalCondition(PredicateMetadata metadata, BiFunction<DslModel, Context, Optional<N>> value) {
        super(metadata, value);
    }

    /**
     * Returns a condition checking if the node value is equal to the given condition value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition eq(TemporalCondition<N> value) {
        return LeafStepCondition.stepCondition(equalsMetadata(metadata, value), getFunction(), value.getFunction(),
                Object::equals);
    }

    /**
     * Returns a condition checking if the node value is before the given value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition before(N value) {
        return LeafStepCondition.stepCondition(TemporalBiFunctionMetadata.beforeValueMetadata(this, value), getFunction(), value,
                (l, r) -> beforeFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is before the given field value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition before(TemporalFieldInfo<N> value) {
        return LeafStepCondition.stepCondition(TemporalBiFunctionMetadata.beforeTemporalFieldMetadata(this, value), getFunction(), value,
                (l, r) -> beforeFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is before the supplier value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition before(Supplier<N> value) {
        return LeafStepCondition.stepCondition(TemporalBiFunctionMetadata.beforeSupplierMetadata(this, value), getFunction(), value,
                (l, r) -> beforeFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is before the condition value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition before(TemporalCondition<N> value) {
        return LeafStepCondition.stepCondition(TemporalBiFunctionMetadata.beforeTemporalConditionMetadata(this, value), getFunction(),
                value.getFunction(),
                (l, r) -> beforeFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is before or equals the value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition beforeOrEq(N value) {
        return LeafStepCondition.stepCondition(TemporalBiFunctionMetadata.beforeOrEqualsValueMetadata(this, value), getFunction(), value,
                (l, r) -> beforeOrEqualsFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is before the given field value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition beforeOrEq(TemporalFieldInfo<N> value) {
        return LeafStepCondition.stepCondition(TemporalBiFunctionMetadata.beforeOrEqTemporalFieldMetadata(this, value), getFunction(), value,
                (l, r) -> beforeOrEqualsFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is before or equals the supplier value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition beforeOrEq(Supplier<N> value) {
        return LeafStepCondition.stepCondition(TemporalBiFunctionMetadata.beforeOrEqualsSupplierMetadata(this, value), getFunction(), value,
                (l, r) -> beforeOrEqualsFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is before or equals the condition value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition beforeOrEq(TemporalCondition<N> value) {
        return LeafStepCondition.stepCondition(TemporalBiFunctionMetadata.beforeOrEqualsTemporalConditionMetadata(this, value), getFunction(),
                value.getFunction(), (l, r) -> beforeOrEqualsFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is after the value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition after(N value) {
        return LeafStepCondition.stepCondition(TemporalBiFunctionMetadata.afterValueMetadata(this, value), getFunction(), value,
                (l, r) -> afterFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is after the field value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition after(TemporalFieldInfo<N> value) {
        return LeafStepCondition.stepCondition(TemporalBiFunctionMetadata.afterTemporalFieldMetadata(this, value), getFunction(), value,
                (l, r) -> afterFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is after the supplier value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition after(Supplier<N> value) {
        return LeafStepCondition.stepCondition(TemporalBiFunctionMetadata.afterSupplierMetadata(this, value), getFunction(), value,
                (l, r) -> afterFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is after the condition value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition after(TemporalCondition<N> value) {
        return LeafStepCondition.stepCondition(TemporalBiFunctionMetadata.afterTemporalConditionMetadata(this, value), getFunction(),
                value.getFunction(), (l, r) -> afterFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is after or equals the value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition afterOrEq(N value) {
        return LeafStepCondition.stepCondition(TemporalBiFunctionMetadata.afterOrEqualsValueMetadata(this, value), getFunction(), value,
                (l, r) -> afterOrEqualsFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is after the field value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition afterOrEq(TemporalFieldInfo<N> value) {
        return LeafStepCondition.stepCondition(TemporalBiFunctionMetadata.afterOrEqTemporalFieldMetadata(this, value), getFunction(), value,
                (l, r) -> afterOrEqualsFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is after or equals the supplier value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition afterOrEq(Supplier<N> value) {
        return LeafStepCondition.stepCondition(TemporalBiFunctionMetadata.afterOrEqualsSupplierMetadata(this, value), getFunction(), value,
                (l, r) -> afterOrEqualsFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is after or equals the condition value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition afterOrEq(TemporalCondition<N> value) {
        return LeafStepCondition.stepCondition(TemporalBiFunctionMetadata.afterOrEqualsTemporalConditionMetadata(this, value), getFunction(),
                value.getFunction(), (l, r) -> afterOrEqualsFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is between the given min inclusive and max exclusive values.
     *
     * @param minIncluded the min value included
     * @param maxExcluded the max value excluded
     * @return the step condition
     */
    public final StepCondition between(N minIncluded, N maxExcluded) {
        return LogicalBinaryCondition.and(beforeOrEq(maxExcluded), afterOrEq(minIncluded));
    }

    /**
     * Returns a condition checking if the node value is between the given min inclusive and max exclusive supplier
     * values.
     *
     * @param minIncluded the min value included
     * @param maxExcluded the max value excluded
     * @return the step condition
     */
    public final StepCondition between(Supplier<N> minIncluded, Supplier<N> maxExcluded) {
        return LogicalBinaryCondition.and(beforeOrEq(maxExcluded), afterOrEq(minIncluded));
    }

    /**
     * Returns a condition checking if the node value is between the given min inclusive and max exclusive condition
     * values.
     *
     * @param minIncluded the min value included
     * @param maxExcluded the max value excluded
     * @return the step condition
     */
    public final StepCondition between(TemporalCondition<N> minIncluded, TemporalCondition<N> maxExcluded) {
        return LogicalBinaryCondition.and(beforeOrEq(maxExcluded), afterOrEq(minIncluded));
    }

    /**
     * Returns a condition checking if the node value is not between the given min inclusive and max exclusive values.
     *
     * @param minIncluded the min value included
     * @param maxExcluded the max value excluded
     * @return the step condition
     */
    public final StepCondition notBetween(N minIncluded, N maxExcluded) {
        return LogicalUnaryCondition.negate(between(minIncluded, maxExcluded));
    }

    /**
     * Returns a condition checking if the node value is not between the given min inclusive and max exclusive supplier
     * values.
     *
     * @param minIncluded the min value included
     * @param maxExcluded the max value excluded
     * @return the step condition
     */
    public final StepCondition notBetween(Supplier<N> minIncluded, Supplier<N> maxExcluded) {
        return LogicalUnaryCondition.negate(between(minIncluded, maxExcluded));
    }

    /**
     * Returns a condition checking if the node value is not between the given min inclusive and max exclusive condition
     * values.
     *
     * @param minIncluded the min value included
     * @param maxExcluded the max value excluded
     * @return the step condition
     */
    public final StepCondition notBetween(TemporalCondition<N> minIncluded, TemporalCondition<N> maxExcluded) {
        return LogicalUnaryCondition.negate(between(minIncluded, maxExcluded));
    }
}
