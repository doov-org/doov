package io.doov.sample.validation;

import static io.doov.sample.field.SampleFieldIdInfo.accountCreationDate;
import static io.doov.sample.field.SampleFieldIdInfo.configurationMinUserAge;
import static io.doov.sample.field.SampleFieldIdInfo.userBirthdate;
import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.YEARS;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.firstDayOfNextYear;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.ofDateAdjuster;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import io.doov.assertions.Assertions;
import io.doov.assertions.ResultAssert;
import io.doov.core.FieldModel;
import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.sample.model.*;

public class Tests {

    private FieldModel wrapper;
    private Account account;
    private User user;

    @Before
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
        assertThat(userBirthdate().eq(now())).isTrue();
        assertThat(userBirthdate().notEq(now())).isFalse();
    }

    @Test
    public void should_temporal_same_as_local_date_operation_with() {
        assertThat(userBirthdate().with(firstDayOfMonth()).eq(now().with(firstDayOfMonth()))).isTrue();
        assertThat(userBirthdate().with(firstDayOfMonth()).notEq(now().with(firstDayOfMonth()))).isFalse();
    }

    @Test
    public void should_temporal_same_as_local_date_operation_plus_and_minus() {
        LocalDate birthDate = now().minus(18, YEARS);
        user.setBirthDate(birthDate);

        assertThat(userBirthdate().eq(birthDate)).isTrue();
        assertThat(userBirthdate().minus(1, DAYS).eq(birthDate.minus(1, DAYS))).isTrue();
        assertThat(userBirthdate().plus(1, DAYS).eq(birthDate.plus(1, DAYS))).isTrue();
        assertThat(userBirthdate().minus(configurationMinUserAge(), YEARS).eq(birthDate.minus(18, YEARS))).isTrue();
        assertThat(userBirthdate().plus(configurationMinUserAge(), YEARS).eq(birthDate.plus(18, YEARS))).isTrue();
    }

    @Test
    public void should_temporal_same_as_local_date_operation_years_between() {
        LocalDate birthDate = now().minus(18, YEARS);
        user.setBirthDate(birthDate);

        assertThat(userBirthdate().yearsBetween(now()).eq(18)).isTrue();
        assertThat(userBirthdate().plus(1, DAYS).yearsBetween(now()).eq(18)).isFalse();
        assertThat(userBirthdate().plus(1, DAYS).minus(1, DAYS).yearsBetween(now()).eq(18)).isTrue();
        assertThat(userBirthdate().yearsBetween(accountCreationDate()).eq(18)).isTrue();

        user.setBirthDate(birthDate.plus(1, DAYS));
        assertThat(userBirthdate().yearsBetween(now()).eq(17)).isTrue();
        assertThat(userBirthdate().minus(1, DAYS).yearsBetween(now()).eq(18)).isTrue();
        assertThat(userBirthdate().yearsBetween(now().plus(1, DAYS)).eq(18)).isTrue();

        user.setBirthDate(LocalDate.of(2000, 1, 1));
        assertThat(userBirthdate().yearsBetween(LocalDate.of(2018, 1, 1)).eq(18)).isTrue();
        assertThat(userBirthdate().yearsBetween(LocalDate.of(2018, 1, 2)).eq(18)).isTrue();
        assertThat(userBirthdate().yearsBetween(LocalDate.of(2017, 12, 31)).eq(17)).isTrue();

        account.setCreationDate(LocalDate.of(2018, 1, 1));
        assertThat(userBirthdate().yearsBetween(accountCreationDate()).eq(18)).isTrue();
    }

    @Test
    public void should_temporal_same_as_local_date_operation_with_chained() {
        LocalDate birthDate = now().minus(18, YEARS);
        user.setBirthDate(birthDate);

        assertThat(userBirthdate().with(firstDayOfYear()).yearsBetween(now().with(firstDayOfYear())).eq(18)).isTrue();
        assertThat(userBirthdate().with(firstDayOfNextYear()).with(ofDateAdjuster(d -> d.withDayOfMonth(15)))
                        .yearsBetween(now().with(firstDayOfNextYear()).with(ofDateAdjuster(d -> d.withDayOfMonth(15))))
                        .eq(18)).isTrue();
    }

    private ResultAssert assertThat(StepCondition condition) {
        return Assertions.assertThat(exec(condition));
    }

    private Result exec(StepCondition condition) {
        return DOOV.when(condition).validate().executeOn(wrapper);
    }

}
