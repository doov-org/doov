/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.model;

import static io.doov.sample.field.dsl.DslSampleModel.accountId;
import static io.doov.sample.field.dsl.DslSampleModel.favoriteSiteName1;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.FieldModel;
import io.doov.sample.field.dsl.DslSampleModel;

public class SampleModelNullInitTest {

    private SampleModel sample;
    private FieldModel source;

    @BeforeEach
    public void before() {
        sample = SampleModels.sample();
        source = new SampleModelWrapper(sample);
    }

    @Test
    void should_not_initialize_model_if_model_is_not_initialized_and_value_is_null() {
        // given
        sample.setAccount(null);
        // when
        source.set(accountId().id(), null);
        // then
        assertThat(sample.getAccount()).isNull();
    }

    @Test
    void should_not_initialize_list_if_model_is_not_initialized_and_value_is_null() {
        // given
        sample.getAccount().setTop3WebSite(null);
        // when
        source.set(favoriteSiteName1().id(), null);
        // then
        assertThat(sample.getAccount().getTop3WebSite()).isNull();
    }
}
