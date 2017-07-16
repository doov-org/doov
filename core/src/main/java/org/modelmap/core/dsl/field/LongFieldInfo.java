package org.modelmap.core.dsl.field;

import org.modelmap.core.FieldId;

public class LongFieldInfo extends DefaultFieldInfo<Long> {

    LongFieldInfo(FieldId fieldId, Class<?> type, FieldId[] siblings) {
        super(fieldId, type, new Class[] {}, siblings);
    }
}
