package io.doov.core;

import java.util.EnumSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Utility methods for creating and manipulating {@code FieldModel}.
 */
public class FieldModels {

    /**
     * Returns a {@code Collector} that accumulates the input elements into a unique {@code FieldModel}.
     */
    public static <Fm extends FieldModel> Collector<Entry<FieldId, Object>, ?, Fm> toFieldModel(Fm model) {
        return new FieldModelCollector<Fm>(model);
    }

    /**
     * Returns a concurrent {@code Collector} that accumulates the input elements into a unique {@code FieldModel}.
     */
    public static <Fm extends FieldModel> Collector<Entry<FieldId, Object>, ?, Fm> toConcurrentFieldModel(Fm model) {
        return new ConcurrentFieldModelCollector<Fm>(model);
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
