package org.modelmap.core.dsl.field;

import org.modelmap.core.FieldId;

public class BooleanFieldInfo extends DefaultFieldInfo<Boolean> {

    BooleanFieldInfo(FieldId fieldId, Class<?> type, FieldId[] siblings) {
        super(fieldId, type, new Class[] {}, siblings);
    }
}
