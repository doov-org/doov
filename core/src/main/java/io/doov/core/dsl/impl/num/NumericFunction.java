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

import static io.doov.core.dsl.meta.function.NumericFunctionMetadata.plusMetadata;
import static io.doov.core.dsl.meta.function.NumericFunctionMetadata.timesMetadata;
import static io.doov.core.dsl.meta.function.NumericFunctionMetadata.whenMetadata;
import static io.doov.core.dsl.meta.predicate.NaryPredicateMetadata.minMetadata;
import static io.doov.core.dsl.meta.predicate.NaryPredicateMetadata.sumMetadata;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.BiFunction;

import io.doov.core.FieldModel;
import io.doov.core.Try;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.field.types.NumericFieldInfo;
import io.doov.core.dsl.impl.DefaultFunction;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

public abstract class NumericFunction<N extends Number> extends NumericCondition<N> {

    public NumericFunction(DslField<N> field) {
        super(field);
    }

    public NumericFunction(PredicateMetadata metadata, BiFunction<FieldModel, Context, Try<N>> value) {
        super(metadata, value);
    }

    protected abstract NumericFunction<N> numericFunction(PredicateMetadata metadata,
            BiFunction<FieldModel, Context, Try<N>> value);

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
                                Try.success(model.<N> get(field.id())).recover(identity()).value())));
    }

    /**
     * Returns a numeric function that returns the min value of the given field values.
     *
     * @param fields the field values to minimize
     * @return the numeric function
     */
    public final NumericFunction<N> min(List<NumericFieldInfo<N>> fields) {
        return numericFunction(minMetadata(getMetadataForFields(fields)),
                (model, context) -> fields.stream()
                        .map(f -> Try.success(model.<N> get(f.id())))
                        .filter(Try::isNotNull)
                        .reduce(
                                Try.success(identity()),
                                (lhs,rhs) -> Try.combine(minFunction(), lhs,rhs)));
    }

    /**
     * Returns a numeric function that returns the sum value of the given field values.
     *
     * @param fields the field values to sum
     * @return the numeric function
     */
    public final NumericFunction<N> sum(List<NumericFieldInfo<N>> fields) {
        return numericFunction(sumMetadata(getMetadataForFields(fields)),
                (model, context) -> fields.stream()
                        .map(f -> Try.success(model.<N> get(f.id())))
                        .filter(Try::isNotNull)
                        .reduce(
                                Try.success(identity()),
                                (lhs,rhs) -> Try.combine(sumFunction(), lhs,rhs)));
    }

    /**
     * Returns a numeric function that returns the sum value of the given condition values.
     *
     * @param conditions the condition values to sum
     * @return the numeric function
     */
    public final NumericFunction<N> sumConditions(List<NumericCondition<N>> conditions) {
        return numericFunction(sumMetadata(getMetadataForConditions(conditions)),
                (model, context) -> conditions.stream()
                        .map(c -> c.getFunction().apply(model, context))
                        .filter(Try::isNotNull)
                        .reduce(
                                Try.success(identity()),
                                (lhs,rhs) -> Try.combine(sumFunction(), lhs,rhs)));
    }

    private static <N extends Number> List<Metadata> getMetadataForFields(List<NumericFieldInfo<N>> fields) {
        return fields.stream().map(field -> field.getNumericFunction().metadata()).collect(toList());
    }

    private static <N extends Number> List<Metadata> getMetadataForConditions(List<NumericCondition<N>> conditions) {
        return conditions.stream().map(DefaultFunction::metadata).collect(toList());
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
                        : Try.success(null));
    }
}
