/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package io.doov.assertions;

import static java.util.stream.Collectors.toList;

import java.util.*;

import org.assertj.core.api.AbstractAssert;

import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.meta.Metadata;

/**
 * Assertion for {@link Result}.
 */
public class ResultAssert extends AbstractAssert<ResultAssert, Result> {

    ResultAssert(Result result, Class<?> selfType) {
        super(result, selfType);
    }

    /**
     * Verifies that the result is true (meaning the evaluated predicate is true).
     *
     * @return self
     */
    public ResultAssert isTrue() {
        if (!actual.isTrue()) {
            failWithMessage("Expected result to be true (invalidated nodes: " + getInvalidatedMetadata() + ")");
        }
        return this;
    }

    /**
     * Verifies that the result is false (meaning the evaluated predicate is false).
     *
     * @return self
     */
    public ResultAssert isFalse() {
        if (!actual.isFalse()) {
            failWithMessage("Expected result to be false (invalidated nodes: " + getInvalidatedMetadata() + ")");
        }
        return this;
    }

    /**
     * Verifies that the result message is equal to the given one.
     * <b>Beware : </b>only use this version if you do not use l10n feature
     * otherwise your tests will be locale dependant
     * <br>
     * If you use l10n, please use #hasFailureCause(String message, Locale locale)
     *
     * @param message the message
     * @return self
     */
    public ResultAssert hasFailureCause(String message) {
        return hasFailureCause(message, Locale.getDefault());
    }

    /**
     * Verifies that the result message is equal to the given one with
     * Locale control to make tests independent from system locale
     *
     * @param message the message
     * @param locale locale
     * @return self
     */
    public ResultAssert hasFailureCause(String message, Locale locale) {
        if (!Objects.equals(actual.getFailureCause(locale), message)) {
            failWithMessage("Expected result to have message '" + message
                    + "' but was '" + actual.getFailureCause(locale) + "'");
        }
        return this;
    }
    /**
     * Verifies that the result message is null.
     *
     * @return self
     */
    public ResultAssert hasNoFailureCause() {
        if (actual.getFailureCause() != null) {
            failWithMessage("Expected result to null message but was " + actual.getFailureCause());
        }
        return this;
    }

    /**
     * Verifies that the result has a false node equal to the given one.
     *
     * @param metadata the metadata
     * @return self
     */
    public ResultAssert isEvalFalse(Metadata metadata) {
        if (!actual.getContext().isEvalFalse(metadata)) {
            failWithMessage("Expected result to have invalidated nodes + " + metadata.readable()
                    + " but was " + getInvalidatedMetadata());
        }
        return this;
    }

    /**
     * Verifies that the result has a true node equal to the given one.
     *
     * @param metadata the metadata
     * @return self
     */
    public ResultAssert isEvalTrue(Metadata metadata) {
        if (!actual.getContext().isEvalTrue(metadata)) {
            failWithMessage("Expected result to have validated nodes + " + metadata.readable()
                    + " but was " + getValidatedMetadata());
        }
        return this;
    }

    /**
     * Verifies that the result has no invalidated nodes (meaning the evaluated tree has no false nodes).
     *
     * @return self
     */
    public ResultAssert hasNoInvalidatedMetadata() {
        if (!actual.getContext().getEvalFalse().isEmpty()) {
            failWithMessage("Expected result to have empty invalidated nodes but was " + getInvalidatedMetadata());
        }
        return this;
    }

    private List<String> getInvalidatedMetadata() {
        return actual.getContext().getEvalFalse().stream().map(Readable::readable).collect(toList());
    }

    private List<String> getValidatedMetadata() {
        return actual.getContext().getEvalTrue().stream().map(Readable::readable).collect(toList());
    }

}
