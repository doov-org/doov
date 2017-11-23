package io.doov.assertions;

import io.doov.core.dsl.lang.ValidationRule;

public class Assertions {

    public static ValidationRuleAssert assertThat(ValidationRule actual) {
        return new ValidationRuleAssert(actual, ValidationRuleAssert.class);
    }

}
