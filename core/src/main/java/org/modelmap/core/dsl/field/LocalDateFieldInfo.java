package org.modelmap.core.dsl.field;

import java.time.LocalDate;

import org.modelmap.core.FieldId;

public class LocalDateFieldInfo extends DefaultFieldInfo<LocalDate> {

    LocalDateFieldInfo(FieldId fieldId, FieldId[] siblings) {
        super(fieldId, LocalDate.class, new Class[] {}, siblings);
    }
}
