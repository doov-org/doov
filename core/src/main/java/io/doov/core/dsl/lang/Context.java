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

public interface Context {

    Metadata getRootMetadata();

    boolean isShortCircuit();

    void addEvalFalse(Metadata metadata);

    void addEvalTrue(Metadata metadata);

    void addEvalValue(DslId id, Object value);

    Object getEvalValue(DslId id);

    public boolean isEvalTrue(Metadata metadata);

    public boolean isEvalFalse(Metadata metadata);

    public List<Metadata> getEvalTrue();

    public List<Metadata> getEvalFalse();
}
