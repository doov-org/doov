package org.modelmap.core.dsl.field;

import org.modelmap.core.FieldId;

public class CharacterFieldInfo extends DefaultFieldInfo<Character> {

    CharacterFieldInfo(FieldId fieldId, Class<?> type, FieldId[] siblings) {
        super(fieldId, type, new Class[] {}, siblings);
    }
}
