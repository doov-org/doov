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

import io.doov.core.FieldModel;

/**
 * Bi type converter
 *
 * @param <I> 1st in type
 * @param <J> 2nd in type
 * @param <O> out type
 */
public interface BiTypeConverter<I, J, O> extends DSLBuilder {
    /**
     * Convert the given fields in with type {@link O} {@link J}, the model to the value in type {@link O}
     *
     * @param fieldModel field model
     * @param context context
     * @param in 1st input
     * @param in2 2nd input
     * @return output value
     */
    O convert(FieldModel fieldModel, Context context, I in, J in2);
}
