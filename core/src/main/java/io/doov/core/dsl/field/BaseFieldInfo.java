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
package io.doov.core.dsl.field;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import java.util.Collection;
import java.util.List;
import java.util.function.*;

import io.doov.core.TagId;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.impl.DefaultFunction;
import io.doov.core.dsl.impl.base.IterableFunction;
import io.doov.core.dsl.impl.base.StringFunction;
import io.doov.core.dsl.impl.num.IntegerFunction;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.function.MapFunctionMetadata;

/**
 * Base interface for all field info.
 * <p>
 * It contains default methods for common checks, which are calls to methods on {@link DefaultFunction}.
 *
 * @param <T> the type of the field value
 * @see #getDefaultFunction()
 */
public interface BaseFieldInfo<T> extends DslField<T> {

    /**
     * See {@link DefaultFunction#isNull()}
     *
     * @return the step condition
     * @see DefaultFunction#isNull()
     */
    default StepCondition isNull() {
        return getDefaultFunction().isNull();
    }

    /**
     * See {@link DefaultFunction#isNotNull()}
     *
     * @return the step condition
     * @see DefaultFunction#isNotNull()
     */
    default StepCondition isNotNull() {
        return getDefaultFunction().isNotNull();
    }

    /**
     * See {@link DefaultFunction#eq(Object)}
     *
     * @param value the right side value
     * @return the step condition
     * @see DefaultFunction#eq(Object)
     */
    default StepCondition eq(T value) {
        return getDefaultFunction().eq(value);
    }

    /**
     * See {@link DefaultFunction#eq(BaseFieldInfo)}
     *
     * @param value the right side value
     * @return the step condition
     * @see DefaultFunction#eq(BaseFieldInfo)
     */
    default StepCondition eq(BaseFieldInfo<T> value) {
        return getDefaultFunction().eq(value);
    }

    /**
     * See {@link DefaultFunction#eq(Supplier)}
     *
     * @param supplier the right side value supplier
     * @return the step condition
     * @see DefaultFunction#eq(Supplier)
     */
    default StepCondition eq(Supplier<T> supplier) {
        return getDefaultFunction().eq(supplier);
    }

    /**
     * See {@link DefaultFunction#eq(io.doov.core.dsl.field.types.Function)}
     *
     * @param function the right side value function
     * @return the step condition
     * @see DefaultFunction#eq(io.doov.core.dsl.field.types.Function)
     */
    default StepCondition eq(io.doov.core.dsl.field.types.Function<T> function) {
        return getDefaultFunction().eq(function);
    }

    /**
     * See {@link DefaultFunction#notEq(Object)}
     *
     * @param value the right side value
     * @return the step condition
     * @see DefaultFunction#notEq(Object)
     */
    default StepCondition notEq(T value) {
        return getDefaultFunction().notEq(value);
    }

    /**
     * See {@link DefaultFunction#notEq(BaseFieldInfo)}
     *
     * @param value the right side value
     * @return the step condition
     * @see DefaultFunction#notEq(BaseFieldInfo)
     */
    default StepCondition notEq(BaseFieldInfo<T> value) {
        return getDefaultFunction().notEq(value);
    }

    /**
     * See {@link DefaultFunction#eq(Supplier)}
     *
     * @param supplier the right side value supplier
     * @return the step condition
     * @see DefaultFunction#eq(Supplier)
     */
    default StepCondition notEq(Supplier<T> supplier) {
        return getDefaultFunction().notEq(supplier);
    }

    /**
     * See {@link DefaultFunction#eq(io.doov.core.dsl.field.types.Function)}
     *
     * @param function the right side value function
     * @return the step condition
     * @see DefaultFunction#eq(io.doov.core.dsl.field.types.Function)
     */
    default StepCondition notEq(io.doov.core.dsl.field.types.Function<T> function) {
        return getDefaultFunction().notEq(function);
    }

    /**
     * See {@link DefaultFunction#anyMatch(List)}
     *
     * @param value the value to match
     * @return the step condition
     * @see DefaultFunction#anyMatch(List)
     */
    default StepCondition anyMatch(Predicate<T> value) {
        return getDefaultFunction().anyMatch(singletonList(value));
    }

    /**
     * See {@link DefaultFunction#anyMatch(Collection)}
     *
     * @param values the values to match
     * @return the step condition
     * @see DefaultFunction#anyMatch(Collection)
     */
    @SuppressWarnings("unchecked")
    default StepCondition anyMatch(T... values) {
        return getDefaultFunction().anyMatch(asList(values));
    }

    /**
     * See {@link DefaultFunction#anyMatch(Collection)}
     *
     * @param values the values to match
     * @return the step condition
     * @see DefaultFunction#anyMatch(Collection)
     */
    default StepCondition anyMatch(Collection<T> values) {
        return getDefaultFunction().anyMatch(values);
    }

    /**
     * See {@link DefaultFunction#allMatch(List)}
     *
     * @param value the value to match
     * @return the step condition
     * @see DefaultFunction#allMatch(List)
     */
    default StepCondition allMatch(Predicate<T> value) {
        return getDefaultFunction().allMatch(singletonList(value));
    }

    /**
     * See {@link DefaultFunction#allMatch(Collection)}
     *
     * @param values the values to match
     * @return the step condition
     * @see DefaultFunction#allMatch(Collection)
     */
    @SuppressWarnings("unchecked")
    default StepCondition allMatch(T... values) {
        return getDefaultFunction().allMatch(asList(values));
    }

    /**
     * See {@link DefaultFunction#allMatch(Collection)}
     *
     * @param values the values to match
     * @return the step condition
     * @see DefaultFunction#allMatch(Collection)
     */
    default StepCondition allMatch(Collection<T> values) {
        return getDefaultFunction().allMatch(values);
    }

    /**
     * See {@link DefaultFunction#noneMatch(List)}
     *
     * @param value the value to match
     * @return the step condition
     * @see DefaultFunction#noneMatch(List)
     */
    default StepCondition noneMatch(Predicate<T> value) {
        return getDefaultFunction().noneMatch(singletonList(value));
    }

    /**
     * See {@link DefaultFunction#noneMatch(Collection)}
     *
     * @param values the values to match
     * @return the step condition
     * @see DefaultFunction#noneMatch(Collection)
     */
    @SuppressWarnings("unchecked")
    default StepCondition noneMatch(T... values) {
        return getDefaultFunction().noneMatch(asList(values));
    }

    /**
     * See {@link DefaultFunction#noneMatch(Collection)}
     *
     * @param values the values to match
     * @return the step condition
     * @see DefaultFunction#noneMatch(Collection)
     */
    default StepCondition noneMatch(Collection<T> values) {
        return getDefaultFunction().noneMatch(values);
    }

    /**
     * See {@link DefaultFunction#mapToInt(Function)}
     *
     * @param mapper the mapper to apply
     * @return the step condition
     * @see DefaultFunction#mapToInt(Function)
     */
    default IntegerFunction mapToInt(Function<T, Integer> mapper) {
        return getDefaultFunction().mapToInt(mapper);
    }

    /**
     * See {@link DefaultFunction#mapToString(Function)}
     *
     * @param mapper mapper function to apply
     * @return string condition
     */
    default StringFunction mapToString(Function<T, String> mapper) {
        return getDefaultFunction().mapToString(mapper);
    }

    /**
     * See {@link DefaultFunction#map(String, Function)}
     *
     * @param readable text describing the function
     * @param mapper mapper function to apply
     * @param <R> target type
     * @return condition with target type
     */
    default <R> DefaultFunction<R, MapFunctionMetadata> map(String readable, Function<T, R> mapper) {
        return getDefaultFunction().map(readable, mapper);
    }

    /**
     * See {@link DefaultFunction#mapUsing(String, io.doov.core.dsl.field.types.Function, BiFunction)}
     *
     * @param readable description
     * @param condition condition
     * @param mapper mapper function to apply
     * @param <U> condition type
     * @param <R> target type
     * @return condition with target type
     */
    default <U, R> DefaultFunction<R, MapFunctionMetadata> mapUsing(String readable, DslField<U> condition,
            BiFunction<T, U, R> mapper) {
        return getDefaultFunction().mapUsing(readable, condition.getDefaultFunction(), mapper);
    }

    /**
     * See {@link DefaultFunction#tags()}
     *
     * @param tag tag to look for
     * @return the step function
     */
    default StepCondition hasTag(TagId tag) {
        return getDefaultFunction().tags().contains(tag);
    }

    /**
     * See {@link DefaultFunction#tags()}
     *
     * @return the iterable function
     */
    default IterableFunction<TagId, List<TagId>> tags() {
        return getDefaultFunction().tags();
    }

    /**
     * See {@link DefaultFunction#position()}
     *
     * @return the integer function
     */
    default IntegerFunction position() {
        return getDefaultFunction().position();
    }

}
