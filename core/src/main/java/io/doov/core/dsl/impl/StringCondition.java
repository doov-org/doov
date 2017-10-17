/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.impl;

import static io.doov.core.dsl.meta.FieldMetadata.containsMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.endsWithMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.matchesMetadata;
import static io.doov.core.dsl.meta.FieldMetadata.startsWithMetadata;

import java.util.Optional;
import java.util.function.BiPredicate;

import io.doov.core.FieldModel;
import io.doov.core.dsl.field.StringFieldInfo;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.FieldMetadata;

public class StringCondition extends AbstractStepCondition {

    private StringCondition(FieldMetadata metadata, BiPredicate<FieldModel, Context> predicate) {
        super(metadata, predicate);
    }

    public static StringCondition contains(StringFieldInfo field, String value) {
        return new StringCondition(containsMetadata(field, value),
                        (model, context) -> value(model, field).map(v -> v.contains(value)).orElse(false));
    }

    public static StringCondition matches(StringFieldInfo field, String regex) {
        return new StringCondition(matchesMetadata(field, regex),
                        (model, context) -> value(model, field).map(v -> v.matches(regex)).orElse(false));
    }

    public static StringCondition startsWith(StringFieldInfo field, String prefix) {
        return new StringCondition(startsWithMetadata(field, prefix),
                        (model, context) -> value(model, field).map(v -> v.startsWith(prefix)).orElse(false));
    }

    public static StringCondition endsWith(StringFieldInfo field, String suffix) {
        return new StringCondition(endsWithMetadata(field, suffix),
                        (model, context) -> value(model, field).map(v -> v.endsWith(suffix)).orElse(false));
    }

    public static Optional<String> value(FieldModel fieldModel, StringFieldInfo field) {
        return Optional.ofNullable(fieldModel.<String> get(field.id()));
    }

}
