package io.doov.assertions;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.assertj.core.api.AbstractAssert;

import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.meta.Metadata;

public class ResultAssert extends AbstractAssert<ResultAssert, Result> {

    ResultAssert(Result actual, Class<?> selfType) {
        super(actual, selfType);
    }

    public ResultAssert isTrue() {
        if (!actual.isTrue()) {
            failWithMessage("Expected result to be true (failed nodes: " + getFailedNodes() + ")");
        }
        return this;
    }

    public ResultAssert isFalse() {
        if (!actual.isFalse()) {
            failWithMessage("Expected result to be false (failed nodes: " + getFailedNodes() + ")");
        }
        return this;
    }

    public ResultAssert hasMessage(String message) {
        if (!actual.getMessage().equals(message)) {
            failWithMessage("Expected result to have message + " + message + " but was " + actual.getMessage());
        }
        return this;
    }

    public ResultAssert hasFailedNode(Metadata metadata) {
        if (!actual.getFailedNodes().contains(metadata)) {
            failWithMessage("Expected result to have failed nodes + " + metadata.readable()
                            + " but was " + getFailedNodes());
        }
        return this;
    }

    public ResultAssert hasFailedNodeEmpty() {
        if (!actual.getFailedNodes().isEmpty()) {
            failWithMessage("Expected result to have empty failed nodes but was " + getFailedNodes());
        }
        return this;
    }

    private List<String> getFailedNodes() {
        return actual.getFailedNodes().stream().map(Readable::readable).collect(toList());
    }

}
