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
package io.doov.core.dsl.meta.predicate;

import java.util.concurrent.atomic.AtomicInteger;

import io.doov.core.dsl.meta.Metadata;

public interface PredicateMetadata extends Metadata {

    default AtomicInteger evalTrue() {
        throw new UnsupportedOperationException(getClass().getName());
    }

    default AtomicInteger evalFalse() {
        throw new UnsupportedOperationException(getClass().getName());
    }
 
    default int incTrueEval() {
        return evalTrue().incrementAndGet();
    }

    default int incFalseEval() {
        return evalFalse().incrementAndGet();
    }

    default int trueEvalCount() {
        return evalTrue().get();
    }

    default int falseEvalCount() {
        return evalFalse().get();
    }
    
    default void resetCounters() {
        evalTrue().set(0);
        evalFalse().set(0);
    }
}
