/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.example;

import static org.modelmap.sample.field.SampleFieldIdInfo.ACCOUNT_ID;
import static org.modelmap.sample.field.SampleFieldIdInfo.BIRTHDATE;
import static org.modelmap.sample.field.SampleFieldIdInfo.TIMEZONE;

import java.time.LocalDate;

import org.junit.Test;
import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.DSL;
import org.modelmap.sample.model.SampleModels;
import org.modelmap.sample.model.Timezone;

public class DSLSandboxTest {

    @Test
    public void sample1() {
        FieldModel model = SampleModels.wrapper();
        DSL.when(ACCOUNT_ID.eq(1l)) //
                        .throwMessage("incorrect account id") //
                        .executeOn(model);
    }

    @Test(expected = RuntimeException.class)
    public void sample2() {
        FieldModel model = SampleModels.wrapper();
        DSL.when(ACCOUNT_ID.eq(1l).not()) //
                        .throwMessage("incorrect account id") //
                        .executeOn(model);
    }

    @Test(expected = RuntimeException.class)
    public void sample3() {
        FieldModel model = SampleModels.wrapper();
        DSL.when(BIRTHDATE.eq(LocalDate.of(1980, 8, 1))) //
                        .throwMessage("you can't be born the August 1, 1980")
                        .executeOn(model);
    }

    @Test(expected = RuntimeException.class)
    public void sample4() {
        FieldModel model = SampleModels.wrapper();
        DSL.when(BIRTHDATE.between(LocalDate.of(1980, 1, 1), LocalDate.of(1980, 12, 31))) //
                        .throwMessage("you can't be born in 1980")
                        .executeOn(model);
    }

    @Test(expected = RuntimeException.class)
    public void sample5() {
        FieldModel model = SampleModels.wrapper();
        DSL.when(BIRTHDATE.between(LocalDate.of(1980, 1, 1), LocalDate.of(1980, 12, 31)) //
                        .and(ACCOUNT_ID.eq(9l).not())
                        .or(TIMEZONE.eq(Timezone.ETC_GMT))) //
                        .throwMessage("you can't be born in 1980 and have an ID different of 9 or having timezone " +
                                        "equals to ETC_GMT")
                        .executeOn(model);
    }

}
