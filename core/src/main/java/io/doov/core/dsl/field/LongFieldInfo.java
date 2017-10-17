/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.field;

import io.doov.core.FieldId;
import io.doov.core.dsl.impl.LongCondition;
import io.doov.core.dsl.lang.StepCondition;

public class LongFieldInfo extends DefaultFieldInfo<Long> implements NumericFieldInfo<Long> {

    LongFieldInfo(FieldId fieldId, String readable, Class<?> type, FieldId[] siblings) {
        super(fieldId, readable, type, new Class[] {}, siblings);
    }

    public StepCondition lesserThan(long value) {
        return new LongCondition(this).lesserThan(value);
    }

    public StepCondition lesserOrEquals(long value) {
        return new LongCondition(this).lesserOrEquals(value);
    }

    public StepCondition greaterThan(long value) {
        return new LongCondition(this).greaterThan(value);
    }

    public StepCondition greaterOrEquals(long value) {
        return new LongCondition(this).greaterOrEquals(value);
    }

    public StepCondition greaterOrEquals(LongFieldInfo field) {
        return new LongCondition(this).greaterOrEquals(field);
    }

    public StepCondition between(long minIncluded, long maxExcluded) {
        return new LongCondition(this).between(minIncluded, maxExcluded);
    }

}
