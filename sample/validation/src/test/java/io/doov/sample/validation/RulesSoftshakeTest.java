package io.doov.sample.validation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.Result;
import io.doov.sample.model.SampleModels;

public class RulesSoftshakeTest {

    @Test
    public void test() {
        // Given
        FieldModel model = SampleModels.wrapper();

        // When
        Result result = RulesSoftshake.EXAMPLE.executeOn(model);

        // Then
        assertThat(result.isValid()).isTrue();
    }

}