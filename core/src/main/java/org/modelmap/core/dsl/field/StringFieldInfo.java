package org.modelmap.core.dsl.field;

import org.modelmap.core.FieldId;

public class StringFieldInfo extends DefaultFieldInfo<String> {

    StringFieldInfo(FieldId fieldId, FieldId[] siblings) {
        super(fieldId, String.class, new Class[] {}, siblings);
    }
}
