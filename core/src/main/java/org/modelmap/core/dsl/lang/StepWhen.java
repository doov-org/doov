/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.lang;

import java.util.Optional;

import org.modelmap.core.dsl.meta.Readable;

public interface StepWhen extends ValidationRule, Readable {

    StepWhen withMessage(String message);

    StepCondition stepCondition();

    Optional<String> message();

    ValidationRule validationRule();

}
