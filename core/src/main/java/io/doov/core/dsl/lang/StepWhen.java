/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.lang;

import io.doov.core.dsl.meta.Readable;

public interface StepWhen extends Readable {

    StepCondition stepCondition();

    ValidationRule validate();

}
