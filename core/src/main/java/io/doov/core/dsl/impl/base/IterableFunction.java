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
package io.doov.core.dsl.impl.base;

import static io.doov.core.dsl.meta.function.IterableFunctionMetadata.containsMetadata;
import static io.doov.core.dsl.meta.function.IterableFunctionMetadata.hasNotSizeMetadata;
import static io.doov.core.dsl.meta.function.IterableFunctionMetadata.hasSizeMetadata;
import static io.doov.core.dsl.meta.predicate.UnaryPredicateMetadata.isEmptyMetadata;
import static io.doov.core.dsl.meta.predicate.UnaryPredicateMetadata.isNotEmptyMetadata;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.StreamSupport.stream;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.impl.DefaultCondition;
import io.doov.core.dsl.impl.LeafStepCondition;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

public class IterableFunction<T, C extends Iterable<T>> extends DefaultCondition<C> {

    public IterableFunction(DslField<C> field) {
        super(field);
    }

    public IterableFunction(PredicateMetadata metadata, BiFunction<FieldModel, Context, Optional<C>> value) {
        super(metadata, value);
    }

    public StepCondition contains(T value) {
        return LeafStepCondition.stepCondition(containsMetadata(metadata, value), getFunction(),
                collection -> stream(collection.spliterator(), false).anyMatch(value::equals));
    }

    @SafeVarargs
    public final StepCondition containsAll(T... values) {
        return LeafStepCondition.stepCondition(containsMetadata(metadata, Arrays.asList(values)), getFunction(),
                iterable -> stream(iterable.spliterator(), false).collect(toSet()).containsAll(Arrays.asList(values)));
    }

    public StepCondition isEmpty() {
        return LeafStepCondition.stepCondition(isEmptyMetadata(metadata), getFunction(),
                iterable -> !iterable.iterator().hasNext());
    }

    public StepCondition isNotEmpty() {
        return LeafStepCondition.stepCondition(isNotEmptyMetadata(metadata), getFunction(),
                iterable -> iterable.iterator().hasNext());
    }

    public StepCondition hasSize(int size) {
        return LeafStepCondition.stepCondition(hasSizeMetadata(metadata, size), getFunction(),
                iterable -> stream(iterable.spliterator(), false).count() == size);
    }

    public StepCondition hasNotSize(int size) {
        return LeafStepCondition.stepCondition(hasNotSizeMetadata(metadata, size), getFunction(),
                iterable -> stream(iterable.spliterator(), false).count() != size);
    }

}
