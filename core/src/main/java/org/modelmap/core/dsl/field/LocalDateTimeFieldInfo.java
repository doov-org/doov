package org.modelmap.core.dsl.field;

import java.time.LocalTime;

import org.modelmap.core.FieldId;

public class LocalDateTimeFieldInfo extends DefaultFieldInfo<LocalTime> {

    LocalDateTimeFieldInfo(FieldId fieldId, FieldId[] siblings) {
        super(fieldId, LocalTime.class, new Class[] {}, siblings);
    }
}
