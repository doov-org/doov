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

import java.util.function.Supplier;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.field.types.ContextAccessor;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.mapping.*;

/**
 * First step for creating a static mapping rule.
 *
 * @param <I> in type
 */
public class StaticStepMap<I> {

    private final ContextAccessor<I> input;

    public StaticStepMap(Supplier<I> input) {
        this(new StaticInput<>(input));
    }

    private StaticStepMap(ContextAccessor<I> input) {
        this.input = input;
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

    /**
     * Return the step mapping
     *
     * @param typeConverter static type converter
     * @param <O>           out type
     * @return the step mapping
     */
    public <O> StaticStepMap<O> using(TypeConverter<I, O> typeConverter) {
        return new StaticStepMap<>(new ConverterInput<>(input, typeConverter));
    }

}
