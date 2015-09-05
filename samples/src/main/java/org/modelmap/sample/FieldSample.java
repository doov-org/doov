package org.modelmap.sample;

import org.modelmap.core.FieldId;

public enum FieldSample implements FieldId {
    FIRST_NAME,
    LAST_NAME,
    EMAIL,
    BIRTHDATE,
    LANGUAGE,
    TIMEZONE,

    PHONE_NUMBER,
    EMAIL_ACCEPTED,
    EMAILS_PREFERENCES,
    MAX_WEEKLY_EMAILS,

    LOGIN,
    PASSWORD,
    PASSWORD_CONFIRMATION;

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
