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
package io.doov.core.dsl.lang;

import java.util.Locale;

import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.meta.Metadata;

/**
 * Interface for the execution result after a call to {@link ValidationRule#executeOn(DslModel)}.
 */
public interface Result {

    /**
     * Returns true if the predicate evaluates to true.
     *
     * @return true if true predicate
     */
    boolean isTrue();

    /**
     * Returns the predicate reduction. This will reduce the syntax tree using execution values to output the minimum tree.
     * 
     * @param type the type of reduction
     * @return the reduce metadata
     */
    Metadata reduce(ReduceType type);

    /**
     * Returns the failure cause of the failed predicate. This will reduce the syntax tree using execution values to output
     * the minimum failed tree.
     *
     * @param locale the desire locale for the failure cause
     * @return the failure cause, if failed
     */
    default String getFailureCause(Locale locale) {
        if (isTrue())
            return null;
        return reduceMessage(locale, ReduceType.FAILURE);
    }

    /**
     * Returns the failure cause of the failed predicate. This will reduce the syntax tree using execution values to output
     * the minimum failed tree.
     *
     * @return the failure cause, if failed
     */
    default String getFailureCause() {
        return getFailureCause(Locale.getDefault());
    }

    /**
     * Returns the predicate reduction. This will reduce the syntax tree using execution values to output the minimum tree.
     * @param locale the desire locale for the predicate reduction
     * @param type the type of reduction
     * @return the reduce
     */
    default String reduceMessage(Locale locale, ReduceType type) {
        final Metadata metadata = reduce(type);
        if (metadata == null)
            return null;
        return metadata.readable(locale).trim();
    }

    /**
     * Returns the predicate reduction. This will reduce the syntax tree using execution values to output the minimum tree.
     * @param type the type of reduction
     * @return the reduce
     */
    default String reduceMessage(ReduceType type) {
        return reduceMessage(Locale.getDefault(), type);
    }

    /**
     * Returns the context that contains the execution values.
     *
     * @return the context
     */
    Context getContext();

}
