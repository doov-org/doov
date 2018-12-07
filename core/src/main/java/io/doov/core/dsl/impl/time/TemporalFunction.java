/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl.time;

import static io.doov.core.dsl.meta.function.TemporalAdjusterMetadata.withMetadata;
import static io.doov.core.dsl.meta.function.TemporalFunctionMetadata.*;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.YEARS;

import java.time.temporal.*;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.field.types.NumericFieldInfo;
import io.doov.core.dsl.field.types.TemporalFieldInfo;
import io.doov.core.dsl.impl.num.*;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;
import io.doov.core.dsl.time.TemporalAdjuster;

public abstract class TemporalFunction<N extends Temporal> extends TemporalCondition<N> {

    protected TemporalFunction(DslField<N> field) {
        super(field);
    }

    protected TemporalFunction(PredicateMetadata metadata, BiFunction<DslModel, Context, Optional<N>> value) {
        super(metadata, value);
    }

    @Override
    protected final TemporalFunction<N> temporalCondition(PredicateMetadata metadata,
            BiFunction<DslModel, Context, Optional<N>> value) {
        return temporalFunction(metadata, value);
    }

    protected abstract TemporalFunction<N> temporalFunction(PredicateMetadata metadata,
            BiFunction<DslModel, Context, Optional<N>> value);

    /**
     * Returns a temporal function that returns the node value with given temporal adjuster applied.
     *
     * @param adjuster the adjuster
     * @return the temporal function
     */
    public final TemporalFunction<N> with(TemporalAdjuster adjuster) {
        return temporalCondition(withMetadata(metadata, adjuster.getMetadata()), (model,
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
        return temporalCondition(minusMetadata(metadata, value, unit),
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
        return temporalCondition(minusMetadata(metadata, value, unit),
                (model, context) -> value(model, context)
                        .flatMap(l -> Optional.ofNullable(model.<Integer> get(value.id()))
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
        return temporalCondition(plusMetadata(metadata, value, unit),
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
        return temporalCondition(plusMetadata(metadata, value, unit),
                (model, context) -> value(model, context)
                        .flatMap(l -> Optional.ofNullable(model.<Integer> get(value.id()))
                                .map(r -> plusFunction(r, unit).apply(l))));
    }

    /**
     * Returns a numeric function that returns the age at the given value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> ageAt(N value) {
        return new IntegerFunction(timeBetween(ageAtValueMetadata(this, value), YEARS, value));
    }

    /**
     * Returns a numeric function that returns the age of this node value at the given field value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> ageAt(TemporalFieldInfo<N> value) {
        return new IntegerFunction(timeBetween(ageAtTemporalFieldMetadata(this, value), YEARS, value));
    }

    /**
     * Returns a numeric function that returns the age of this node value at the given condition value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> ageAt(TemporalFunction<N> value) {
        return new IntegerFunction(timeBetween(ageAtTemporalConditionMetadata(this, value), YEARS, value));
    }

    /**
     * Returns a numeric function that returns the age of this node value at the given supplier value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> ageAt(Supplier<N> value) {
        return new IntegerFunction(timeBetween(ageAtSupplierMetadata(this, value), YEARS, value));
    }

    /**
     * Returns a numeric function that returns the days between this node value and the given value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> daysBetween(N value) {
        return new IntegerFunction(timeBetween(ageAtValueMetadata(this, value), DAYS, value));
    }

    /**
     * Returns a numeric function that returns the days between this node value and the given field value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> daysBetween(TemporalFieldInfo<N> value) {
        return new IntegerFunction(timeBetween(ageAtTemporalFieldMetadata(this, value), DAYS, value));
    }

    /**
     * Returns a numeric function that returns the days between this node value and the given condition value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> daysBetween(TemporalFunction<N> value) {
        return new IntegerFunction(timeBetween(ageAtTemporalConditionMetadata(this, value), DAYS, value));
    }

    /**
     * Returns a numeric function that returns the days between this node value and the given supplier value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> daysBetween(Supplier<N> value) {
        return new IntegerFunction(timeBetween(ageAtSupplierMetadata(this, value), DAYS, value));
    }

    /**
     * Returns a numeric function that returns the months between this node value and the given value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> monthsBetween(N value) {
        return new IntegerFunction(timeBetween(ageAtValueMetadata(this, value), MONTHS, value));
    }

    /**
     * Returns a numeric function that returns the months between this node value and the given field value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> monthsBetween(TemporalFieldInfo<N> value) {
        return new IntegerFunction(timeBetween(ageAtTemporalFieldMetadata(this, value), MONTHS, value));
    }

    /**
     * Returns a numeric function that returns the months between this node value and the given condition value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> monthsBetween(TemporalFunction<N> value) {
        return new IntegerFunction(timeBetween(ageAtTemporalConditionMetadata(this, value), MONTHS, value));
    }

    /**
     * Returns a numeric function that returns the months between this node value and the given supplier value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> monthsBetween(Supplier<N> value) {
        return new IntegerFunction(timeBetween(ageAtSupplierMetadata(this, value), MONTHS, value));
    }

    /**
     * Returns a numeric function that returns the years between this node value and the given value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> yearsBetween(N value) {
        return new IntegerFunction(timeBetween(ageAtValueMetadata(this, value), YEARS, value));
    }

    /**
     * Returns a numeric function that returns the years between this node value and the given field value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> yearsBetween(TemporalFieldInfo<N> value) {
        return new IntegerFunction(timeBetween(ageAtTemporalFieldMetadata(this, value), YEARS, value));
    }

    /**
     * Returns a numeric function that returns the years between this node value and the given condition value.
     *
     * @param value the right side value
     * @return the numeric function
     */
    public final NumericFunction<Integer> yearsBetween(TemporalFunction<N> value) {
        return new IntegerFunction(timeBetween(ageAtTemporalConditionMetadata(this, value), YEARS, value));
    }

    /**
     * Returns a numeric condition that returns the years between this node value and the given supplier value.
     *
     * @param value the right side value
     * @return the numeric condition
     */
    public final NumericFunction<Integer> yearsBetween(Supplier<N> value) {
        return new IntegerFunction(timeBetween(ageAtSupplierMetadata(this, value), YEARS, value));
    }

    private NumericFunction<Long> timeBetween(PredicateMetadata metadata, ChronoUnit unit, N value) {
        return new LongFunction(metadata, (model, context) -> value(model, context)
                .flatMap(l -> Optional.ofNullable(value).map(r -> betweenFunction(unit).apply(l, r))));
    }

    private NumericFunction<Long> timeBetween(PredicateMetadata metadata, ChronoUnit unit,
            TemporalFieldInfo<N> value) {
        return new LongFunction(metadata, (model, context) -> value(model, context)
                .flatMap(l -> valueModel(model, value).map(r -> betweenFunction(unit).apply(l, r))));
    }

    private NumericFunction<Long> timeBetween(PredicateMetadata metadata, ChronoUnit unit,
            TemporalCondition<N> value) {
        return new LongFunction(metadata, (model, context) -> value(model, context).flatMap(
                l -> value.getFunction().apply(model, context).map(r -> betweenFunction(unit).apply(l, r))));
    }

    private NumericFunction<Long> timeBetween(PredicateMetadata metadata, ChronoUnit unit, Supplier<N> value) {
        return new LongFunction(metadata, (model, context) -> value(model, context)
                .flatMap(l -> Optional.ofNullable(value.get()).map(r -> betweenFunction(unit).apply(l, r))));
    }

}
