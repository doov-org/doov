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
package io.doov.core.dsl.impl;

import static io.doov.core.dsl.meta.predicate.UnaryPredicateMetadata.lengthIsMetadata;

import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.impl.num.IntegerFunction;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

public class StringFunction extends StringCondition {

    public StringFunction(DslField<String> field) {
        super(field);
    }

    public StringFunction(PredicateMetadata metadata, BiFunction<DslModel, Context, Optional<String>> value) {
        super(metadata, value);
    }

    /**
     * Returns an integer condition that returns the node value length.
     *
     * @return the integer condition
     */
    public IntegerFunction length() {
        return new IntegerFunction(lengthIsMetadata(metadata),
                (model, context) -> value(model, context).map(String::length));
    }

    /**
     * Returns an integer condition that returns the node value as an integer.
     *
     * @return the integer condition
     */
    public IntegerFunction parseInt() {
        return new IntegerFunction(metadata,
                (model, context) -> value(model, context).map(Integer::parseInt));
    }
}
