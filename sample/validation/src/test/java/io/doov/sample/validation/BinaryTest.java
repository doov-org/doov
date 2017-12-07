package io.doov.sample.validation;

import static io.doov.assertions.Assertions.assertThat;
import static io.doov.sample.field.SampleFieldIdInfo.userFirstName;
import static io.doov.sample.field.SampleFieldIdInfo.userLastName;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.sample.model.*;

public class BinaryTest {

    private SampleModelWrapper model;

    @BeforeEach
    public void before() {
        User user = new User();
        user.setFirstName("first name");
        user.setLastName("last name");

        SampleModel sampleModel = new SampleModel();
        sampleModel.setUser(user);

        model = new SampleModelWrapper(sampleModel);
    }

    @Test
    public void should_short_circuit_enabled_by_default() {
        ValidationRule rule;
        StepCondition node;

        rule = DOOV.when(userFirstName().isNotNull().or(userLastName().isNull())).validate();
        assertThat(rule).validates(model).hasFailedNodeEmpty();

        rule = DOOV.when(userFirstName().isNotNull().or(node = userLastName().isNull())).validate();
        rule = rule.withShortCircuit(false);
        assertThat(rule).validates(model).hasFailedNode(node.getMetadata());
    }

}
