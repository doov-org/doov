/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.meta;

import org.modelmap.core.FieldInfo;

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

    public final F field;
    public final String operator;
    public final V value;

    public FieldMetadata(F field, String operator, V value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public String readable() {
        return field.readable() + " " + operator + " " + value;
    }

}
