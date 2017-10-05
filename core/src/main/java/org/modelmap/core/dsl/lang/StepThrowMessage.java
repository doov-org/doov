/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.lang;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.meta.Readable;

public interface StepThrowMessage extends Readable {

    String message();

    StepWhen stepWhen();

    void executeOn(FieldModel model);

    ValidationRule validationRule();

}
