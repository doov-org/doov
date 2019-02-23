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
package io.doov.core.dsl.meta.function;

import static io.doov.core.dsl.meta.DefaultOperator.and;
import static io.doov.core.dsl.meta.DefaultOperator.is;
import static io.doov.core.dsl.meta.DefaultOperator.or;
import static io.doov.core.dsl.meta.predicate.ValuePredicateMetadata.readableMetadata;
import static io.doov.core.dsl.meta.predicate.ValuePredicateMetadata.valueMetadata;

import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.Operator;
import io.doov.core.dsl.meta.predicate.BinaryPredicateMetadata;

public class BooleanFunctionMetadata extends BinaryPredicateMetadata {

    public BooleanFunctionMetadata(Metadata left, Operator operator, Metadata right) {
        super(left, operator, right);
    }

    // boolean

    public static BooleanFunctionMetadata andMetadata(Metadata metadata, boolean value) {
        return new BooleanFunctionMetadata(metadata, and, valueMetadata(value));
    }

    public static BooleanFunctionMetadata andMetadata(Metadata metadata, Readable value) {
        return new BooleanFunctionMetadata(metadata, and, readableMetadata(value));
    }

    public static BooleanFunctionMetadata orMetadata(Metadata metadata, boolean value) {
        return new BooleanFunctionMetadata(metadata, or, valueMetadata(value));
    }

    public static BooleanFunctionMetadata orMetadata(Metadata metadata, Readable value) {
        return new BooleanFunctionMetadata(metadata, or, readableMetadata(value));
    }

    public static BooleanFunctionMetadata isMetadata(Metadata metadata, boolean value) {
        return new BooleanFunctionMetadata(metadata, is, valueMetadata(value));
    }
}
