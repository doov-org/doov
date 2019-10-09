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
package io.doov.core.dsl.mapping.builder;

import java.util.List;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.impl.DefaultFunction;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.mapping.*;
import io.doov.core.dsl.meta.Metadata;

/**
 * First step for creating a static mapping rule.
 *
 * @param <I> in type
 */
public class IterableStepMap<E, I extends Iterable<E>> {

    private final MappingInput<I> input;

    public IterableStepMap(Supplier<I> input) {
        this(new IterableInput<>(input));
    }

    private IterableStepMap(MappingInput<I> input) {
        this.input = input;
    }

    public MappingInput<I> getInput() {
        return input;
    }

    /**
     * Return the static mapping rule
     *
     * @param output consumer output
     * @return the static mapping rule
     */
    public DefaultMappingRule<I> to(MappingOutput<I> output) {
        return new DefaultMappingRule<>(input, output);
    }

    /**
     * Return the static mapping rule
     *
     * @param outFieldInfo out field info
     * @return the static mapping rule
     */
    public DefaultMappingRule<I> to(DslField<I> outFieldInfo) {
        return this.to(new FieldOutput<>(outFieldInfo));
    }

    /**
     * Return the static mapping rule
     *
     * @param consumer consumer
     * @return the static mapping rule
     */
    public DefaultMappingRule<I> to(TriConsumer<FieldModel, Context, I> consumer) {
        return this.to(new ConsumerOutput<>(consumer));
    }

    public <O> IterableStepMap<O, List<O>> map(TypeConverter<E, O> converter) {
        return new IterableStepMap<>(new MapEachInput<>(input, converter));
    }

    public <O> IterableStepMap<O, List<O>> map(Function<E, O> converterFunction, String description) {
        return this.map(TypeConverters.converter(converterFunction, description));
    }

    public IterableStepMap<E, List<E>> filter(TypeConverter<E, Boolean> converter) {
        return new IterableStepMap<>(new FilterInput<>(input, converter));
    }

    public IterableStepMap<E, List<E>> filter(Predicate<E> predicateFunction, String description) {
        return this.filter(TypeConverters.converter(predicateFunction::test, description));
    }

    public IterableStepMap<E, List<E>> filter(Function<DefaultFunction<E, ? extends Metadata>, StepCondition> conditionFunction) {
        return new IterableStepMap<>(new FilterConditionInput<>(input, conditionFunction));
    }

    public <T> SimpleStepMap<T> reduce(TypeConverter<Stream<E>, T> converter) {
        return new SimpleStepMap<>(new ReduceInput<>(input, converter));
    }

    public <T> SimpleStepMap<T> reduce(Function<Stream<E>, T> converterFunction, String description) {
        return this.reduce(TypeConverters.converter(converterFunction, description));
    }

    public <T extends Iterable<E>> SimpleStepMap<T> collect(Collector<E, ?, T> collector) {
        return new SimpleStepMap<>(new CollectInput<>(input, collector));
    }

    public static <O> IterableStepMap<O, List<O>> concat(IterableStepMap<O, ? extends Iterable<O>>... iterables) {
        return new IterableStepMap<>(new ConcatInput<>(iterables));
    }
}
