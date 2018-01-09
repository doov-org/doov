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

import static io.doov.assertions.Assertions.assertThat;
import static io.doov.core.dsl.time.LocalDateSuppliers.today;
import static io.doov.core.dsl.time.TemporalAdjuster.firstDayOfMonth;
import static io.doov.core.dsl.time.TemporalAdjuster.firstDayOfNextYear;
import static io.doov.core.dsl.time.TemporalAdjuster.firstDayOfYear;
import static io.doov.core.dsl.time.TemporalAdjuster.ofDateAdjuster;
import static io.doov.sample.field.SampleFieldIdInfo.accountCreationDate;
import static io.doov.sample.field.SampleFieldIdInfo.configurationMinAge;
import static io.doov.sample.field.SampleFieldIdInfo.userBirthdate;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.YEARS;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.time.LocalDateSuppliers;
import io.doov.sample.model.*;

public class TemporalTest {

    private DslModel model;
    private Configuration configuration;
    private Account account;
    private User user;

    @BeforeEach
    public void before() {
        SampleModel sampleModel = new SampleModel();
        sampleModel.setUser(user = new User());
        sampleModel.setAccount(account = new Account());
        sampleModel.setConfiguration(configuration = new Configuration());
        model = new SampleModelWrapper(sampleModel);
    }

    @Test
    public void should_temporal_same_as_local_date_operation_eq() {
        user.setBirthDate(LocalDate.now());

        assertThat(userBirthdate().eq(LocalDate.now())).validates(model);
        assertThat(userBirthdate().notEq(LocalDate.now())).doesNotValidate(model);
    }

    @Test
    public void should_temporal_same_as_local_date_operation_with() {
        user.setBirthDate(LocalDate.now());

        assertThat(userBirthdate().with(firstDayOfMonth())
                        .eq(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())))
                        .validates(model);
        assertThat(userBirthdate().with(firstDayOfMonth())
                        .notEq(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())))
                        .doesNotValidate(model);
    }

    @Test
    public void should_temporal_same_as_local_date_operation_plus_and_minus() {
        LocalDate birthDate = LocalDate.now().minus(18, YEARS);
        user.setBirthDate(birthDate);
        configuration.setMinAge(18);

        assertThat(userBirthdate().eq(birthDate)).validates(model);
        assertThat(userBirthdate().minus(1, DAYS).eq(birthDate.minus(1, DAYS))).validates(model);
        assertThat(userBirthdate().plus(1, DAYS).eq(birthDate.plus(1, DAYS))).validates(model);
        assertThat(userBirthdate().minus(configurationMinAge(), YEARS).eq(birthDate.minus(18, YEARS)))
                        .validates(model);
        assertThat(userBirthdate().plus(configurationMinAge(), YEARS).eq(birthDate.plus(18, YEARS)))
                        .validates(model);
    }

    @Test
    public void should_temporal_same_as_local_date_operation_years_between() {
        LocalDate birthDate = LocalDate.now().minus(18, YEARS);
        user.setBirthDate(birthDate);
        account.setCreationDate(LocalDate.now());

        assertThat(userBirthdate().yearsBetween(LocalDate.now()).eq(18)).validates(model);
        assertThat(userBirthdate().plus(1, DAYS).yearsBetween(LocalDate.now()).eq(18)).doesNotValidate(model);
        assertThat(userBirthdate().plus(1, DAYS).minus(1, DAYS).yearsBetween(LocalDate.now()).eq(18)).validates(model);
        assertThat(userBirthdate().yearsBetween(accountCreationDate()).eq(18)).validates(model);

        user.setBirthDate(birthDate.plus(1, DAYS));
        assertThat(userBirthdate().yearsBetween(LocalDate.now()).eq(17)).validates(model);
        assertThat(userBirthdate().minus(1, DAYS).yearsBetween(LocalDate.now()).eq(18)).validates(model);
        assertThat(userBirthdate().yearsBetween(LocalDate.now().plus(1, DAYS)).eq(18)).validates(model);

        user.setBirthDate(LocalDate.of(2000, 1, 1));
        assertThat(userBirthdate().yearsBetween(LocalDate.of(2018, 1, 1)).eq(18)).validates(model);
        assertThat(userBirthdate().yearsBetween(LocalDate.of(2018, 1, 2)).eq(18)).validates(model);
        assertThat(userBirthdate().yearsBetween(LocalDate.of(2017, 12, 31)).eq(17)).validates(model);

        account.setCreationDate(LocalDate.of(2018, 1, 1));
        assertThat(userBirthdate().yearsBetween(accountCreationDate()).eq(18)).validates(model);
    }

    @Test
    public void should_temporal_same_as_local_date_operation_with_chained() {
        LocalDate birthDate = LocalDate.now().minus(18, YEARS);
        user.setBirthDate(birthDate);

        assertThat(userBirthdate().with(firstDayOfYear())
                        .yearsBetween(LocalDate.now().with(TemporalAdjusters.firstDayOfYear()))
                        .eq(18))
                        .validates(model);
        assertThat(userBirthdate().with(firstDayOfNextYear()).with(ofDateAdjuster(d -> d.withDayOfMonth(15)))
                        .yearsBetween(LocalDate.now().with(TemporalAdjusters.firstDayOfNextYear())
                                        .with(TemporalAdjusters.ofDateAdjuster(d -> d.withDayOfMonth(15))))
                        .eq(18)).validates(model);
    }

    @Test
    void should_temporal_date_suppliers_set_clock_not_validates() {
        user.setBirthDate(LocalDate.of(1980, 1, 1));

        LocalDateSuppliers.setClock(LocalDateSuppliers.createClockFrom(LocalDate.of(1990, 1, 1)));
        assertThat(userBirthdate().ageAt(today()).greaterOrEquals(18)).doesNotValidate(model);
        LocalDateSuppliers.setDefaultClock();
    }

    @Test
    void should_temporal_date_suppliers_set_clock_validate() {
        user.setBirthDate(LocalDate.of(1980, 1, 1));

        LocalDateSuppliers.setClock(LocalDateSuppliers.createClockFrom(LocalDate.of(2017, 1, 1)));
        assertThat(userBirthdate().ageAt(today()).greaterOrEquals(18)).validates(model);
        LocalDateSuppliers.setDefaultClock();
    }
}
