package org.modelmap.sample.field;

import org.modelmap.core.FieldId;

public enum SampleFieldId implements FieldId {

    ACCOUNT_ID,
    USER_ID,

    FIRST_NAME,
    LAST_NAME,
    FULLNAME,
    EMAIL,
    BIRTHDATE,
    LANGUAGE,
    TIMEZONE,

    PHONE_NUMBER,
    EMAIL_ACCEPTED,
    EMAILS_PREFERENCES,

    FAVORITE_SITE_NAME_1(1),
    FAVORITE_SITE_NAME_2(2),
    FAVORITE_SITE_NAME_3(3),

    FAVORITE_SITE_URL_1(1),
    FAVORITE_SITE_URL_2(2),
    FAVORITE_SITE_URL_3(3),

    LOGIN,
    PASSWORD;

    private final int position;

    SampleFieldId() {
        this(-1);
    }


    SampleFieldId(int position) {
        this.position = position;
    }

    @Override
    public int position() {
        return position;
    }
}
