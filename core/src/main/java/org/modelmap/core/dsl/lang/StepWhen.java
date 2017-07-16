/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.lang;

public interface StepWhen {
    StepCondition stepCondition();
    
    StepThrowMessage throwMessage(String message);
}
