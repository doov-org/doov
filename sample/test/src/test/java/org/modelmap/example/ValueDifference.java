/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.example;

import java.util.Map;
import java.util.Objects;

import org.modelmap.core.FieldId;

final class ValueDifference {

    static ValueDifference left(Map.Entry<FieldId, Object> entry) {
        ValueDifference diff = new ValueDifference(entry.getKey());
        diff.left = entry.getValue();
        return diff;
    }

    static ValueDifference right(Map.Entry<FieldId, Object> entry) {
        ValueDifference diff = new ValueDifference(entry.getKey());
        diff.right = entry.getValue();
        return diff;
    }

    static ValueDifference merge(ValueDifference d1, ValueDifference d2) {
        if (d2.left != null)
            d1.left = d2.left;
        if (d2.right != null)
            d1.right = d2.right;
        return d1;
    }

    final FieldId fieldId;
    Object left;
    Object right;

    ValueDifference(FieldId fieldId) {
        this.fieldId = fieldId;
    }

    boolean isEquals() {
        return Objects.equals(left, right);
    }

    @Override
    public String toString() {
        return fieldId + ";" + String.valueOf(left) + ";" + String.valueOf(right);
    }
}
