/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.lang;

import org.modelmap.core.dsl.meta.Readable;

public interface StepWhen extends Readable {

    StepCondition stepCondition();

    StepThrowMessage throwMessage(String message);

}
