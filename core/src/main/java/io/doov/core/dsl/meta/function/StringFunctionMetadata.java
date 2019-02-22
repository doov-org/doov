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
import static io.doov.core.dsl.meta.predicate.ValuePredicateMetadata.fieldMetadata;
import static io.doov.core.dsl.meta.predicate.ValuePredicateMetadata.stringMetadata;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.impl.StringCondition;
import io.doov.core.dsl.impl.StringFunction;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.Operator;
import io.doov.core.dsl.meta.predicate.BinaryPredicateMetadata;

public class StringFunctionMetadata extends BinaryPredicateMetadata {

    public StringFunctionMetadata(Metadata left, Operator operator, Metadata right) {
        super(left, operator, right);
    }

    public static StringFunctionMetadata matchesMetadata(Metadata metadata, String value) {
        return new StringFunctionMetadata(metadata, matches, stringMetadata(value));
    }

    public static StringFunctionMetadata containsMetadata(Metadata metadata, String value) {
        return new StringFunctionMetadata(metadata, contains, stringMetadata(value));
    }
    public static StringFunctionMetadata containsMetadata(Metadata metadata, StringCondition value) {
        return new StringFunctionMetadata(metadata, contains, value.getMetadata());
    }

    public static StringFunctionMetadata startsWithMetadata(Metadata metadata, String value) {
        return new StringFunctionMetadata(metadata, starts_with, stringMetadata(value));
    }

    public static StringFunctionMetadata endsWithMetadata(Metadata metadata, String value) {
        return new StringFunctionMetadata(metadata, ends_with, stringMetadata(value));
    }

}
