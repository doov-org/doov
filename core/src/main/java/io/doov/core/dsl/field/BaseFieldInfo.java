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
import io.doov.core.dsl.impl.IntegerCondition;
import io.doov.core.dsl.lang.StepCondition;

/**
 * Base interface for all field info.
 * <p>
 * It contains default methods for common checks, which are calls to methods on {@link DefaultCondition}.
 *
 * @param <T> the type of the field value
 * @see #getDefaultCondition()
 */
public interface BaseFieldInfo<T> extends DslField<T> {

    /**
     * See {@link DefaultCondition#isNull()}
     *
     * @return the step condition
     * @see DefaultCondition#isNull()
     */
    default StepCondition isNull() {
        return getDefaultCondition().isNull();
    }

    /**
     * See {@link DefaultCondition#isNotNull()}
     *
     * @return the step condition
     * @see DefaultCondition#isNotNull()
     */
    default StepCondition isNotNull() {
        return getDefaultCondition().isNotNull();
    }

    /**
     * See {@link DefaultCondition#eq(Object)}
     *
     * @param value the right side value
     * @return the step condition
     * @see DefaultCondition#eq(Object)
     */
    default StepCondition eq(T value) {
        return getDefaultCondition().eq(value);
    }

    /**
     * See {@link DefaultCondition#eq(BaseFieldInfo)}
     *
     * @param value the right side value
     * @return the step condition
     * @see DefaultCondition#eq(BaseFieldInfo)
     */
    default StepCondition eq(BaseFieldInfo<T> value) {
        return getDefaultCondition().eq(value);
    }

    /**
     * See {@link DefaultCondition#eq(Supplier)}
     *
     * @param value the right side value
     * @return the step condition
     * @see DefaultCondition#eq(Supplier)
     */
    default StepCondition eq(Supplier<T> value) {
        return getDefaultCondition().eq(value);
    }

    /**
     * See {@link DefaultCondition#notEq(Object)}
     *
     * @param value the right side value
     * @return the step condition
     * @see DefaultCondition#notEq(Object)
     */
    default StepCondition notEq(T value) {
        return getDefaultCondition().notEq(value);
    }

    /**
     * See {@link DefaultCondition#notEq(BaseFieldInfo)}
     *
     * @param value the right side value
     * @return the step condition
     * @see DefaultCondition#notEq(BaseFieldInfo)
     */
    default StepCondition notEq(BaseFieldInfo<T> value) {
        return getDefaultCondition().notEq(value);
    }

    /**
     * See {@link DefaultCondition#anyMatch(List)}
     *
     * @param value the value to match
     * @return the step condition
     * @see DefaultCondition#anyMatch(List)
     */
    default StepCondition anyMatch(Predicate<T> value) {
        return getDefaultCondition().anyMatch(singletonList(value));
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
        return getDefaultCondition().anyMatch(asList(values));
    }

    /**
     * See {@link DefaultCondition#anyMatch(Collection)}
     *
     * @param values the values to match
     * @return the step condition
     * @see DefaultCondition#anyMatch(Collection)
     */
    default StepCondition anyMatch(Collection<T> values) {
        return getDefaultCondition().anyMatch(values);
    }

    /**
     * See {@link DefaultCondition#allMatch(List)}
     *
     * @param value the value to match
     * @return the step condition
     * @see DefaultCondition#allMatch(List)
     */
    default StepCondition allMatch(Predicate<T> value) {
        return getDefaultCondition().allMatch(singletonList(value));
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
        return getDefaultCondition().allMatch(asList(values));
    }

    /**
     * See {@link DefaultCondition#allMatch(Collection)}
     *
     * @param values the values to match
     * @return the step condition
     * @see DefaultCondition#allMatch(Collection)
     */
    default StepCondition allMatch(Collection<T> values) {
        return getDefaultCondition().allMatch(values);
    }

    /**
     * See {@link DefaultCondition#noneMatch(List)}
     *
     * @param value the value to match
     * @return the step condition
     * @see DefaultCondition#noneMatch(List)
     */
    default StepCondition noneMatch(Predicate<T> value) {
        return getDefaultCondition().noneMatch(singletonList(value));
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
        return getDefaultCondition().noneMatch(asList(values));
    }

    /**
     * See {@link DefaultCondition#noneMatch(Collection)}
     *
     * @param values the values to match
     * @return the step condition
     * @see DefaultCondition#noneMatch(Collection)
     */
    default StepCondition noneMatch(Collection<T> values) {
        return getDefaultCondition().noneMatch(values);
    }

    /**
     * See {@link DefaultCondition#mapToInt(Function)}
     *
     * @param mapper the mapper to apply
     * @return the step condition
     * @see DefaultCondition#mapToInt(Function)
     */
    default IntegerCondition mapToInt(Function<T, Integer> mapper) {
        return getDefaultCondition().mapToInt(mapper);
    }

    /**
     * Returns a new default condition that will use this as a field.
     *
     * @return the default condition
     */
    DefaultCondition<T> getDefaultCondition();

}
