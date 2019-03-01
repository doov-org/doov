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
package io.doov.core.dsl.lang;

import java.util.stream.Stream;

import io.doov.core.FieldModel;

/**
 * Mapping rule
 */
public interface MappingRule extends DSLBuilder {
    /**
     * Verifies the mapping rule for given in/out models
     *
     * @param inModel in model
     * @param outModel out model
     * @return true if this rule can execute on the in/out models
     */
    boolean validate(FieldModel inModel, FieldModel outModel);

    /**
     * Execute the mapping rule on in/out models
     *
     * @param inModel in model
     * @param outModel out model
     * @return context
     */
    Context executeOn(FieldModel inModel, FieldModel outModel);

    /**
     * Execute the mapping rule on in/out models with given context
     *
     * @param inModel in model
     * @param outModel out model
     * @param <C> context type
     * @param context context
     * @return context
     */
    <C extends Context> C executeOn(FieldModel inModel, FieldModel outModel, C context);

    /**
     * Stream over mapping rules contained in this rule Default implementation returns a stream of itself.
     *
     * @return mapping rule stream
     */
    default Stream<MappingRule> stream() {
        return Stream.of(this);
    }
}
