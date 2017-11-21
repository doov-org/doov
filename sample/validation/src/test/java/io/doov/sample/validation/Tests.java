package io.doov.sample.validation;

import static io.doov.assertions.Assertions.assertThat;
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
    }

    @Test
    public void should_temporal_operations_iso_with_local_date_api() {
        // Basic equals on today
        user.setBirthDate(now());
        assertThat(exec(userBirthdate().eq(now()))).isTrue();
        assertThat(exec(userBirthdate().notEq(now()))).isFalse();
        assertThat(exec(userBirthdate().with(firstDayOfMonth()).eq(now().with(firstDayOfMonth())))).isTrue();
        assertThat(exec(userBirthdate().with(firstDayOfMonth()).notEq(now().with(firstDayOfMonth())))).isFalse();

        // More or less 18 years old
        user.setBirthDate(now().minus(18, YEARS));
        assertThat(exec(userBirthdate().ageAt(now()).eq(18))).isTrue();
        assertThat(exec(userBirthdate().plus(1, DAYS).yearsBetween(now()).eq(18))).isFalse();
        assertThat(exec(userBirthdate().plus(1, DAYS).minus(1, DAYS).yearsBetween(now()).eq(18))).isTrue();

        user.setBirthDate(now().minus(18, YEARS).plus(1, DAYS));
        assertThat(exec(userBirthdate().yearsBetween(now()).eq(17))).isTrue();
        assertThat(exec(userBirthdate().minus(1, DAYS).yearsBetween(now()).eq(18))).isTrue();
        assertThat(exec(userBirthdate().yearsBetween(now().plus(1, DAYS)).eq(18))).isTrue();

        user.setBirthDate(now().minus(18, YEARS));
        assertThat(exec(userBirthdate().with(firstDayOfYear())
                        .yearsBetween(now().with(firstDayOfYear())).eq(18))).isTrue();
        assertThat(exec(userBirthdate().with(firstDayOfNextYear()).with(ofDateAdjuster(d -> d.withDayOfMonth(15)))
                        .yearsBetween(now().with(firstDayOfNextYear()).with(ofDateAdjuster(d -> d.withDayOfMonth(15))))
                        .eq(18))).isTrue();

        // With reference date
        user.setBirthDate(LocalDate.of(2000, 1, 1));
        assertThat(exec(userBirthdate().yearsBetween(LocalDate.of(2018, 1, 1)).eq(18))).isTrue();
        assertThat(exec(userBirthdate().yearsBetween(LocalDate.of(2018, 1, 2)).eq(18))).isTrue();
        assertThat(exec(userBirthdate().yearsBetween(LocalDate.of(2017, 12, 31)).eq(17))).isTrue();
    }

    @Test
    public void should_before_after_operation_iso_all_parameters() {
        // TODO
    }

    private Result exec(StepCondition condition) {
        return DOOV.when(condition).validate().executeOn(wrapper);
    }

}
