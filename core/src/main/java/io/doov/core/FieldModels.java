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
package io.doov.core;

import java.util.EnumSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.*;
import java.util.stream.Collector;

/**
 * Utility methods for creating and manipulating {@code FieldModel}.
 */
public class FieldModels {

    /**
     * Returns a {@code Collector} that accumulates the input elements into a unique {@code FieldModel}.
     */
    public static <Fm extends FieldModel> Collector<Entry<FieldId, Object>, ?, Fm> toFieldModel(Fm model) {
        return new FieldModelCollector<>(model);
    }

    /**
     * Returns a concurrent {@code Collector} that accumulates the input elements into a unique {@code FieldModel}.
     */
    public static <Fm extends FieldModel> Collector<Entry<FieldId, Object>, ?, Fm> toConcurrentFieldModel(Fm model) {
        return new ConcurrentFieldModelCollector<>(model);
    }

    private static final class FieldModelCollector<Fm extends FieldModel>
            implements Collector<Entry<FieldId, Object>, Fm, Fm> {

        private final Fm model;

        FieldModelCollector(Fm model) {
            this.model = model;
        }

        @Override
        public Supplier<Fm> supplier() {
            return () -> model;
        }

        @Override
        public BiConsumer<Fm, Entry<FieldId, Object>> accumulator() {
            return (model, entry) -> {
                Object value = entry.getValue();
                if (value != null) {
                    model.set(entry.getKey(), value);
                }
            };
        }

        @Override
        public BinaryOperator<Fm> combiner() {
            return (m1, m2) -> m1;
        }

        @Override
        public Function<Fm, Fm> finisher() {
            return (m) -> model;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return EnumSet.allOf(Characteristics.class);
        }
    }

    private static final class ConcurrentFieldModelCollector<Fm extends FieldModel>
            implements Collector<Entry<FieldId, Object>, Fm, Fm> {

        private final Fm model;
        private final ReentrantLock lock;

        ConcurrentFieldModelCollector(Fm model) {
            this.model = model;
            this.lock = new ReentrantLock();
        }

        @Override
        public Supplier<Fm> supplier() {
            return () -> model;
        }

        @Override
        public BiConsumer<Fm, Entry<FieldId, Object>> accumulator() {
            return (supplier, entry) -> {
                Object value = entry.getValue();
                if (value == null) {
                    return;
                }
                lock.lock();
                try {
                    supplier.set(entry.getKey(), value);
                } finally {
                    lock.unlock();
                }
            };
        }

        @Override
        public BinaryOperator<Fm> combiner() {
            return (m1, m2) -> m1;
        }

        @Override
        public Function<Fm, Fm> finisher() {
            return (m) -> model;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return EnumSet.allOf(Characteristics.class);
        }
    }
}
