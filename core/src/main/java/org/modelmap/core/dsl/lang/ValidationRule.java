/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.lang;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.meta.Readable;

public interface ValidationRule extends Readable {

    ValidationRule withMessage(String message);

    Result executeOn(FieldModel model);
    
    ValidationRule registerOn(RuleRegistry registry);

}
