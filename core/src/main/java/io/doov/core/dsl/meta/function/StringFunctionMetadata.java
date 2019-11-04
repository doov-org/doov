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
import static io.doov.core.dsl.meta.predicate.ValuePredicateMetadata.readableMetadata;
import static io.doov.core.dsl.meta.predicate.ValuePredicateMetadata.stringMetadata;
import static io.doov.core.dsl.meta.predicate.ValuePredicateMetadata.valueListMetadata;
import static io.doov.core.dsl.meta.predicate.ValuePredicateMetadata.valueMetadata;

import java.util.Arrays;
import java.util.Locale;

import io.doov.core.dsl.field.types.Function;
import io.doov.core.dsl.impl.base.StringFunction;
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
    public static StringFunctionMetadata containsMetadata(Metadata metadata, StringFunction value) {
        return new StringFunctionMetadata(metadata, contains, value.getMetadata());
    }

    public static StringFunctionMetadata startsWithMetadata(Metadata metadata, String value) {
        return new StringFunctionMetadata(metadata, starts_with, stringMetadata(value));
    }

    public static StringFunctionMetadata endsWithMetadata(Metadata metadata, String value) {
        return new StringFunctionMetadata(metadata, ends_with, stringMetadata(value));
    }

    public static StringFunctionMetadata replaceAllMetadata(Metadata metadata, String regex, String replacement) {
        return new StringFunctionMetadata(metadata, replace_all, valueListMetadata(Arrays.asList(regex, replacement)));
    }

    public static StringFunctionMetadata replaceAllMetadata(Metadata metadata, Function<String> value,
            Function<String> replacement) {
        return new StringFunctionMetadata(metadata, concat,
                readableMetadata(value.getMetadata(), replacement.getMetadata()));
    }

    public static StringFunctionMetadata substringMetadata(Metadata metadata, int beginIndex, int endIndex) {
        return new StringFunctionMetadata(metadata, substring, valueListMetadata(Arrays.asList(beginIndex, endIndex)));
    }

    public static StringFunctionMetadata substringMetadata(Metadata metadata, Function<Integer> value,
            Function<Integer> replacement) {
        return new StringFunctionMetadata(metadata, substring,
                readableMetadata(value.getMetadata(), replacement.getMetadata()));
    }



    public static StringFunctionMetadata upperCaseMetadata(Metadata metadata, Locale locale) {
        return new StringFunctionMetadata(metadata, upper_case, valueMetadata(locale));
    }

    public static StringFunctionMetadata lowerCaseMetadata(Metadata metadata, Locale locale) {
        return new StringFunctionMetadata(metadata, lower_case, valueMetadata(locale));
    }

    public static StringFunctionMetadata concatMetadata(Metadata metadata, String value) {
        return new StringFunctionMetadata(metadata, concat, stringMetadata(value));
    }

    public static StringFunctionMetadata concatMetadata(Metadata metadata, Function<String> value) {
        return new StringFunctionMetadata(metadata, concat, value.getMetadata());
    }

}
