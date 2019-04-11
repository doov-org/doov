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
package io.doov.core.dsl.field.types;

import io.doov.core.FieldInfo;
import io.doov.core.dsl.field.BaseFieldInfo;
import io.doov.core.dsl.field.DelegatingFieldInfoImpl;
import io.doov.core.dsl.impl.DefaultCondition;
import io.doov.core.dsl.impl.base.IterableFunction;
import io.doov.core.dsl.lang.StepCondition;

public class IterableFieldInfo<T, C extends Iterable<T>> extends DelegatingFieldInfoImpl implements BaseFieldInfo<C> {

    public IterableFieldInfo(FieldInfo fieldInfo) {
        super(fieldInfo);
    }

    @Override
    public DefaultCondition<C> getDefaultFunction() {
        return new DefaultCondition<>(this);
    }

    public StepCondition contains(T value) {
        return new IterableFunction<>(this).contains(value);
    }

    @SafeVarargs
    public final StepCondition containsAll(T... values) {
        return new IterableFunction<>(this).containsAll(values);
    }

    public StepCondition isEmpty() {
        return new IterableFunction<>(this).isEmpty();
    }

    public StepCondition isNotEmpty() {
        return new IterableFunction<>(this).isNotEmpty();
    }

    public StepCondition hasSize(int size) {
        return new IterableFunction<>(this).hasSize(size);
    }

    public StepCondition hasNotSize(int size) {
        return new IterableFunction<>(this).hasNotSize(size);
    }

}
