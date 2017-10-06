/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.modelmap.sample.field.SampleFieldIdInfo.ACCOUNT_ID;
import static org.modelmap.sample.field.SampleFieldIdInfo.BIRTHDATE;
import static org.modelmap.sample.field.SampleFieldIdInfo.TIMEZONE;

import java.time.LocalDate;

import org.junit.Test;
import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.DSL;
import org.modelmap.core.dsl.lang.StepWhen;
import org.modelmap.sample.model.SampleModels;
import org.modelmap.sample.model.Timezone;

public class DSLSandboxTest {

    private FieldModel model = SampleModels.wrapper();

    @Test
    public void sample1() {
        StepWhen step = DSL.when(ACCOUNT_ID.eq(1L))
                        .withMessage("incorrect account id");
        System.out.println(step.readable());
        assertThat(step.executeOn(model)).isEmpty();
    }

    @Test
    public void sample2() {
        StepWhen step = DSL.when(ACCOUNT_ID.eq(1L).not())
                        .withMessage("incorrect account id");
        System.out.println(step.readable());
        assertThat(step.executeOn(model)).isNotEmpty();
    }

    @Test
    public void sample3() {
        StepWhen step = DSL.when(BIRTHDATE.eq(LocalDate.of(1980, 8, 1)))
                        .withMessage("you can't be born the August 1, 1980");
        System.out.println(step.readable());
        assertThat(step.executeOn(model)).isNotEmpty();
    }

    @Test
    public void sample4() {
        StepWhen step = DSL.when(BIRTHDATE.between(LocalDate.of(1980, 1, 1), LocalDate.of(1980, 12, 31)))
                        .withMessage("you can't be born in 1980");
        System.out.println(step.readable());
        assertThat(step.executeOn(model)).isNotEmpty();
    }

    @Test
    public void sample5() {
        StepWhen step = DSL.when(BIRTHDATE.between(LocalDate.of(1980, 1, 1), LocalDate.of(1980, 12, 31))
                        .and(ACCOUNT_ID.eq(9L).not())
                        .or(TIMEZONE.eq(Timezone.ETC_GMT)))
                        .withMessage("you can't be born in 1980 and have an ID different of 9 or having timezone " +
                                        "equals to ETC_GMT");
        System.out.println(step.readable());
        assertThat(step.executeOn(model)).isNotEmpty();
    }

}
