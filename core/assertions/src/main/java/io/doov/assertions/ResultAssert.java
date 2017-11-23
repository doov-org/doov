package io.doov.assertions;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.assertj.core.api.AbstractAssert;

import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.lang.Result;

class ResultAssert extends AbstractAssert<ResultAssert, Result> {

    ResultAssert(Result actual, Class<?> selfType) {
        super(actual, selfType);
    }

    ResultAssert isTrue() {
        if (!actual.isTrue()) {
            failWithMessage("Expected result to be true (failed nodes: " + getFailedNodes() + ")");
        }
        return this;
    }

    ResultAssert isFalse() {
        if (!actual.isFalse()) {
            failWithMessage("Expected result to be false (failed nodes: " + getFailedNodes() + ")");
        }
        return this;
    }

    private List<String> getFailedNodes() {
        return actual.getFailedNodes().stream().map(Readable::readable).collect(toList());
    }

}
