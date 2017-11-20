package io.doov.assertions;

import org.assertj.core.api.AbstractAssert;

import io.doov.core.dsl.lang.Result;

public class ResultAssert extends AbstractAssert<ResultAssert, Result> {

    ResultAssert(Result actual, Class<?> selfType) {
        super(actual, selfType);
    }

    public ResultAssert isValid() {
        if (!actual.isValid()) {
            failWithMessage("Expected result to be valid");
        }
        return this;
    }

    public ResultAssert isInvalid() {
        if (!actual.isInvalid()) {
            failWithMessage("Expected result to be invalid");
        }
        return this;
    }

}
