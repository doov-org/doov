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

import java.util.List;

import io.doov.core.dsl.DslId;
import io.doov.core.dsl.meta.Metadata;

/**
 * Interface for the execution context.
 * <p>
 * You have for example node values (see {@link #getEvalValue(DslId)}), failed nodes (see {@link #getEvalFalse()}), etc.
 */
public interface Context {

    /**
     * Returns the root node of the syntax tree.
     *
     * @return the metadata
     */
    Metadata getRootMetadata();

    /**
     * Returns true if the evaluation short-circuit is activated, which will evaluate logical expression like java.
     * Activated by default.
     *
     * @return true if activated
     */
    boolean isShortCircuit();

    /**
     * Adds the given node that evaluates to false.
     *
     * @param metadata the metadata
     */
    void addEvalFalse(Metadata metadata);

    /**
     * Adds the given node that evaluates to true.
     *
     * @param metadata the metadata
     */
    void addEvalTrue(Metadata metadata);

    /**
     * Adds the given evaluation value for the given field id.
     *
     * @param id    the id
     * @param value the value
     */
    void addEvalValue(DslId id, Object value);

    /**
     * Return the evaluation value for this field id.
     *
     * @param id the id
     * @return the value
     */
    Object getEvalValue(DslId id);

    /**
     * Returns true if the given node evaluation is true.
     *
     * @param metadata the metadata
     * @return true if eval is true
     */
    boolean isEvalTrue(Metadata metadata);

    /**
     * Returns true if the given node evaluation is false.
     *
     * @param metadata the metadata
     * @return true if eval is false
     */
    boolean isEvalFalse(Metadata metadata);

    /**
     * Returns the list of nodes that evaluates to true.
     *
     * @return the eval list
     */
    List<Metadata> getEvalTrue();

    /**
     * Returns the list of nodes that evaluates to false.
     *
     * @return the eval list
     */
    List<Metadata> getEvalFalse();

}
