package io.doov.assertions;

import org.assertj.core.api.AbstractAssert;

import io.doov.core.dsl.lang.Result;

public class ResultAssert extends AbstractAssert<ResultAssert, Result> {

    ResultAssert(Result actual, Class<?> selfType) {
        super(actual, selfType);
    }

    public ResultAssert isTrue() {
        if (!actual.isTrue()) {
            failWithMessage("Expected result to be true");
        }
        return this;
    }

    public ResultAssert isFalse() {
        if (!actual.isFalse()) {
            failWithMessage("Expected result to be false");
        }
        return this;
    }

}
