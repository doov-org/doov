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

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.field.types.ContextAccessor;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.mapping.*;
import io.doov.core.dsl.mapping.input.BiConverterInput;
import io.doov.core.dsl.mapping.input.FieldInput;
import io.doov.core.dsl.mapping.output.ConsumerOutput;
import io.doov.core.dsl.mapping.output.FieldOutput;

/**
 * First step for creating mapping rule.
 * Associates field with type {@link I} and {@link J} with a bi-converter.
 *
 * @param <I> 1st in type
 * @param <J> 2nd in type
 */
public class BiStepMap<I, J> {

    private final DslField<I> inFieldInfo;
    private final DslField<J> in2FieldInfo;

    public BiStepMap(DslField<I> inFieldInfo, DslField<J> in2FieldInfo) {
        this.inFieldInfo = inFieldInfo;
        this.in2FieldInfo = in2FieldInfo;
    }

    /**
     * Returns bi step mapping
     *
     * @param typeConverter bi type converter
     * @param <O>           out type
     * @return bi step mapping
     */
    public <O> BiStepMapping<I, J, O> using(BiTypeConverter<I, J, O> typeConverter) {
        return new BiStepMapping<>(inFieldInfo, in2FieldInfo, typeConverter);
    }

    /**
     * Intermediary step for creating a bi mapping rule.
     * Associates in fields with type {@link I} and {@link J} with the out field with type {@link O}
     *
     * @param <I> 1st in type
     * @param <J> 2nd in type
     * @param <O> out type
     */
    public class BiStepMapping<I, J, O> {

        private final ContextAccessor<O> input;

        BiStepMapping(DslField<I> inFieldInfo, DslField<J> in2FieldInfo, BiTypeConverter<I, J, O> typeConverter) {
            this.input = new BiConverterInput<>(
                    new FieldInput<>(inFieldInfo),
                    new FieldInput<>(in2FieldInfo),
                    typeConverter);
        }

        /**
         * Return the bi mapping rule
         *
         * @param outFieldInfo out field
         * @return bi mapping rule
         */
        public DefaultMappingRule<O> to(DslField<O> outFieldInfo) {
            return new DefaultMappingRule<>(input, new FieldOutput<>(outFieldInfo));
        }

        /**
         * Return the bi mapping rule
         *
         * @param output consumer output
         * @return bi mapping rule
         */
        public DefaultMappingRule<O> to(ConsumerOutput<O> output) {
            return new DefaultMappingRule<>(input, output);
        }

        /**
         * Return the bi mapping rule
         *
         * @param consumer consumer
         * @return bi mapping rule
         */
        public DefaultMappingRule<O> to(TriConsumer<FieldModel, Context, O> consumer) {
            return new DefaultMappingRule<>(input, new ConsumerOutput<>(consumer));
        }

    }


}
