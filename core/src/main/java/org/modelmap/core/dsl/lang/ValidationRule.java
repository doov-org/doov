/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.lang;

import java.util.Optional;

import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.meta.Readable;

public interface ValidationRule extends Readable {

    Optional<String> executeOn(FieldModel model);

}
