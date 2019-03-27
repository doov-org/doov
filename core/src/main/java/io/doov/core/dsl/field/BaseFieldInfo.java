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

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.impl.DefaultCondition;
import io.doov.core.dsl.impl.base.StringFunction;
import io.doov.core.dsl.impl.num.IntegerFunction;
import io.doov.core.dsl.lang.StepCondition;

/**
 * Base interface for all field info.
 * <p>
 * It contains default methods for common checks, which are calls to methods on {@link DefaultCondition}.
 *
 * @param <T> the type of the field value
 * @see #getDefaultFunction()
 */
public interface BaseFieldInfo<T> extends DslField<T> {

    /**
     * See {@link DefaultCondition#isNull()}
     *
     * @return the step condition
     * @see DefaultCondition#isNull()
     */
    default StepCondition isNull() {
        return getDefaultFunction().isNull();
    }

    /**
     * See {@link DefaultCondition#isNotNull()}
     *
     * @return the step condition
     * @see DefaultCondition#isNotNull()
     */
    default StepCondition isNotNull() {
        return getDefaultFunction().isNotNull();
    }

    /**
     * See {@link DefaultCondition#eq(Object)}
     *
     * @param value the right side value
     * @return the step condition
     * @see DefaultCondition#eq(Object)
     */
    default StepCondition eq(T value) {
        return getDefaultFunction().eq(value);
    }

    /**
     * See {@link DefaultCondition#eq(BaseFieldInfo)}
     *
     * @param value the right side value
     * @return the step condition
     * @see DefaultCondition#eq(BaseFieldInfo)
     */
    default StepCondition eq(BaseFieldInfo<T> value) {
        return getDefaultFunction().eq(value);
    }

    /**
     * See {@link DefaultCondition#eq(Supplier)}
     *
     * @param supplier the right side value supplier
     * @return the step condition
     * @see DefaultCondition#eq(Supplier)
     */
    default StepCondition eq(Supplier<T> supplier) {
        return getDefaultFunction().eq(supplier);
    }

    /**
     * See {@link DefaultCondition#notEq(Object)}
     *
     * @param value the right side value
     * @return the step condition
     * @see DefaultCondition#notEq(Object)
     */
    default StepCondition notEq(T value) {
        return getDefaultFunction().notEq(value);
    }

    /**
     * See {@link DefaultCondition#notEq(BaseFieldInfo)}
     *
     * @param value the right side value
     * @return the step condition
     * @see DefaultCondition#notEq(BaseFieldInfo)
     */
    default StepCondition notEq(BaseFieldInfo<T> value) {
        return getDefaultFunction().notEq(value);
    }

    /**
     * See {@link DefaultCondition#eq(Supplier)}
     *
     * @param supplier the right side value supplier
     * @return the step condition
     * @see DefaultCondition#eq(Supplier)
     */
    default StepCondition notEq(Supplier<T> supplier) {
        return getDefaultFunction().notEq(supplier);
    }

    /**
     * See {@link DefaultCondition#anyMatch(List)}
     *
     * @param value the value to match
     * @return the step condition
     * @see DefaultCondition#anyMatch(List)
     */
    default StepCondition anyMatch(Predicate<T> value) {
        return getDefaultFunction().anyMatch(singletonList(value));
    }

    /**
     * See {@link DefaultCondition#anyMatch(Collection)}
     *
     * @param values the values to match
     * @return the step condition
     * @see DefaultCondition#anyMatch(Collection)
     */
    @SuppressWarnings("unchecked")
    default StepCondition anyMatch(T... values) {
        return getDefaultFunction().anyMatch(asList(values));
    }

    /**
     * See {@link DefaultCondition#anyMatch(Collection)}
     *
     * @param values the values to match
     * @return the step condition
     * @see DefaultCondition#anyMatch(Collection)
     */
    default StepCondition anyMatch(Collection<T> values) {
        return getDefaultFunction().anyMatch(values);
    }

    /**
     * See {@link DefaultCondition#allMatch(List)}
     *
     * @param value the value to match
     * @return the step condition
     * @see DefaultCondition#allMatch(List)
     */
    default StepCondition allMatch(Predicate<T> value) {
        return getDefaultFunction().allMatch(singletonList(value));
    }

    /**
     * See {@link DefaultCondition#allMatch(Collection)}
     *
     * @param values the values to match
     * @return the step condition
     * @see DefaultCondition#allMatch(Collection)
     */
    @SuppressWarnings("unchecked")
    default StepCondition allMatch(T... values) {
        return getDefaultFunction().allMatch(asList(values));
    }

    /**
     * See {@link DefaultCondition#allMatch(Collection)}
     *
     * @param values the values to match
     * @return the step condition
     * @see DefaultCondition#allMatch(Collection)
     */
    default StepCondition allMatch(Collection<T> values) {
        return getDefaultFunction().allMatch(values);
    }

    /**
     * See {@link DefaultCondition#noneMatch(List)}
     *
     * @param value the value to match
     * @return the step condition
     * @see DefaultCondition#noneMatch(List)
     */
    default StepCondition noneMatch(Predicate<T> value) {
        return getDefaultFunction().noneMatch(singletonList(value));
    }

    /**
     * See {@link DefaultCondition#noneMatch(Collection)}
     *
     * @param values the values to match
     * @return the step condition
     * @see DefaultCondition#noneMatch(Collection)
     */
    @SuppressWarnings("unchecked")
    default StepCondition noneMatch(T... values) {
        return getDefaultFunction().noneMatch(asList(values));
    }

    /**
     * See {@link DefaultCondition#noneMatch(Collection)}
     *
     * @param values the values to match
     * @return the step condition
     * @see DefaultCondition#noneMatch(Collection)
     */
    default StepCondition noneMatch(Collection<T> values) {
        return getDefaultFunction().noneMatch(values);
    }

    /**
     * See {@link DefaultCondition#mapToInt(Function)}
     *
     * @param mapper the mapper to apply
     * @return the step condition
     * @see DefaultCondition#mapToInt(Function)
     */
    default IntegerFunction mapToInt(Function<T, Integer> mapper) {
        return getDefaultFunction().mapToInt(mapper);
    }

    /**
     * See {@link DefaultCondition#mapToString(Function)}
     *
     * @param mapper mapper function to apply
     * @return string condition
     */
    default StringFunction mapToString(Function<T, String> mapper) {
        return getDefaultFunction().mapToString(mapper);
    }

    /**
     * See {@link DefaultCondition#map(String, Function)}
     *
     * @param readable text describing the function
     * @param mapper mapper function to apply
     * @param <R> target type
     * @return condition with target type
     */
    default <R> DefaultCondition<R> map(String readable, Function<T, R> mapper) {
        return getDefaultFunction().map(readable, mapper);
    }

    /**
     * See {@link DefaultCondition#mapUsing(String, io.doov.core.dsl.field.types.ContextAccessor, BiFunction)}
     *
     * @param readable description
     * @param condition condition
     * @param mapper mapper function to apply
     * @param <U> condition type
     * @param <R> target type
     * @return condition with target type
     */
    default <U, R> DefaultCondition<R> mapUsing(String readable, DslField<U> condition,
            BiFunction<T, U, R> mapper) {
        return getDefaultFunction().mapUsing(readable, condition.getDefaultFunction(), mapper);
    }

}
