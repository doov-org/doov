/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.lang;

import io.doov.core.FieldModel;
import io.doov.core.dsl.meta.Readable;

public interface ValidationRule extends Readable {

    ValidationRule withMessage(String message);

    Result executeOn(FieldModel model);

    ValidationRule registerOn(RuleRegistry registry);

    ValidationRule registerOn(RuleRegistry registry, RuleId id);

}
