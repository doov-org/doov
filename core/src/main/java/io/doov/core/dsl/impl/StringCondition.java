/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import java.util.Optional;
import java.util.function.Predicate;

import io.doov.core.FieldModel;
import io.doov.core.dsl.field.StringFieldInfo;
import io.doov.core.dsl.meta.FieldMetadata;

public class StringCondition extends AbstractStepCondition {

    private StringCondition(FieldMetadata metadata, Predicate<FieldModel> predicate) {
        super(metadata, predicate);
    }

    public static StringCondition contains(StringFieldInfo field, String value) {
        return new StringCondition(FieldMetadata.containsMetadata(field, value),
                        fieldContext -> value(fieldContext, field).map(v -> v.contains(value)).orElse(false));
    }

    public static StringCondition matches(StringFieldInfo field, String regex) {
        return new StringCondition(FieldMetadata.matchesMetadata(field, regex),
                        fieldContext -> value(fieldContext, field).map(v -> v.matches(regex)).orElse(false));
    }

    public static StringCondition startsWith(StringFieldInfo field, String prefix) {
        return new StringCondition(FieldMetadata.startsWithMetadata(field, prefix),
                        fieldContext -> value(fieldContext, field).map(v -> v.startsWith(prefix)).orElse(false));
    }

    public static StringCondition endsWith(StringFieldInfo field, String suffix) {
        return new StringCondition(FieldMetadata.endsWithMetadata(field, suffix),
                        fieldContext -> value(fieldContext, field).map(v -> v.endsWith(suffix)).orElse(false));
    }

    public static Optional<String> value(FieldModel fieldModel, StringFieldInfo field) {
        return Optional.ofNullable(fieldModel.<String> get(field.id()));
    }

}
