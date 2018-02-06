/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.serial;

import io.doov.core.FieldId;
import io.doov.core.FieldInfo;

public interface StringMapper {

    String getAsString(FieldId fieldId);

    String getAsString(FieldInfo fieldInfo);

    void setAsString(FieldId fieldId, String value);

    void setAsString(FieldInfo fieldInfo, String value);
}
