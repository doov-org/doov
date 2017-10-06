/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.field;

import org.modelmap.core.FieldId;

public class CharacterFieldInfo extends DefaultFieldInfo<Character> {

    CharacterFieldInfo(FieldId fieldId, String readable, Class<?> type, FieldId[] siblings) {
        super(fieldId, readable, type, new Class[] {}, siblings);
    }

    @Override
    public CharacterFieldInfo as(String readable) {
        return new CharacterFieldInfo(id(), readable, type(), siblings());
    }

}
