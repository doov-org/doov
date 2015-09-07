package org.modelmap.sample.field;

import org.modelmap.core.FieldId;

public enum SampleFieldId implements FieldId {
    FIRST_NAME,
    LAST_NAME,
    EMAIL,
    BIRTHDATE,
    LANGUAGE,
    TIMEZONE,

    PHONE_NUMBER,
    EMAIL_ACCEPTED,
    EMAILS_PREFERENCES,

    LOGIN,
    PASSWORD,;

    @Override
    public String getCode() {
        return name();
    }

    // FIXME add some example with lists with index
    @Override
    public int position() {
        return -1;
    }
}
