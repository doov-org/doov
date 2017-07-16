/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.lang;

import org.modelmap.core.FieldModel;

public interface StepThrowMessage {

    String message();

    StepWhen stepWhen();

    void executeOn(FieldModel model);

    ValidationRule validationRule();
}
