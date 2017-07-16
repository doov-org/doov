/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.lang;

import org.modelmap.core.FieldModel;

public interface ValidationRule {

    void executeOn(FieldModel model);

}
