/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation.ast;

import static io.doov.assertions.Assertions.assertThat;
import static io.doov.sample.field.dsl.DslSampleModel.favoriteSiteName1;
import static io.doov.sample.field.dsl.DslSampleModel.favoriteSiteUrl1;
import static io.doov.sample.validation.EmployeeMapping.*;
import static io.doov.sample.validation.SampleMappings.*;

import org.junit.jupiter.api.Test;

import io.doov.sample.field.SampleFieldId;
import io.doov.sample3.field.EmployeeFieldId;

public class FieldCollectorEmployeeMappingTest {

    @Test
    void FULLNAME_MAPPING() {
        assertThat(FULLNAME_MAPPING).isUsing(
                        SampleFieldId.FIRST_NAME,
                        SampleFieldId.LAST_NAME,
                        EmployeeFieldId.FULLNAME);
    }

    @Test
    void EMAIL_MAPPING() {
        assertThat(EMAIL_MAPPING).isUsing(
                        SampleFieldId.EMAIL_ACCEPTED,
                        SampleFieldId.EMAIL,
                        EmployeeFieldId.EMAIL);
    }

    @Test
    void AGE_MAPPING() {
        assertThat(AGE_MAPPING).isUsing(
                        SampleFieldId.BIRTHDATE,
                        EmployeeFieldId.AGE);
    }

    @Test
    void COUNTRY_MAPPING() {
        assertThat(COUNTRY_MAPPING).isUsing(
                        SampleFieldId.COUNTRY,
                        EmployeeFieldId.COUNTRY);
    }

    @Test
    void COMPANY_MAPPING() {
        assertThat(COMPANY_MAPPING).isUsing(
                        SampleFieldId.COMPANY,
                        EmployeeFieldId.COMPANY);
    }

    @Test
    void ALL_MAPPINGS() {
        assertThat(ALL_MAPPINGS).isUsing(
                        SampleFieldId.FIRST_NAME,
                        SampleFieldId.LAST_NAME,
                        SampleFieldId.EMAIL_ACCEPTED,
                        SampleFieldId.BIRTHDATE,
                        SampleFieldId.COUNTRY,
                        SampleFieldId.COMPANY);
    }

    @Test
    void DEFAULT_CONFIG_AGE() {
        assertThat(DEFAULT_CONFIG_AGE).isUsing(
                        SampleFieldId.CONFIGURATION_MIN_AGE);
    }

    @Test
    void DEFAULT_CONFIG_CAMPAIGN() {
        assertThat(DEFAULT_CONFIG_CAMPAIGN).isUsing(
                        SampleFieldId.CONFIGURATION_MAILING_CAMPAIGN);
    }

    @Test
    void DEFAULT_CREATION_DATE() {
        assertThat(DEFAULT_CREATION_DATE).isUsing(
                        SampleFieldId.CREATION_DATE);
    }

    @Test
    void AGE_AT() {
        assertThat(AGE_AT).isUsing(SampleFieldId.BIRTHDATE,
                        SampleFieldId.CONFIGURATION_MIN_AGE);
    }

    @Test
    void DEFAULT_DATE_TO_STRING() {
        assertThat(DEFAULT_DATE_TO_STRING).isUsing(
                        SampleFieldId.LOGIN);
    }

    @Test
    void DEFAULT_NAME_COMBINER() {
        assertThat(DEFAULT_NAME_COMBINER).isUsing(
                        SampleFieldId.FIRST_NAME,
                        SampleFieldId.LAST_NAME);
    }

    @Test
    void DEFAULT_CONDITIONAL() {
        assertThat(DEFAULT_CONDITIONAL).isUsing(
                        SampleFieldId.CONFIGURATION_MAILING_CAMPAIGN);
    }

    @Test
    void TEMPLATE_MAP_GOOGLE() {
        assertThat(TEMPLATE_MAP_GOOGLE.bind(favoriteSiteName1, favoriteSiteUrl1))
                        .isUsing(SampleFieldId.FAVORITE_SITE_NAME_1,
                                        SampleFieldId.FAVORITE_SITE_URL_1);
    }

}
