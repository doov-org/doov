/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.impl;

import static org.modelmap.core.dsl.meta.FieldMetadata.CONTAINS;
import static org.modelmap.core.dsl.meta.FieldMetadata.ENDS_WITH;
import static org.modelmap.core.dsl.meta.FieldMetadata.MATCHES;
import static org.modelmap.core.dsl.meta.FieldMetadata.STARTS_WITH;

import java.util.Optional;
import java.util.function.Predicate;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.field.StringFieldInfo;
import org.modelmap.core.dsl.meta.FieldMetadata;

public class StringCondition extends AbstractStepCondition<StringFieldInfo, String> {

    private StringCondition(FieldMetadata<StringFieldInfo, String> metadata, Predicate<FieldModel> predicate) {
        super(metadata, predicate);
    }

    @Override
    public Predicate<FieldModel> predicate() {
        return predicate;
    }

    @Override
    public String readable() {
        return metadata.readable();
    }

    public static StringCondition contains(StringFieldInfo field, String value) {
        return new StringCondition(new FieldMetadata<>(field, CONTAINS, value),
                        fieldContext -> value(fieldContext, field).map(v -> v.contains(value)).orElse(false));
    }

    public static StringCondition matches(StringFieldInfo field, String regex) {
        return new StringCondition(new FieldMetadata<>(field, MATCHES, regex),
                        fieldContext -> value(fieldContext, field).map(v -> v.matches(regex)).orElse(false));
    }

    public static StringCondition startsWith(StringFieldInfo field, String prefix) {
        return new StringCondition(new FieldMetadata<>(field, STARTS_WITH, prefix),
                        fieldContext -> value(fieldContext, field).map(v -> v.startsWith(prefix)).orElse(false));
    }

    public static StringCondition endsWith(StringFieldInfo field, String suffix) {
        return new StringCondition(new FieldMetadata<>(field, ENDS_WITH, suffix),
                        fieldContext -> value(fieldContext, field).map(v -> v.endsWith(suffix)).orElse(false));
    }

    public static Optional<String> value(FieldModel fieldModel, StringFieldInfo field) {
        return Optional.ofNullable(fieldModel.<String> get(field.id()));
    }

}
