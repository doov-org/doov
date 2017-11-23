package io.doov.assertions;

import org.assertj.core.api.AbstractAssert;

import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.ValidationRule;

public class ValidationRuleAssert extends AbstractAssert<ValidationRuleAssert, ValidationRule> {

    ValidationRuleAssert(ValidationRule actual, Class<?> selfType) {
        super(actual, selfType);
    }

    public void validates(DslModel model) {
        Result result = actual.executeOn(model);
        ResultAssert resultAssert = new ResultAssert(result, ResultAssert.class);
        resultAssert.isTrue();
    }

    public void doesNotValidate(DslModel model) {
        Result result = actual.executeOn(model);
        ResultAssert resultAssert = new ResultAssert(result, ResultAssert.class);
        resultAssert.isFalse();
    }

    // TODO move to LesFurets
    public void excludes(DslModel model) {
        validates(model);
    }

    // TODO move to LesFurets
    public void doesNotExclude(DslModel model) {
        doesNotValidate(model);
    }

}
