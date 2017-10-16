/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.meta;

import org.modelmap.core.FieldInfo;
import org.modelmap.core.dsl.field.IntegerFieldInfo;
import org.modelmap.core.dsl.field.StringFieldInfo;

public class FieldMetadata<F extends FieldInfo, V> extends AbstractMetadata {

    public static final String EQUALS = "equals";
    public static final String NOT_EQUALS = "not equals";

    public static final String AFTER = "after";
    public static final String BEFORE = "before";

    public static final String CONTAINS = "contains";
    public static final String MATCHES = "matches";
    public static final String STARTS_WITH = "starts with";
    public static final String ENDS_WITH = "ends with";

    public static final String IS = "is";

    public static final String LESSER_THAN = "lesser than";
    public static final String LESSER_OR_EQUALS = "lesser or equals";
    public static final String GREATER_THAN = "greater than";
    public static final String GREATER_OR_EQUALS = "greater or equals";
    public static final String BETWEEN = "between";
    public static final String LENGTH = "length";

    public final F field;
    public final String operator;
    public final Object value;

    public FieldMetadata(F field, String operator, V value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    public static FieldMetadata<IntegerFieldInfo, Integer> lesserThan(IntegerFieldInfo field, int value) {
        return new FieldMetadata<>(field, LESSER_THAN, value);
    }

    public static FieldMetadata<IntegerFieldInfo, Integer> lesserOrEquals(IntegerFieldInfo field, int value) {
        return new FieldMetadata<>(field, LESSER_OR_EQUALS, value);
    }

    public static FieldMetadata<IntegerFieldInfo, Integer> greaterThan(IntegerFieldInfo field, int value) {
        return new FieldMetadata<>(field, GREATER_THAN, value);
    }

    public static FieldMetadata<IntegerFieldInfo, Integer> greaterOrEquals(IntegerFieldInfo field, int value) {
        return new FieldMetadata<>(field, GREATER_OR_EQUALS, value);
    }

    public static FieldMetadata<IntegerFieldInfo, ?> between(IntegerFieldInfo field, int min, int max) {
        return new FieldMetadata<>(field, BETWEEN, min + " and " + max);
    }

    public static FieldMetadata<StringFieldInfo, ?> length(StringFieldInfo field) {
        return new FieldMetadata<>(field, LENGTH, null);
    }

    public static <T extends FieldInfo> FieldMetadata<T, ?> combine(FieldMetadata<T, ?> metadata1,
                    FieldMetadata<?, ?> metadata2) {
        return new FieldMetadata<>(metadata1.field, metadata1.operator, metadata2.value);
    }

    @Override
    public String readable() {
        return field.readable() + " " + operator + " " + value;
    }

}
