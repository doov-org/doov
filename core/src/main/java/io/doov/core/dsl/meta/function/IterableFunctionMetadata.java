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

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.predicate.ValuePredicateMetadata.valueMetadata;
import static io.doov.core.dsl.meta.predicate.ValuePredicateMetadata.valueListMetadata;

import java.util.Collection;

import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.Operator;
import io.doov.core.dsl.meta.predicate.BinaryPredicateMetadata;

public class IterableFunctionMetadata extends BinaryPredicateMetadata {

    public IterableFunctionMetadata(Metadata left, Operator operator, Metadata right) {
        super(left, operator, right);
    }

    public static IterableFunctionMetadata containsMetadata(Metadata metadata, Object value) {
        return new IterableFunctionMetadata(metadata, contains, valueMetadata(value));
    }

    public static IterableFunctionMetadata containsMetadata(Metadata metadata, Collection<Object> values) {
        return new IterableFunctionMetadata(metadata, contains, valueListMetadata(values));
    }

    // size

    public static IterableFunctionMetadata hasSizeMetadata(Metadata metadata, int size) {
        return new IterableFunctionMetadata(metadata, has_size, valueMetadata(size));
    }

    public static IterableFunctionMetadata hasNotSizeMetadata(Metadata metadata, int size) {
        return new IterableFunctionMetadata(metadata, has_not_size, valueMetadata(size));
    }

}
