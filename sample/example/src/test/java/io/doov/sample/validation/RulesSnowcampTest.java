package io.doov.sample.validation;

import org.junit.jupiter.api.Test;

import io.doov.assertions.Assertions;
import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.Result;
import io.doov.sample.model.SampleModels;

public class RulesSnowcampTest {

    @Test
    public void testAccountRule() {
        FieldModel wrapper = SampleModels.wrapper();
        Result result = RulesSnowcamp.rule.executeOn(wrapper);
        Assertions.assertThat(result).isTrue();
    }

}