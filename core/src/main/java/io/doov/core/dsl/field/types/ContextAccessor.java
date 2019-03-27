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

import java.util.Optional;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.Metadata;

public interface ContextAccessor<T> {

    /**
     * Verifies the input for given in model
     *
     * @param model the model
     * @param context the context
     * @return true if the input can read a value from the model
     */
    Optional<T> value(FieldModel model, Context context);

    /**
     * Verifies the input for given in model
     *
     * @param model in model
     * @return true if the input can read a value from the model
     */
    boolean validate(FieldModel model);

    Metadata metadata();
}
