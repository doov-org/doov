/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.field;

import org.modelmap.core.FieldInfo;
import org.modelmap.core.dsl.lang.StepCondition;

public interface NumericFieldInfo<T extends Number> extends FieldInfo {

    StepCondition lessThan(T value);

    StepCondition lessOrEqual(T value);

    StepCondition greaterThan(T value);

    StepCondition greaterOrEqual(T value);
}
