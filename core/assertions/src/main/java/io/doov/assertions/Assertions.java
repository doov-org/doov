package io.doov.assertions;

import io.doov.core.dsl.lang.Result;

public class Assertions {

    public static ResultAssert assertThat(Result actual) {
        return new ResultAssert(actual, ResultAssert.class);
    }

}
