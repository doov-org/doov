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

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.Metadata;

public class DefaultContext implements Context {

    private final List<Metadata> validated = new ArrayList<>();
    private final List<Metadata> invalidated = new ArrayList<>();
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
    public List<Metadata> getValidated() {
        return unmodifiableList(validated);
    }

    @Override
    public List<Metadata> getInvalidated() {
        return unmodifiableList(invalidated);
    }

    @Override
    public boolean isShortCircuit() {
        return shortCircuit;
    }

    @Override
    public void validated(Metadata metadata) {
        validated.add(metadata);
    }

    @Override
    public void invalidated(Metadata metadata) {
        invalidated.add(metadata);
    }
}
