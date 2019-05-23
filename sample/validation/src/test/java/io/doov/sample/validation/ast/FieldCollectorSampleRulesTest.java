/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation.ast;

import static io.doov.assertions.Assertions.assertThat;
import static io.doov.sample.field.SampleFieldId.*;
import static io.doov.sample.validation.SampleRules.*;

import org.junit.jupiter.api.Test;

public class FieldCollectorSampleRulesTest {

    @Test
    void RULE_EMAIL() {
        assertThat(RULE_EMAIL).isUsing(EMAIL);
        assertThat(RULE_EMAIL).isNotUsing(ACCOUNT_ID);
    }

    @Test
    void RULE_ACCOUNT() {
        assertThat(RULE_ACCOUNT).isUsing(BIRTHDATE, EMAIL, CONFIGURATION_EMAIL_MAX_SIZE, COUNTRY, PHONE_NUMBER);
        assertThat(RULE_ACCOUNT).isNotUsing(ACCOUNT_ID);
    }

    @Test
    void RULE_ACCOUNT_2() {
        assertThat(RULE_ACCOUNT_2).isUsing(BIRTHDATE, EMAIL, CONFIGURATION_EMAIL_MAX_SIZE, COUNTRY, PHONE_NUMBER);
        assertThat(RULE_ACCOUNT_2).isNotUsing(ACCOUNT_ID);
    }

    @Test
    void RULE_USER() {
        assertThat(RULE_USER).isUsing(FIRST_NAME, LAST_NAME);
        assertThat(RULE_USER).isNotUsing(ACCOUNT_ID);
    }

    @Test
    void RULE_USER_2() {
        assertThat(RULE_USER_2).isUsing(LAST_NAME, PHONE_NUMBER, EMAIL);
        assertThat(RULE_USER_2).isNotUsing(ACCOUNT_ID);
    }

    @Test
    void RULE_USER_ADULT() {
        assertThat(RULE_USER_ADULT).isUsing(BIRTHDATE, CREATION_DATE);
        assertThat(RULE_USER_ADULT).isNotUsing(ACCOUNT_ID);
    }

    @Test
    void RULE_USER_ADULT_FIRSTDAY() {
        assertThat(RULE_USER_ADULT_FIRSTDAY).isUsing(BIRTHDATE, CREATION_DATE);
        assertThat(RULE_USER_ADULT_FIRSTDAY).isNotUsing(ACCOUNT_ID);
    }

    @Test
    void RULE_FIRST_NAME() {
        assertThat(RULE_FIRST_NAME).isUsing(FIRST_NAME);
        assertThat(RULE_FIRST_NAME).isNotUsing(ACCOUNT_ID);
    }

    @Test
    void RULE_ID() {
        assertThat(RULE_ID).isUsing(USER_ID);
        assertThat(RULE_ID).isNotUsing(ACCOUNT_ID);
    }

    @Test
    void RULE_AGE() {
        assertThat(RULE_AGE).isUsing(BIRTHDATE);
        assertThat(RULE_AGE).isNotUsing(ACCOUNT_ID);
    }

    @Test
    void RULE_AGE_2() {
        assertThat(RULE_AGE_2).isUsing(BIRTHDATE);
        assertThat(RULE_AGE_2).isNotUsing(ACCOUNT_ID);
    }

    @Test
    void RULE_SUM() {
        assertThat(RULE_SUM).isUsing(CONFIGURATION_MIN_AGE, CONFIGURATION_EMAIL_MAX_SIZE);
        assertThat(RULE_SUM).isNotUsing(ACCOUNT_ID);
    }

    @Test
    void RULE_MIN() {
        assertThat(RULE_MIN).isUsing(CONFIGURATION_MIN_AGE, CONFIGURATION_EMAIL_MAX_SIZE);
        assertThat(RULE_MIN).isNotUsing(ACCOUNT_ID);
    }

    @Test
    void RULE_DOUBLE_LAMBDA() {
        assertThat(RULE_DOUBLE_LAMBDA).isUsing(FAVORITE_SITE_NAME_1);
        assertThat(RULE_DOUBLE_LAMBDA).isNotUsing(ACCOUNT_ID);
    }

    @Test
    void RULE_BORN_1980() {
        assertThat(RULE_BORN_1980).isUsing(BIRTHDATE);
        assertThat(RULE_BORN_1980).isNotUsing(ACCOUNT_ID);
    }

    @Test
    void RULE_ACCOUNT_TIME_CONTAINS() {
        assertThat(RULE_ACCOUNT_TIME_CONTAINS).isUsing(TIMEZONE);
        assertThat(RULE_ACCOUNT_TIME_CONTAINS).isNotUsing(ACCOUNT_ID);
    }

    @Test
    void RULE_COMPANY_NOT_LESFURETS() {
        assertThat(RULE_COMPANY_NOT_BLABLA).isUsing(COMPANY);
        assertThat(RULE_COMPANY_NOT_BLABLA).isNotUsing(ACCOUNT_ID);
    }
}
