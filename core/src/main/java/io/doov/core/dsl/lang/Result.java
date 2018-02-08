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

import io.doov.core.dsl.DslModel;

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
     * Returns true if the predicate evaluates to false.
     *
     * @return true if false predicate
     */
    boolean isFalse();

    /**
     * Returns the failure cause of the failed predicate. This will reduce the syntax tree using execution values to
     * output the minimum failed tree.
     *
     * @return the failure cause, if failed
     */
    String getFailureCause();

    /**
     * Returns the context that contains the execution values.
     *
     * @return the context
     */
    Context getContext();

}
