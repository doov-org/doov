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

import static io.doov.core.dsl.meta.DefaultOperator.as;
import static io.doov.core.dsl.meta.DefaultOperator.as_a_number;
import static io.doov.core.dsl.meta.DefaultOperator.as_string;
import static io.doov.core.dsl.meta.DefaultOperator.with;
import static io.doov.core.dsl.meta.predicate.ValuePredicateMetadata.readableMetadata;
import static io.doov.core.dsl.meta.predicate.ValuePredicateMetadata.unknownMetadata;

import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.Operator;
import io.doov.core.dsl.meta.predicate.BinaryPredicateMetadata;

public class MapFunctionMetadata extends BinaryPredicateMetadata {

    public MapFunctionMetadata(Metadata left, Operator operator, Metadata right) {
        super(left, operator, right);
    }

    public static MapFunctionMetadata mapToIntMetadata(Metadata metadata) {
        return new MapFunctionMetadata(metadata, as_a_number, unknownMetadata(""));
    }

    public static MapFunctionMetadata mapToStringMetadata(Metadata metadata) {
        return new MapFunctionMetadata(metadata, as_string, unknownMetadata(""));
    }

    public static MapFunctionMetadata mapAsMetadata(Metadata metadata, String readable) {
        return new MapFunctionMetadata(metadata, as, readableMetadata(() -> readable));
    }

    public static MapFunctionMetadata mapUsingMetadata(Metadata metadata, String readable, Readable condition) {
        return new MapFunctionMetadata(
                new MapFunctionMetadata(metadata, as, readableMetadata(() -> readable)),
                with, readableMetadata(condition));
    }
}
