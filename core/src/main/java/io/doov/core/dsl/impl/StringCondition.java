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

import static io.doov.core.dsl.meta.FieldMetadata.containsMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.endsWithMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.matchesMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.startsWithMetadata;

import java.util.Optional;
import java.util.function.Function;

import io.doov.core.FieldModel;
import io.doov.core.dsl.field.StringFieldInfo;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.FieldMetadata;

public class StringCondition extends DefaultCondition<StringFieldInfo, String> {

    public StringCondition(StringFieldInfo field) {
        super(field);
    }

    public StringCondition(FieldMetadata metadata, Function<FieldModel, Optional<String>> value) {
        super(metadata, value);
    }

    public final StepCondition contains(String regex) {
        return predicate(containsMetadata(field, regex),
                        model -> Optional.ofNullable(regex),
                        String::contains);
    }

    public final StepCondition matches(String regex) {
        return predicate(matchesMetadata(field, regex),
                        model -> Optional.ofNullable(regex),
                        String::matches);
    }

    public final StepCondition startsWith(String value) {
        return predicate(startsWithMetadata(field, value),
                        model -> Optional.ofNullable(value),
                        String::startsWith);
    }

    public final StepCondition endsWith(String value) {
        return predicate(endsWithMetadata(field, value),
                        model -> Optional.ofNullable(value),
                        String::endsWith);
    }

}
