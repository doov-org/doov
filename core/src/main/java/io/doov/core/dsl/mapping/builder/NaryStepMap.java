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

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.mapping.*;

/**
 * First step for creating a n-ary mapping rule.
 * Associates the in fields with with a generic converter
 */
public class NaryStepMap {

    private final List<DslField<?>> fieldInfos;

    public NaryStepMap(List<DslField<?>> fieldInfos) {
        this.fieldInfos = fieldInfos;
    }

    /**
     * Return the step mapping
     *
     * @param typeConverter type converter
     * @param <O>           out type
     * @return the step mapping
     */
    public <O> NaryStepMapping<O> using(NaryTypeConverter<O> typeConverter) {
        return new NaryStepMapping<>(fieldInfos, typeConverter);
    }

    /**
     * Intermediary step for creating n-ary mapping rule.
     * Associates in fields with the out field with type {@link O}
     *
     * @param <O> out type
     */
    public class NaryStepMapping<O> {

        private final NaryConverterInput<O> input;

        private NaryStepMapping(List<DslField<?>> fieldInfos, NaryTypeConverter<O> typeConverter) {
            this.input = new NaryConverterInput<>(fieldInfos, typeConverter);
        }

        /**
         * Return the n-ary mapping rule
         *
         * @param output consumer output
         * @return n-ary mapping rule
         */
        public DefaultMappingRule<O> to(MappingOutput<O> output) {
            return new DefaultMappingRule<>(input, output);
        }

        /**
         * Return the n-ary mapping rule
         *
         * @param outFieldInfo out field info
         * @return n-ary mapping rule
         */
        public DefaultMappingRule<O> to(DslField<O> outFieldInfo) {
            return this.to(new FieldOutput<>(outFieldInfo));
        }

        /**
         * Return the n-ary mapping rule
         *
         * @param consumer out field info
         * @return n-ary mapping rule
         */
        public DefaultMappingRule<O> to(TriConsumer<DslModel, Context, O> consumer) {
            return this.to(new ConsumerOutput<>(consumer));
        }

    }

}
