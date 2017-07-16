package org.modelmap.core.dsl.field;

import org.modelmap.core.FieldId;

public class DoubleFieldInfo extends DefaultFieldInfo<Double> {

    DoubleFieldInfo(FieldId fieldId, Class<?> type, FieldId[] siblings) {
        super(fieldId, type, new Class[] {}, siblings);
    }
}
