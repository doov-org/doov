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

import static io.doov.core.dsl.meta.LeafMetadata.*;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.YEARS;

import java.time.temporal.*;
import java.util.Optional;
import java.util.function.*;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.field.NumericFieldInfo;
import io.doov.core.dsl.field.TemporalFieldInfo;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.PredicateMetadata;
import io.doov.core.dsl.time.TemporalAdjuster;

/**
 * Base class for temporal conditions.
 * <p>
 * It contains a {@link DslField} to get the value from the model, a {@link PredicateMetadata} to describe this node,
 * and a {@link BiFunction} to take the value from the model and return an optional value.
 *
 * @param <N> the type of the field value
 */
public abstract class TemporalCondition<N extends Temporal> extends DefaultCondition<N> {

    public TemporalCondition(DslField field) {
        super(field);
    }

    public TemporalCondition(DslField field, PredicateMetadata metadata,
                    BiFunction<DslModel, Context, Optional<N>> value) {
        super(field, metadata, value);
    }

    abstract TemporalCondition<N> temporalCondition(DslField field, PredicateMetadata metadata,
                    BiFunction<DslModel, Context, Optional<N>> value);

    /**
     * Returns a temporal condition that returns the node value with given temporal adjuster applied.
     *
     * @param adjuster the adjuster
     * @return the temporal condition
     */
    public final TemporalCondition<N> with(TemporalAdjuster adjuster) {
        return temporalCondition(field, metadata.merge(withMetadata(field, adjuster.getMetadata())),
                        (model, context) -> value(model, field)
                                        .map(v -> withFunction(adjuster.getAdjuster()).apply(v)));
    }

    abstract Function<N, N> withFunction(java.time.temporal.TemporalAdjuster adjuster);

    /**
     * Returns a temporal condition that returns the node value minus given temporal value and unit.
     *
     * @param value the minus value
     * @param unit  the minus unit
     * @return the temporal condition
     */
    public final TemporalCondition<N> minus(int value, TemporalUnit unit) {
        return temporalCondition(field, metadata.merge(minusMetadata(field, value, unit)),
                        (model, context) -> value(model, field)
                                        .map(v -> minusFunction(value, unit).apply(v)));
    }

    /**
     * Returns a temporal condition that returns the node value minus given temporal field value and unit.
     *
     * @param value the minus field value
     * @param unit  the minus unit
     * @return the temporal condition
     */
    public final TemporalCondition<N> minus(NumericFieldInfo<Integer> value, TemporalUnit unit) {
        return temporalCondition(field, metadata.merge(minusMetadata(field, value, unit)),
                        (model, context) -> value(model, field)
                                        .flatMap(l -> Optional.ofNullable(model.<Integer> get(value.id()))
                                                        .map(r -> minusFunction(r, unit).apply(l))));
    }

    abstract Function<N, N> minusFunction(int value, TemporalUnit unit);

    /**
     * Returns a temporal condition that returns the node value plus given temporal value and unit.
     *
     * @param value the plus value
     * @param unit  the plus unit
     * @return the temporal condition
     */
    public final TemporalCondition<N> plus(int value, TemporalUnit unit) {
        return temporalCondition(field, metadata.merge(plusMetadata(field, value, unit)),
                        (model, context) -> value(model, field)
                                        .map(v -> plusFunction(value, unit).apply(v)));
    }

    /**
     * Returns a temporal condition that returns the node value plus given temporal field value and unit.
     *
     * @param value the plus field value
     * @param unit  the plus unit
     * @return the temporal condition
     */
    public final TemporalCondition<N> plus(NumericFieldInfo<Integer> value, TemporalUnit unit) {
        return temporalCondition(field, metadata.merge(plusMetadata(field, value, unit)),
                        (model, context) -> value(model, field)
                                        .flatMap(l -> Optional.ofNullable(model.<Integer> get(value.id()))
                                                        .map(r -> plusFunction(r, unit).apply(l))));
    }

    abstract Function<N, N> plusFunction(int value, TemporalUnit unit);

    /**
     * Returns a condition checking if the node value is equal to the given condition value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition eq(TemporalCondition<N> value) {
        return predicate(equalsMetadata(field, value),
                        value.function,
                        Object::equals);
    }

    /**
     * Returns a condition checking if the node value is before the given value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition before(N value) {
        return predicate(beforeValueMetadata(this, value),
                        (model, context) -> Optional.ofNullable(value),
                        (l, r) -> beforeFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is before the given field value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition before(TemporalFieldInfo<N> value) {
        return predicate(beforeTemporalFieldMetadata(this, value),
                        (model, context) -> value(model, value),
                        (l, r) -> beforeFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is before the supplier value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition before(Supplier<N> value) {
        return predicate(beforeSupplierMetadata(this, value),
                        (model, context) -> Optional.ofNullable(value.get()),
                        (l, r) -> beforeFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is before the condition value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition before(TemporalCondition<N> value) {
        return predicate(beforeTemporalConditionMetadata(this, value),
                        value.function,
                        (l, r) -> beforeFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is before or equals the value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition beforeOrEq(N value) {
        return predicate(beforeOrEqualsValueMetadata(this, value),
                        (model, context) -> Optional.ofNullable(value),
                        (l, r) -> beforeOrEqualsFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is before or equals the supplier value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition beforeOrEq(Supplier<N> value) {
        return predicate(beforeOrEqualsSupplierMetadata(this, value),
                        (model, context) -> Optional.ofNullable(value.get()),
                        (l, r) -> beforeOrEqualsFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is before or equals the condition value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition beforeOrEq(TemporalCondition<N> value) {
        return predicate(beforeOrEqualsTemporalConditionMetadata(this, value),
                        value.function,
                        (l, r) -> beforeOrEqualsFunction().apply(l, r));
    }

    abstract BiFunction<N, N, Boolean> beforeFunction();

    abstract BiFunction<N, N, Boolean> beforeOrEqualsFunction();

    /**
     * Returns a condition checking if the node value is after the value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition after(N value) {
        return predicate(afterValueMetadata(this, value),
                        (model, context) -> Optional.ofNullable(value),
                        (l, r) -> afterFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is after the field value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition after(TemporalFieldInfo<N> value) {
        return predicate(afterTemporalFieldMetadata(this, value),
                        (model, context) -> value(model, value),
                        (l, r) -> afterFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is after the supplier value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition after(Supplier<N> value) {
        return predicate(afterSupplierMetadata(this, value),
                        (model, context) -> Optional.ofNullable(value.get()),
                        (l, r) -> afterFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is after the condition value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition after(TemporalCondition<N> value) {
        return predicate(afterTemporalConditionMetadata(this, value),
                        value.function,
                        (l, r) -> afterFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is after or equals the value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition afterOrEq(N value) {
        return predicate(afterOrEqualsValueMetadata(this, value),
                        (model, context) -> Optional.ofNullable(value),
                        (l, r) -> afterOrEqualsFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is after or equals the supplier value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition afterOrEq(Supplier<N> value) {
        return predicate(afterOrEqualsSupplierMetadata(this, value),
                        (model, context) -> Optional.ofNullable(value.get()),
                        (l, r) -> afterOrEqualsFunction().apply(l, r));
    }

    /**
     * Returns a condition checking if the node value is after or equals the condition value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition afterOrEq(TemporalCondition<N> value) {
        return predicate(afterOrEqualsTemporalConditionMetadata(this, value),
                        value.function,
                        (l, r) -> afterOrEqualsFunction().apply(l, r));
    }

    abstract BiFunction<N, N, Boolean> afterFunction();

    abstract BiFunction<N, N, Boolean> afterOrEqualsFunction();

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

    /**
     * Returns a numeric condition that returns the age at the given value.
     *
     * @param value the right side value
     * @return the numeric condition
     */
    public final NumericCondition<Integer> ageAt(N value) {
        return new IntegerCondition(timeBetween(ageAtValueMetadata(this, value), YEARS, value));
    }

    /**
     * Returns a numeric condition that returns the age of this node value at the given field value.
     *
     * @param value the right side value
     * @return the numeric condition
     */
    public final NumericCondition<Integer> ageAt(TemporalFieldInfo<N> value) {
        return new IntegerCondition(timeBetween(ageAtTemporalFieldMetadata(this, value), YEARS, value));
    }

    /**
     * Returns a numeric condition that returns the age of this node value at the given condition value.
     *
     * @param value the right side value
     * @return the numeric condition
     */
    public final NumericCondition<Integer> ageAt(TemporalCondition<N> value) {
        return new IntegerCondition(timeBetween(ageAtTemporalConditionMetadata(this, value), YEARS, value));
    }

    /**
     * Returns a numeric condition that returns the age of this node value at the given supplier value.
     *
     * @param value the right side value
     * @return the numeric condition
     */
    public final NumericCondition<Integer> ageAt(Supplier<N> value) {
        return new IntegerCondition(timeBetween(ageAtSupplierMetadata(this, value), YEARS, value));
    }

    /**
     * Returns a numeric condition that returns the days between this node value and the given value.
     *
     * @param value the right side value
     * @return the numeric condition
     */
    public final NumericCondition<Integer> daysBetween(N value) {
        return new IntegerCondition(timeBetween(ageAtValueMetadata(this, value), DAYS, value));
    }

    /**
     * Returns a numeric condition that returns the days between this node value and the given field value.
     *
     * @param value the right side value
     * @return the numeric condition
     */
    public final NumericCondition<Integer> daysBetween(TemporalFieldInfo<N> value) {
        return new IntegerCondition(timeBetween(ageAtTemporalFieldMetadata(this, value), DAYS, value));
    }

    /**
     * Returns a numeric condition that returns the days between this node value and the given condition value.
     *
     * @param value the right side value
     * @return the numeric condition
     */
    public final NumericCondition<Integer> daysBetween(TemporalCondition<N> value) {
        return new IntegerCondition(timeBetween(ageAtTemporalConditionMetadata(this, value), DAYS, value));
    }

    /**
     * Returns a numeric condition that returns the days between this node value and the given supplier value.
     *
     * @param value the right side value
     * @return the numeric condition
     */
    public final NumericCondition<Integer> daysBetween(Supplier<N> value) {
        return new IntegerCondition(timeBetween(ageAtSupplierMetadata(this, value), DAYS, value));
    }

    /**
     * Returns a numeric condition that returns the months between this node value and the given value.
     *
     * @param value the right side value
     * @return the numeric condition
     */
    public final NumericCondition<Integer> monthsBetween(N value) {
        return new IntegerCondition(timeBetween(ageAtValueMetadata(this, value), MONTHS, value));
    }

    /**
     * Returns a numeric condition that returns the months between this node value and the given field value.
     *
     * @param value the right side value
     * @return the numeric condition
     */
    public final NumericCondition<Integer> monthsBetween(TemporalFieldInfo<N> value) {
        return new IntegerCondition(timeBetween(ageAtTemporalFieldMetadata(this, value), MONTHS, value));
    }

    /**
     * Returns a numeric condition that returns the months between this node value and the given condition value.
     *
     * @param value the right side value
     * @return the numeric condition
     */
    public final NumericCondition<Integer> monthsBetween(TemporalCondition<N> value) {
        return new IntegerCondition(timeBetween(ageAtTemporalConditionMetadata(this, value), MONTHS, value));
    }

    /**
     * Returns a numeric condition that returns the months between this node value and the given supplier value.
     *
     * @param value the right side value
     * @return the numeric condition
     */
    public final NumericCondition<Integer> monthsBetween(Supplier<N> value) {
        return new IntegerCondition(timeBetween(ageAtSupplierMetadata(this, value), MONTHS, value));
    }

    /**
     * Returns a numeric condition that returns the years between this node value and the given value.
     *
     * @param value the right side value
     * @return the numeric condition
     */
    public final NumericCondition<Integer> yearsBetween(N value) {
        return new IntegerCondition(timeBetween(ageAtValueMetadata(this, value), YEARS, value));
    }

    /**
     * Returns a numeric condition that returns the years between this node value and the given field value.
     *
     * @param value the right side value
     * @return the numeric condition
     */
    public final NumericCondition<Integer> yearsBetween(TemporalFieldInfo<N> value) {
        return new IntegerCondition(timeBetween(ageAtTemporalFieldMetadata(this, value), YEARS, value));
    }

    /**
     * Returns a numeric condition that returns the years between this node value and the given condition value.
     *
     * @param value the right side value
     * @return the numeric condition
     */
    public final NumericCondition<Integer> yearsBetween(TemporalCondition<N> value) {
        return new IntegerCondition(timeBetween(ageAtTemporalConditionMetadata(this, value), YEARS, value));
    }

    /**
     * Returns a numeric condition that returns the years between this node value and the given supplier value.
     *
     * @param value the right side value
     * @return the numeric condition
     */
    public final NumericCondition<Integer> yearsBetween(Supplier<N> value) {
        return new IntegerCondition(timeBetween(ageAtSupplierMetadata(this, value), YEARS, value));
    }

    private NumericCondition<Long> timeBetween(PredicateMetadata metadata, ChronoUnit unit, N value) {
        return new LongCondition(field, metadata,
                        (model, context) -> value(model, field)
                                        .flatMap(l -> Optional.ofNullable(value)
                                                        .map(r -> betweenFunction(unit).apply(l, r))));
    }

    private NumericCondition<Long> timeBetween(PredicateMetadata metadata, ChronoUnit unit,
                    TemporalFieldInfo<N> value) {
        return new LongCondition(field, metadata,
                        (model, context) -> value(model, field)
                                        .flatMap(l -> valueModel(model, value)
                                                        .map(r -> betweenFunction(unit).apply(l, r))));
    }

    private NumericCondition<Long> timeBetween(PredicateMetadata metadata, ChronoUnit unit,
                    TemporalCondition<N> value) {
        return new LongCondition(field, metadata,
                        (model, context) -> value(model, field)
                                        .flatMap(l -> value.function.apply(model, context)
                                                        .map(r -> betweenFunction(unit).apply(l, r))));
    }

    private NumericCondition<Long> timeBetween(PredicateMetadata metadata, ChronoUnit unit, Supplier<N> value) {
        return new LongCondition(field, metadata,
                        (model, context) -> value(model, field)
                                        .flatMap(l -> Optional.ofNullable(value.get())
                                                        .map(r -> betweenFunction(unit).apply(l, r))));
    }

    abstract BiFunction<N, N, Long> betweenFunction(ChronoUnit unit);

}
