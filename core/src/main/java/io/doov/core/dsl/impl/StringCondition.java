/*
 * Copyright (C) by Courtanet, All Rights Reserved.
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
        return step(containsMetadata(field, regex),
                        model -> Optional.ofNullable(regex),
                        String::contains);
    }

    public final StepCondition matches(String regex) {
        return step(matchesMetadata(field, regex),
                        model -> Optional.ofNullable(regex),
                        String::matches);
    }

    public final StepCondition startsWith(String value) {
        return step(startsWithMetadata(field, value),
                        model -> Optional.ofNullable(value),
                        String::startsWith);
    }

    public final StepCondition endsWith(String value) {
        return step(endsWithMetadata(field, value),
                        model -> Optional.ofNullable(value),
                        String::endsWith);
    }

}
