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
package io.doov.core.dsl.impl.time;

import static io.doov.core.dsl.meta.function.TemporalBiFunctionMetadata.withMetadata;
import static io.doov.core.dsl.meta.predicate.BinaryPredicateMetadata.equalsMetadata;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.YEARS;

import java.time.temporal.*;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.field.types.NumericFieldInfo;
import io.doov.core.dsl.field.types.TemporalFieldInfo;
import io.doov.core.dsl.impl.*;
import io.doov.core.dsl.impl.num.*;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.function.TemporalBiFunctionMetadata;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;
import io.doov.core.dsl.time.TemporalAdjuster;

/**
 * Base class for temporal function.
 * <p>
 * It contains a {@link DslField} to get the value from the model, a {@link PredicateMetadata} to describe this node,
 * and a {@link BiFunction} to take the value from the model and return an optional value.
 *
 * @param <N> the type of the field value
 */
public abstract class TemporalFunction<N extends Temporal> extends DefaultFunction<N, PredicateMetadata>
        implements TemporalOperators<N> {

    protected TemporalFunction(PredicateMetadata metadata, BiFunction<FieldModel, Context, Optional<N>> value) {
        super(metadata, value);
    }

    protected abstract TemporalFunction<N> temporalFunction(PredicateMetadata metadata,
            BiFunction<FieldModel, Context, Optional<N>> value);


    /**
     * Returns a condition checking if the node value is equal to the given condition value.
     *
     * @param value the right side value
     * @return the step condition
     */
    public final StepCondition eq(TemporalFunction<N> value) {
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
    public final StepCondition before(TemporalFunction<N> value) {
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
    public final StepCondition beforeOrEq(TemporalFunction<N> value) {
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
    public final StepCondition after(TemporalFunction<N> value) {
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
    public final StepCondition afterOrEq(TemporalFunction<N> value) {
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
    public final StepCondition between(TemporalFunction<N> minIncluded, TemporalFunction<N> maxExcluded) {
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
    public final StepCondition notBetween(TemporalFunction<N> minIncluded, TemporalFunction<N> maxExcluded) {
        return LogicalUnaryCondition.negate(between(minIncluded, maxExcluded));
    }

    /**
     * Returns a temporal function that returns the node value with given temporal adjuster applied.
     *
     * @param adjuster the adjuster
     * @return the temporal function
     */
    public final TemporalFunction<N> with(TemporalAdjuster adjuster) {
        return temporalFunction(withMetadata(metadata, adjuster.getMetadata()), (model,
                context) -> value(model, context).map(v -> withFunction(adjuster.getAdjuster()).apply(v)));
    }

    /**
     * Returns a temporal function that returns the node value minus given temporal value and unit.
     *
     * @param value the minus value
     * @param unit the minus unit
     * @return the temporal function
     */
    public final TemporalFunction<N> minus(int value, TemporalUnit unit) {
        return temporalFunction(TemporalBiFunctionMetadata.minusMetadata(metadata, value, unit),
                (model, context) -> value(model, context).map(v -> minusFunction(value, unit).apply(v)));
    }

    /**
     * Returns a temporal function that returns the node value minus given temporal field value and unit.
     *
     * @param value the minus field value
     * @param unit the minus unit
     * @return the temporal function
     */
    public final TemporalFunction<N> minus(NumericFieldInfo<Integer> value, TemporalUnit unit) {
        return temporalFunction(TemporalBiFunctionMetadata.minusMetadata(metadata, value, unit),
                (model, context) -> value(model, context)
                        .flatMap(l -> Optional.ofNullable(model.<Integer> get(value.id()))
                                .map(r -> minusFunction(r, unit).apply(l))));
    }

    /**
     * Returns a temporal function that returns the node value minus given temporal field value and unit.
     *
     * @param function the minus field value
     * @param unit the minus unit
     * @return the temporal function
     */
    public final TemporalFunction<N> minus(NumericFunction<Integer> function, TemporalUnit unit) {
        return temporalFunction(TemporalBiFunctionMetadata.minusMetadata(metadata, function, unit),
                (model, context) -> value(model, context)
                        .flatMap(l -> function.value(model, context)
                                .map(r -> minusFunction(r, unit).apply(l))));
    }

    /**
     * Returns a temporal function that returns the node value plus given temporal value and unit.
     *
     * @param value the plus value
     * @param unit the plus unit
     * @return the temporal function
     */
    public final TemporalFunction<N> plus(int value, TemporalUnit unit) {
        return temporalFunction(TemporalBiFunctionMetadata.plusMetadata(metadata, value, unit),
                (model, context) -> value(model, context).map(v -> plusFunction(value, unit).apply(v)));
    }

    /**
     * Returns a temporal function that returns the node value plus given temporal field value and unit.
     *
     * @param value the plus field value
     * @param unit the plus unit
     * @return the temporal function
     */
    public final TemporalFunction<N> plus(NumericFieldInfo<Integer> value, TemporalUnit unit) {
        return temporalFunction(TemporalBiFunctionMetadata.plusMetadata(metadata, value, unit),
                (model, context) -> value(model, context)
                        .flatMap(l -> Optional.ofNullable(model.<Integer> get(value.id()))
                                .map(r -> plusFunction(r, unit).apply(l))));
    }

    /**
     * Returns a temporal function that returns the node value plus given temporal field value and unit.
     *
     * @param function the plus field value
     * @param unit the plus unit
     * @return the temporal function
     */
    public final TemporalFunction<N> plus(NumericFunction<Integer> function, TemporalUnit unit) {
        return temporalFunction(TemporalBiFunctionMetadata.plusMetadata(metadata, function, unit),
                (model, context) -> value(model, context)
                        .flatMap(l -> function.value(model, context)
                                .map(r -> plusFunction(r, unit).apply(l))));
    }

    /**
     * Returns a numeric function that returns the age at the given value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> ageAt(N value) {
        return IntegerFunction.fromLong(timeBetween(TemporalBiFunctionMetadata.ageAtValueMetadata(this, value), YEARS, value));
    }

    /**
     * Returns a numeric function that returns the age of this node value at the given field value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> ageAt(TemporalFieldInfo<N> value) {
        return IntegerFunction.fromLong(timeBetween(TemporalBiFunctionMetadata.ageAtTemporalFieldMetadata(this, value), YEARS, value));
    }

    /**
     * Returns a numeric function that returns the age of this node value at the given condition value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> ageAt(TemporalFunction<N> value) {
        return IntegerFunction.fromLong(timeBetween(TemporalBiFunctionMetadata.ageAtTemporalConditionMetadata(this, value), YEARS, value));
    }

    /**
     * Returns a numeric function that returns the age of this node value at the given supplier value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> ageAt(Supplier<N> value) {
        return IntegerFunction.fromLong(timeBetween(TemporalBiFunctionMetadata.ageAtSupplierMetadata(this, value), YEARS, value));
    }

    /**
     * Returns a numeric function that returns the days between this node value and the given value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> daysBetween(N value) {
        return IntegerFunction.fromLong(timeBetween(TemporalBiFunctionMetadata.ageAtValueMetadata(this, value), DAYS, value));
    }

    /**
     * Returns a numeric function that returns the days between this node value and the given field value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> daysBetween(TemporalFieldInfo<N> value) {
        return IntegerFunction.fromLong(timeBetween(TemporalBiFunctionMetadata.ageAtTemporalFieldMetadata(this, value), DAYS, value));
    }

    /**
     * Returns a numeric function that returns the days between this node value and the given condition value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> daysBetween(TemporalFunction<N> value) {
        return IntegerFunction.fromLong(timeBetween(TemporalBiFunctionMetadata.ageAtTemporalConditionMetadata(this, value), DAYS, value));
    }

    /**
     * Returns a numeric function that returns the days between this node value and the given supplier value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> daysBetween(Supplier<N> value) {
        return IntegerFunction.fromLong(timeBetween(TemporalBiFunctionMetadata.ageAtSupplierMetadata(this, value), DAYS, value));
    }

    /**
     * Returns a numeric function that returns the months between this node value and the given value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> monthsBetween(N value) {
        return IntegerFunction.fromLong(timeBetween(TemporalBiFunctionMetadata.ageAtValueMetadata(this, value), MONTHS, value));
    }

    /**
     * Returns a numeric function that returns the months between this node value and the given field value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> monthsBetween(TemporalFieldInfo<N> value) {
        return IntegerFunction.fromLong(timeBetween(TemporalBiFunctionMetadata.ageAtTemporalFieldMetadata(this, value), MONTHS, value));
    }

    /**
     * Returns a numeric function that returns the months between this node value and the given condition value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> monthsBetween(TemporalFunction<N> value) {
        return IntegerFunction.fromLong(timeBetween(TemporalBiFunctionMetadata.ageAtTemporalConditionMetadata(this, value), MONTHS, value));
    }

    /**
     * Returns a numeric function that returns the months between this node value and the given supplier value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> monthsBetween(Supplier<N> value) {
        return IntegerFunction.fromLong(timeBetween(TemporalBiFunctionMetadata.ageAtSupplierMetadata(this, value), MONTHS, value));
    }

    /**
     * Returns a numeric function that returns the years between this node value and the given value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> yearsBetween(N value) {
        return IntegerFunction.fromLong(timeBetween(TemporalBiFunctionMetadata.ageAtValueMetadata(this, value), YEARS, value));
    }

    /**
     * Returns a numeric function that returns the years between this node value and the given field value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> yearsBetween(TemporalFieldInfo<N> value) {
        return IntegerFunction.fromLong(timeBetween(TemporalBiFunctionMetadata.ageAtTemporalFieldMetadata(this, value), YEARS, value));
    }

    /**
     * Returns a numeric function that returns the years between this node value and the given condition value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> yearsBetween(TemporalFunction<N> value) {
        return IntegerFunction.fromLong(timeBetween(TemporalBiFunctionMetadata.ageAtTemporalConditionMetadata(this, value), YEARS, value));
    }

    /**
     * Returns a numeric condition that returns the years between this node value and the given supplier value.
     *
     * @param value the right side value
     * @return the numeric condition
     */
    public final NumericFunction<Integer> yearsBetween(Supplier<N> value) {
        return IntegerFunction.fromLong(timeBetween(TemporalBiFunctionMetadata.ageAtSupplierMetadata(this, value), YEARS, value));
    }

    private NumericFunction<Long> timeBetween(PredicateMetadata metadata, ChronoUnit unit, N value) {
        return new LongFunction(metadata, (model, context) -> value(model, context)
                .flatMap(l -> Optional.ofNullable(value).map(r -> betweenFunction(unit).apply(l, r))));
    }

    private NumericFunction<Long> timeBetween(PredicateMetadata metadata, ChronoUnit unit,
            TemporalFieldInfo<N> value) {
        return new LongFunction(metadata, (model, context) -> value(model, context)
                .flatMap(l -> FieldModel.valueModel(model, value).map(r -> betweenFunction(unit).apply(l, r))));
    }

    private NumericFunction<Long> timeBetween(PredicateMetadata metadata, ChronoUnit unit,
            TemporalFunction<N> value) {
        return new LongFunction(metadata, (model, context) -> value(model, context).flatMap(
                l -> value.getFunction().apply(model, context).map(r -> betweenFunction(unit).apply(l, r))));
    }

    private NumericFunction<Long> timeBetween(PredicateMetadata metadata, ChronoUnit unit, Supplier<N> value) {
        return new LongFunction(metadata, (model, context) -> value(model, context)
                .flatMap(l -> Optional.ofNullable(value.get()).map(r -> betweenFunction(unit).apply(l, r))));
    }

}
