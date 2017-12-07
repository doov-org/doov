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
package io.doov.sample.validation.impl;

import static io.doov.core.dsl.time.TemporalAdjuster.firstDayOfMonth;
import static io.doov.core.dsl.time.TemporalAdjuster.firstDayOfNextYear;
import static io.doov.core.dsl.time.TemporalAdjuster.firstDayOfYear;
import static io.doov.core.dsl.time.TemporalAdjuster.ofDateAdjuster;
import static io.doov.sample.field.SampleFieldIdInfo.accountCreationDate;
import static io.doov.sample.field.SampleFieldIdInfo.configurationMinUserAge;
import static io.doov.sample.field.SampleFieldIdInfo.userBirthdate;
import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.YEARS;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.assertions.Assertions;
import io.doov.assertions.ValidationRuleAssert;
import io.doov.core.FieldModel;
import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.sample.model.*;

public class TemporalTest {

    private FieldModel wrapper;
    private Account account;
    private User user;

    @BeforeEach
    public void before() {
        SampleModel sample = SampleModels.sample();
        wrapper = new SampleModelWrapper(sample);
        account = sample.getAccount();
        user = sample.getUser();

        user.setBirthDate(now());
        account.setCreationDate(now());
    }

    @Test
    public void should_temporal_same_as_local_date_operation_eq() {
        assertThat(userBirthdate().eq(now())).validates(wrapper);
        assertThat(userBirthdate().notEq(now())).doesNotValidate(wrapper);
    }

    @Test
    public void should_temporal_same_as_local_date_operation_with() {
        assertThat(userBirthdate().with(firstDayOfMonth()).eq(now().with(TemporalAdjusters.firstDayOfMonth())))
                .validates(wrapper);
        assertThat(userBirthdate().with(firstDayOfMonth()).notEq(now().with(TemporalAdjusters.firstDayOfMonth())))
                .doesNotValidate(wrapper);
    }

    @Test
    public void should_temporal_same_as_local_date_operation_plus_and_minus() {
        LocalDate birthDate = now().minus(18, YEARS);
        user.setBirthDate(birthDate);

        assertThat(userBirthdate().eq(birthDate)).validates(wrapper);
        assertThat(userBirthdate().minus(1, DAYS).eq(birthDate.minus(1, DAYS))).validates(wrapper);
        assertThat(userBirthdate().plus(1, DAYS).eq(birthDate.plus(1, DAYS))).validates(wrapper);
        assertThat(userBirthdate().minus(configurationMinUserAge(), YEARS).eq(birthDate.minus(18, YEARS)))
                .validates(wrapper);
        assertThat(userBirthdate().plus(configurationMinUserAge(), YEARS).eq(birthDate.plus(18, YEARS)))
                .validates(wrapper);
    }

    @Test
    public void should_temporal_same_as_local_date_operation_years_between() {
        LocalDate birthDate = now().minus(18, YEARS);
        user.setBirthDate(birthDate);

        assertThat(userBirthdate().yearsBetween(now()).eq(18)).validates(wrapper);
        assertThat(userBirthdate().plus(1, DAYS).yearsBetween(now()).eq(18)).doesNotValidate(wrapper);
        assertThat(userBirthdate().plus(1, DAYS).minus(1, DAYS).yearsBetween(now()).eq(18)).validates(wrapper);
        assertThat(userBirthdate().yearsBetween(accountCreationDate()).eq(18)).validates(wrapper);

        user.setBirthDate(birthDate.plus(1, DAYS));
        assertThat(userBirthdate().yearsBetween(now()).eq(17)).validates(wrapper);
        assertThat(userBirthdate().minus(1, DAYS).yearsBetween(now()).eq(18)).validates(wrapper);
        assertThat(userBirthdate().yearsBetween(now().plus(1, DAYS)).eq(18)).validates(wrapper);

        user.setBirthDate(LocalDate.of(2000, 1, 1));
        assertThat(userBirthdate().yearsBetween(LocalDate.of(2018, 1, 1)).eq(18)).validates(wrapper);
        assertThat(userBirthdate().yearsBetween(LocalDate.of(2018, 1, 2)).eq(18)).validates(wrapper);
        assertThat(userBirthdate().yearsBetween(LocalDate.of(2017, 12, 31)).eq(17)).validates(wrapper);

        account.setCreationDate(LocalDate.of(2018, 1, 1));
        assertThat(userBirthdate().yearsBetween(accountCreationDate()).eq(18)).validates(wrapper);
    }

    @Test
    public void should_temporal_same_as_local_date_operation_with_chained() {
        LocalDate birthDate = now().minus(18, YEARS);
        user.setBirthDate(birthDate);

        assertThat(userBirthdate().with(firstDayOfYear())
                .yearsBetween(now().with(TemporalAdjusters.firstDayOfYear()))
                .eq(18))
                .validates(wrapper);
        assertThat(userBirthdate().with(firstDayOfNextYear()).with(ofDateAdjuster(d -> d.withDayOfMonth(15)))
                .yearsBetween(now().with(TemporalAdjusters.firstDayOfNextYear())
                        .with(TemporalAdjusters.ofDateAdjuster(d -> d.withDayOfMonth(15))))
                .eq(18)).validates(wrapper);
    }

    private ValidationRuleAssert assertThat(StepCondition condition) {
        return Assertions.assertThat(DOOV.when(condition).validate());
    }

}
