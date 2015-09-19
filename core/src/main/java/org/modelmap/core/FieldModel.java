/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core;

import java.util.List;
import java.util.Set;

public interface FieldModel {

    <T> T get(FieldId fieldId);

    void set(FieldId fieldId, Object value);

    void clear(TagId tag);

    void clear();

    void setAll(FieldModel context);

    Set<FieldInfo> getFieldInfos();

    /**
     * @return all field ids with a not-null value
     */
    List<FieldId> getFields();

    /**
     * * @return all field ids
     */
    List<FieldId> getAllFields();
}
