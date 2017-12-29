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
package io.doov.core.dsl.impl;

import java.util.*;

import io.doov.core.dsl.DslId;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.Metadata;

public class DefaultContext implements Context {

    private final List<Metadata> evalTrue = new ArrayList<>();
    private final List<Metadata> evalFalse = new ArrayList<>();
    private final Map<DslId, Object> values = new HashMap<>();
    private final boolean shortCircuit;
    private Metadata rootMetadata;

    public DefaultContext(Metadata rootMetadata) {
        this(true, rootMetadata);
    }

    public DefaultContext(boolean shortCircuit, Metadata rootMetadata) {
        this.shortCircuit = shortCircuit;
        this.rootMetadata = rootMetadata;
    }

    @Override
    public Metadata getRootMetadata() {
        return rootMetadata;
    }

    @Override
    public boolean isEvalTrue(Metadata metadata) {
        return evalTrue.contains(metadata);
    }

    @Override
    public boolean isEvalFalse(Metadata metadata) {
        return evalFalse.contains(metadata);
    }

    @Override
    public boolean isShortCircuit() {
        return shortCircuit;
    }

    @Override
    public void addEvalTrue(Metadata metadata) {
        evalTrue.add(metadata);
    }

    @Override
    public void addEvalFalse(Metadata metadata) {
        evalFalse.add(metadata);
    }

    @Override
    public void addEvalValue(DslId id, Object value) {
        values.put(id, value);
    }
    
    @Override
    public Object getEvalValue(DslId id) {
        return values.get(id);
    }
    
    @Override
    public List<Metadata> getEvalTrue() {
        return Collections.unmodifiableList(evalTrue);
    }

    @Override
    public List<Metadata> getEvalFalse() {
        return Collections.unmodifiableList(evalFalse);
    }
}
