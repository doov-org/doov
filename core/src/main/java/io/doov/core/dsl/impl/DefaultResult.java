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
package io.doov.core.dsl.impl;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.meta.Metadata;

public class DefaultResult implements Result {

    private final boolean validity;
    private final String message;
    private final List<Metadata> metadatas = new ArrayList<>();

    protected DefaultResult(boolean validity, String message, List<Metadata> metadatas) {
        this.validity = validity;
        this.message = message;
        this.metadatas.addAll(metadatas);
    }

    @Override
    public boolean isTrue() {
        return validity;
    }

    @Override
    public boolean isFalse() {
        return !validity;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public List<Metadata> getFailedNodes() {
        return unmodifiableList(metadatas);
    }

}
