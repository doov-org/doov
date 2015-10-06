/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core;

import static java.util.stream.Collector.Characteristics.CONCURRENT;
import static java.util.stream.Collector.Characteristics.UNORDERED;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.*;
import java.util.stream.*;

/**
 * Utility methods for creating and manipulating {@code FieldModel}.
 */
public class FieldModels {

    /**
     * A {@code Spliterator} designed for use by sources that traverse and split all key/value pairs maintained in a
     * {@code FieldModel}.
     */
    public static Spliterator<Entry<FieldId, Object>> spliterator(FieldModel model) {
        Map<FieldId, Object> map = new HashMap<>();
        for (FieldInfo info : model.getFieldInfos()) {
            map.put(info.id(), model.get(info.id()));
        }
        return map.entrySet().spliterator();
    }

    /**
     * Creates a new sequential  from a {@code FieldModel}.
     */
    public static Stream<Entry<FieldId, Object>> stream(FieldModel model) {
        return StreamSupport.stream(spliterator(model), false);
    }

    /**
     * Creates a new sequential or parallel {@code Stream} from a {@code FieldModel}.
     */
    public static Stream<Entry<FieldId, Object>> stream(FieldModel model, boolean parallel) {
        return StreamSupport.stream(spliterator(model), parallel);
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a new {@code FieldModel}.
     */
    public static Collector<Entry<FieldId, Object>, ?, FieldModel> toFieldModel(FieldInfo... fieldInfos) {
        return new FieldModelCollector(() -> new BaseFieldModel(fieldInfos), fieldInfos);
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a new {@code FieldModel}.
     */
    public static Collector<Entry<FieldId, Object>, ?, FieldModel> toFieldModel(FieldModel model) {
        return new FieldModelCollector(() -> model, model.getFieldInfos());
    }

    private static final class FieldModelCollector
                    implements Collector<Entry<FieldId, Object>, FieldModel, FieldModel> {

        private final FieldInfo[] fieldInfos;
        private final Supplier<FieldModel> finisher;

        FieldModelCollector(Supplier<FieldModel> finisher, FieldInfo... fieldInfos) {
            this.fieldInfos = fieldInfos;
            this.finisher = finisher;
        }

        @Override
        public Supplier<FieldModel> supplier() {
            return () -> new BaseFieldModel(new ConcurrentHashMap<>(), fieldInfos);
        }

        @Override
        public BiConsumer<FieldModel, Entry<FieldId, Object>> accumulator() {
            return (model, entry) -> model.set(entry.getKey(), entry.getValue());
        }

        @Override
        public BinaryOperator<FieldModel> combiner() {
            return (m1, m2) -> {
                m2.setAll(m1);
                return m2;
            };
        }

        @Override
        public Function<FieldModel, FieldModel> finisher() {
            return (m) -> {
                FieldModel model = finisher.get();
                model.setAll(m);
                return model;
            };
        }

        @Override
        public Set<Characteristics> characteristics() {
            return EnumSet.of(CONCURRENT, UNORDERED);
        }
    }
}
