/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package io.doov.sample.field;

import static io.doov.sample.field.SampleTag.ACCOUNT;
import static io.doov.sample.field.SampleTag.READ_ONLY;
import static io.doov.sample.field.SampleTag.USER;

import java.util.Arrays;
import java.util.List;

import io.doov.core.FieldId;
import io.doov.core.TagId;

public enum SampleFieldId implements FieldId {

    USER_ID(USER),
    ACCOUNT_ID(ACCOUNT),

    FIRST_NAME(USER),
    LAST_NAME(USER),
    FULLNAME(USER, READ_ONLY),
    BIRTHDATE(USER),

    EMAIL(ACCOUNT),
    LANGUAGE(ACCOUNT),
    TIMEZONE(ACCOUNT),
    COUNTRY(ACCOUNT),

    PHONE_NUMBER(ACCOUNT),
    EMAIL_ACCEPTED(ACCOUNT),
    EMAIL_MAX_SIZE(ACCOUNT),
    EMAILS_PREFERENCES(ACCOUNT),

    FAVORITE_SITE_NAME_1(1, ACCOUNT),
    FAVORITE_SITE_NAME_2(2, ACCOUNT),
    FAVORITE_SITE_NAME_3(3, ACCOUNT),

    FAVORITE_SITE_URL_1(1, ACCOUNT),
    FAVORITE_SITE_URL_2(2, ACCOUNT),
    FAVORITE_SITE_URL_3(3, ACCOUNT),

    LOGIN(ACCOUNT),
    PASSWD(ACCOUNT);

    private final int position;
    private final List<TagId> tags;

    SampleFieldId(TagId... tags) {
        this(-1, tags);
    }

    SampleFieldId(int position, TagId... tags) {
        this.position = position;
        this.tags = Arrays.asList(tags);
    }

    @Override
    public int position() {
        return position;
    }

    @Override
    public List<TagId> tags() {
        return tags;
    }
}
