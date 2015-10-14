package org.modelmap.core;

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
    public static Collector<Entry<FieldId, Object>, ?, FieldModel> toFieldModel(FieldModel model) {
        return new FieldModelCollector(model);
    }

    /**
     * Returns a concurrent {@code Collector} that accumulates the input elements into a unique {@code FieldModel}.
     */
    public static Collector<Entry<FieldId, Object>, ?, FieldModel> toConcurrentFieldModel(FieldModel model) {
        return new ConcurrentFieldModelCollector(model);
    }

    private static final class FieldModelCollector
                    implements Collector<Entry<FieldId, Object>, FieldModel, FieldModel> {

        private final FieldModel model;

        FieldModelCollector(FieldModel model) {
            this.model = model;
        }

        @Override
        public Supplier<FieldModel> supplier() {
            return () -> model;
        }

        @Override
        public BiConsumer<FieldModel, Entry<FieldId, Object>> accumulator() {
            return (model, entry) -> {
                Object value = entry.getValue();
                if (value != null) {
                    model.set(entry.getKey(), value);
                }
            };
        }

        @Override
        public BinaryOperator<FieldModel> combiner() {
            return (m1, m2) -> m1;
        }

        @Override
        public Function<FieldModel, FieldModel> finisher() {
            return (m) -> model;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return EnumSet.allOf(Characteristics.class);
        }
    }

    private static final class ConcurrentFieldModelCollector
                    implements Collector<Entry<FieldId, Object>, FieldModel, FieldModel> {

        private final FieldModel model;
        private final ReentrantLock lock;

        ConcurrentFieldModelCollector(FieldModel model) {
            this.model = model;
            this.lock = new ReentrantLock();
        }

        @Override
        public Supplier<FieldModel> supplier() {
            return () -> model;
        }

        @Override
        public BiConsumer<FieldModel, Entry<FieldId, Object>> accumulator() {
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
        public BinaryOperator<FieldModel> combiner() {
            return (m1, m2) -> m1;
        }

        @Override
        public Function<FieldModel, FieldModel> finisher() {
            return (m) -> model;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return EnumSet.allOf(Characteristics.class);
        }
    }
}
