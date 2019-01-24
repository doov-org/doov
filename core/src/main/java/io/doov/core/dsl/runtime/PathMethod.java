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
package io.doov.core.dsl.runtime;

/**
 * Building block for the runtime implementation.
 * PathMethod implements actions on a field : get/set/create
 *
 * @param <T> path container type
 * @param <R> field return type
 */
public interface PathMethod<T, R> {

    /**
     * Get value
     *
     * @param link path container
     * @return field value
     */
    R get(T link);

    /**
     * Set value
     *
     * @param link  path container
     * @param value value to set
     */
    void set(T link, R value);

    /**
     * Create, set in the path container and return the value
     *
     * @param link path container
     * @return created value
     */
    R create(T link);

}
