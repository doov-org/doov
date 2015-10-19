/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.example;

import java.util.Map;
import java.util.Objects;

import org.modelmap.core.FieldId;

final class ValueDifference {

    static ValueDifference left(Map.Entry<FieldId, Object> entry) {
        return new ValueDifference(entry.getKey(), entry.getValue(), null);
    }

    static ValueDifference right(Map.Entry<FieldId, Object> entry) {
        return new ValueDifference(entry.getKey(), null, entry.getValue());
    }

    static ValueDifference merge(ValueDifference d1, ValueDifference d2) {
        Object left = d1.left != null ? d1.left : d2.left;
        Object right = d2.right != null ? d2.right : d1.right;
        return new ValueDifference(d1.key, left, right);
    }

    private final FieldId key;
    private final Object left, right;

    ValueDifference(FieldId key, Object left, Object right) {
        this.key = key;
        this.left = left;
        this.right = right;
    }

    public FieldId getKey() {
        return key;
    }

    boolean isEquals() {
        return Objects.equals(left, right);
    }

    @Override
    public String toString() {
        return key + ";" + String.valueOf(left) + ";" + String.valueOf(right);
    }
}
