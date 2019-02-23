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

import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.Metadata;

public class DefaultResult implements Result {
    private final boolean validated;
    private final Context context;

    protected DefaultResult(boolean validated, Context context) {
        this.validated = validated;
        this.context = context;
    }

    @Override
    public boolean value() {
        return validated;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public Metadata reduce(ReduceType type) {
        return context.getRootMetadata().reduce(context, type);
    }
}
