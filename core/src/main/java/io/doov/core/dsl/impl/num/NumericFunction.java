/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl.num;

import static io.doov.core.dsl.meta.function.NumericFunctionMetadata.plusMetadata;
import static io.doov.core.dsl.meta.function.NumericFunctionMetadata.timesMetadata;
import static io.doov.core.dsl.meta.predicate.LeafPredicateMetadata.whenMetadata;
import static io.doov.core.dsl.meta.predicate.NaryPredicateMetadata.minMetadata;
import static io.doov.core.dsl.meta.predicate.NaryPredicateMetadata.sumMetadata;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.field.types.NumericFieldInfo;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

public abstract class NumericFunction<N extends Number> extends NumericCondition<N> {

    public NumericFunction(DslField<N> field) {
        super(field);
    }

    public NumericFunction(PredicateMetadata metadata, BiFunction<DslModel, Context, Optional<N>> value) {
        super(metadata, value);
    }

    protected abstract NumericFunction<N> numericFunction(PredicateMetadata metadata,
            BiFunction<DslModel, Context, Optional<N>> value);

    /**
     * Returns a numeric function that returns the node value multiplied by the given multiplier.
     *
     * @param multiplier the multiplier
     * @return the numeric function
     */
    public final NumericFunction<N> times(int multiplier) {
        return numericFunction(timesMetadata(metadata, multiplier),
                (model, context) -> value(model, context).map(v -> timesFunction().apply(v, multiplier)));
    }

    /**
     * Returns a numeric function that returns the node value sum with the node value param.
     *
     * @param field the field to sum
     * @return the numeric function
     */
    public final NumericFunction<N> plus(NumericFieldInfo<N> field) {
        return numericFunction(plusMetadata(metadata, field),
                (model, context) -> value(model, context)
                        .map(v -> sumFunction().apply(v,
                                Optional.ofNullable(model.<N> get(field.id())).orElse(identity()))));
    }

    /**
     * Returns a numeric function that returns the min value of the given field values.
     *
     * @param fields the field values to minimize
     * @return the numeric function
     */
    public final NumericFunction<N> min(List<NumericFieldInfo<N>> fields) {
        return numericFunction(minMetadata(getMetadataForFields(fields)),
                (model, context) -> fields.stream().map(f -> Optional.ofNullable(model.<N> get(f.id())))
                        .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                        .reduce(minFunction()));
    }

    /**
     * Returns a numeric function that returns the sum value of the given field values.
     *
     * @param fields the field values to sum
     * @return the numeric function
     */
    public final NumericFunction<N> sum(List<NumericFieldInfo<N>> fields) {
        return numericFunction(sumMetadata(getMetadataForFields(fields)), (model,
                context) -> Optional.of(fields.stream().map(f -> Optional.ofNullable(model.<N> get(f.id())))
                        .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                        .reduce(identity(), sumFunction())));
    }

    /**
     * Returns a numeric function that returns the sum value of the given condition values.
     *
     * @param conditions the condition values to sum
     * @return the numeric function
     */
    public final NumericFunction<N> sumConditions(List<NumericCondition<N>> conditions) {
        return numericFunction(sumMetadata(getMetadataForConditions(conditions)),
                (model, context) -> Optional.of(conditions.stream().map(c -> c.getFunction().apply(model, context))
                        .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                        .reduce(identity(), sumFunction())));
    }

    private static <N extends Number> List<Metadata> getMetadataForFields(List<NumericFieldInfo<N>> fields) {
        return fields.stream().map(field -> field.getNumericFunction().getMetadata()).collect(toList());
    }

    private static <N extends Number> List<Metadata> getMetadataForConditions(List<NumericCondition<N>> conditions) {
        return conditions.stream().map(condition -> condition.getMetadata()).collect(toList());
    }

    /**
     * Returns a numeric step condition that returns the node value if the condition evaluates to true.
     *
     * @param condition the condition to test
     * @return the numeric condition
     */
    public final NumericFunction<N> when(StepCondition condition) {
        return numericFunction(whenMetadata(metadata, condition),
                (model, context) -> condition.predicate().test(model, context) ? value(model, context)
                        : Optional.empty());
    }
}
