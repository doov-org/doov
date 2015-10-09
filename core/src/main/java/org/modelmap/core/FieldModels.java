package org.modelmap.core;

import java.util.EnumSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Utility methods for creating and manipulating {@code FieldModel}.
 */
public class FieldModels {

    /**
     * Returns a {@code Collector} that accumulates the input elements into a new {@code FieldModel}.
     */
    public static Collector<Entry<FieldId, Object>, ?, FieldModel> toMapThenFieldModel(
                    Supplier<FieldModel> modelSupplier) {
        return Collectors.collectingAndThen(
                        Collectors.toMap(Entry::getKey, Entry::getValue),
                        m -> {
                            FieldModel model = modelSupplier.get();
                            m.forEach(model::set);
                            return model;
                        });
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a new {@code FieldModel}.
     */
    public static Collector<Entry<FieldId, Object>, ?, FieldModel> toFieldModel(FieldInfo... fieldInfos) {
        return Collectors.collectingAndThen(
                        Collectors.toMap(Entry::getKey, Entry::getValue),
                        m -> new BaseFieldModel(m, fieldInfos));
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a unique {@code FieldModel}.
     */
    public static Collector<Entry<FieldId, Object>, ?, FieldModel> toFieldModel(FieldModel model) {
        return new FieldModelCollector(model);
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
}
