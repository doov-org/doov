package org.modelmap.core;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.function.Supplier;
import java.util.stream.*;

/**
 * Utility methods for creating and manipulating {@code FieldModel}.
 */
public class FieldModels {

    /**
     * Creates a Stream from a {@code FieldModel}.
     */
    public static Stream<Entry<FieldId, Object>> stream(FieldModel model, boolean parallel) {
        if (parallel)
            return Arrays.stream(model.getFieldInfos()).parallel()
                            .map(info -> new AbstractMap.SimpleEntry<>(info.id(), model.get(info.id())));
        else
            return Arrays.stream(model.getFieldInfos()).map(
                            info -> new AbstractMap.SimpleEntry<>(info.id(), model.get(info.id())));
    }

    /**
     * Returns a {@code Collector} that accumulates the input elements into a new {@code FieldModel}.
     */
    public static Collector<Entry<FieldId, Object>, ?, FieldModel> toFieldModel(Supplier<FieldModel> modelSupplier) {
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
}
