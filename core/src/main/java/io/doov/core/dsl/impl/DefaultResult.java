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

import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.Result;

public class DefaultResult implements Result {
    private final boolean validity;
    private final String message;
    private final DefaultContext context;

    protected DefaultResult(boolean validity, String message, DefaultContext context) {
        this.validity = validity;
        this.message = message;
        this.context = context;
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
    public Context getContext() {
        return context;
    }

    @Override
    public String getFailureCause() {
        return context.getRootMetadata().message(context).readable();
    }
}
