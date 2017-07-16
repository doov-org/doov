package org.modelmap.core.dsl.field;

import org.modelmap.core.FieldId;

public class FloatFieldInfo extends DefaultFieldInfo<Float> {

    FloatFieldInfo(FieldId fieldId, Class<?> type, FieldId[] siblings) {
        super(fieldId, type, new Class[] {}, siblings);
    }
}
