package org.modelmap.core;

import java.util.Map.Entry;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Utility methods for creating and manipulating {@code FieldModel}.
 */
public class FieldModels {

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
