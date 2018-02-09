package io.doov.sample.validation.impl;

import static io.doov.assertions.Assertions.assertThat;
import static io.doov.core.dsl.DOOV.min;
import static io.doov.core.dsl.DOOV.sum;
import static io.doov.sample.field.dsl.DSLSampleModel.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.DslModel;
import io.doov.sample.model.*;

public class NumericTest {

    private DslModel model;
    private User user;
    private Configuration configuration;

    @BeforeEach
    public void before() {
        SampleModel sampleModel = new SampleModel();
        sampleModel.setUser(user = new User());
        sampleModel.setConfiguration(configuration = new Configuration());
        model = new SampleModelWrapper(sampleModel);
    }

    @Test
    public void should_integer_greater_and_lesser_works_like_java() {
        assertThat(configurationMinAge().greaterOrEquals(0)).validates(model);

        configuration.setMinAge(18);
        assertThat(configurationMinAge().greaterOrEquals(18)).validates(model);
        assertThat(configurationMinAge().greaterThan(18)).doesNotValidate(model);
        assertThat(configurationMinAge().greaterThan(17)).validates(model);

        assertThat(configurationMinAge().lesserOrEquals(18)).validates(model);
        assertThat(configurationMinAge().lesserThan(18)).doesNotValidate(model);
        assertThat(configurationMinAge().lesserThan(19)).validates(model);
    }

    @Test
    public void should_double_greater_and_lesser_works_like_java() {
        assertThat(configurationMaxDouble().greaterOrEquals(0.00)).validates(model);

        configuration.setMaxDouble(18);
        assertThat(configurationMaxDouble().greaterOrEquals(18.00)).validates(model);
        assertThat(configurationMaxDouble().greaterOrEquals(18.01)).doesNotValidate(model);
        assertThat(configurationMaxDouble().greaterThan(18.00)).doesNotValidate(model);
        assertThat(configurationMaxDouble().greaterThan(17.00)).validates(model);

        assertThat(configurationMaxDouble().lesserOrEquals(18.00)).validates(model);
        assertThat(configurationMaxDouble().lesserOrEquals(17.99)).doesNotValidate(model);
        assertThat(configurationMaxDouble().lesserThan(18.00)).doesNotValidate(model);
        assertThat(configurationMaxDouble().lesserThan(19.00)).validates(model);
    }

    @Test
    public void should_float_greater_and_lesser_works_like_java() {
        assertThat(configurationMaxFloat().greaterOrEquals(0f)).validates(model);

        configuration.setMaxFloat(18);
        assertThat(configurationMaxFloat().greaterOrEquals(18f)).validates(model);
        assertThat(configurationMaxFloat().greaterThan(18f)).doesNotValidate(model);
        assertThat(configurationMaxFloat().greaterThan(17f)).validates(model);

        assertThat(configurationMaxFloat().lesserOrEquals(18f)).validates(model);
        assertThat(configurationMaxFloat().lesserThan(18f)).doesNotValidate(model);
        assertThat(configurationMaxFloat().lesserThan(19f)).validates(model);
    }

    @Test
    public void should_long_greater_and_lesser_works_like_java() {
        assertThat(configurationMaxLong().greaterOrEquals(0L)).validates(model);

        configuration.setMaxLong(18);
        assertThat(configurationMaxLong().greaterOrEquals(18L)).validates(model);
        assertThat(configurationMaxLong().greaterThan(18L)).doesNotValidate(model);
        assertThat(configurationMaxLong().greaterThan(17L)).validates(model);

        assertThat(configurationMaxLong().lesserOrEquals(18L)).validates(model);
        assertThat(configurationMaxLong().lesserThan(18L)).doesNotValidate(model);
        assertThat(configurationMaxLong().lesserThan(19L)).validates(model);
    }

    @Test
    public void should_between_works_like_java() {
        assertThat(configurationMinAge().between(0, 1)).validates(model);

        configuration.setMinAge(18);
        assertThat(configurationMinAge().between(18, 19)).validates(model);
        assertThat(configurationMinAge().between(18, 18)).doesNotValidate(model);
        assertThat(configurationMinAge().between(18, 17)).doesNotValidate(model);
        assertThat(configurationMinAge().between(17, 18)).doesNotValidate(model);
        assertThat(configurationMinAge().between(17, 19)).validates(model);
    }

    @Test
    public void should_times_works_like_java() {
        assertThat(configurationMinAge().times(0).eq(0)).validates(model);
        assertThat(configurationMinAge().times(5).eq(0)).validates(model);

        configuration.setMinAge(18);
        assertThat(configurationMinAge().times(-1).eq(-18)).validates(model);
        assertThat(configurationMinAge().times(0).eq(0)).validates(model);
        assertThat(configurationMinAge().times(1).eq(18)).validates(model);
        assertThat(configurationMinAge().times(5).eq(90)).validates(model);
    }

    @Test
    public void should_when_works_like_java() {
        assertThat(configurationMinAge().when(userFullName().isNull()).eq(0)).validates(model);

        configuration.setMinAge(18);
        user.setLastName("Toto");
        assertThat(configurationMinAge().when(userLastName().isNotNull()).eq(18)).validates(model);
        assertThat(configurationMinAge().when(userLastName().eq("Toto")).eq(18)).validates(model);
        assertThat(configurationMinAge().when(userLastName().eq("Titi")).eq(18)).doesNotValidate(model);
    }

    @Test
    public void should_sum_works_like_java() {
        assertThat(sum(configurationMinAge(), configurationMaxEmailSize()).eq(0)).validates(model);

        configuration.setMinAge(18);
        configuration.setMaxEmailSize(1000);
        assertThat(sum(configurationMinAge()).eq(18)).validates(model);
        assertThat(sum(configurationMinAge(), configurationMaxEmailSize()).eq(1018)).validates(model);

        assertThat(sum(configurationMinAge().times(1)).eq(18)).validates(model);
        assertThat(sum(configurationMinAge().times(1), configurationMaxEmailSize().times(1)).eq(1018)).validates(model);
        assertThat(sum(configurationMinAge().times(1), configurationMaxEmailSize().times(2)).eq(2018)).validates(model);
    }

    @Test
    public void should_min_works_like_java() {
        assertThat(min(configurationMinAge(), configurationMaxEmailSize()).eq(0)).validates(model);

        configuration.setMinAge(18);
        configuration.setMaxEmailSize(1000);
        assertThat(min(configurationMinAge()).eq(18)).validates(model);
        assertThat(min(configurationMinAge(), configurationMaxEmailSize()).eq(18)).validates(model);
    }

}
