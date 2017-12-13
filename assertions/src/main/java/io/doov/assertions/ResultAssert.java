/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    public ResultAssert hasMessageNull() {
        if (actual.getMessage() != null) {
            failWithMessage("Expected result to null message but was " + actual.getMessage());
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
