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

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.mapping.*;

/**
 * First step for creating mapping rule.
 * Associates the in field with type {@link I} with a converter or the out field with type {@link I}
 *
 * @param <I> in type
 */
public class SimpleStepMap<I> {

    private final MappingInput<I> input;

    public SimpleStepMap(MappingInput<I> input) {
        this.input = input;
    }

    public SimpleStepMap(DslField<I> inFieldInfo) {
        this(new FieldInput<>(inFieldInfo));
    }

    /**
     * Return the step mapping
     *
     * @param typeConverter type converter
     * @param <O>           out type
     * @return the step mapping
     */
    public <O> SimpleStepMap<O> using(TypeConverter<I, O> typeConverter) {
        return new SimpleStepMap<>(new ConverterInput<>(input, typeConverter));
    }

    /**
     * Return the mapping rule
     *
     * @param output consumer output
     * @return the mapping rule
     */
    public DefaultMappingRule<I> to(MappingOutput<I> output) {
        return new DefaultMappingRule<>(input, output);
    }

    /**
     * Return the mapping rule
     *
     * @param outFieldInfo out field info
     * @return the mapping rule
     */
    public DefaultMappingRule<I> to(DslField<I> outFieldInfo) {
        return this.to(new FieldOutput<>(outFieldInfo));
    }

    /**
     * Return the mapping rule
     *
     * @param consumer out field info
     * @return the mapping rule
     */
    public DefaultMappingRule<I> to(TriConsumer<DslModel, Context, I> consumer) {
        return this.to(new ConsumerOutput<>(consumer));
    }
}
