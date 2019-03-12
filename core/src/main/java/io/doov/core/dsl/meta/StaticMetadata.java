package io.doov.core.dsl.meta;

import java.util.function.Supplier;

public class StaticMetadata<T> extends LeafMetadata<StaticMetadata<T>> {

    private final T value;

    private final Class<T> valueClass;

    private StaticMetadata(MetadataType type, T value) {
        super(type);
        this.value = value;
        this.valueClass = (Class<T>) value.getClass();
    }

    public static <U> StaticMetadata<U> create(Supplier<U> valueSupplier) {
        return new StaticMetadata<>(MetadataType.MAPPING_LEAF, valueSupplier.get())
                .valueSupplier(valueSupplier);
    }

    public T value() {
        return value;
    }

    public Class<T> valueClass() {
        return valueClass;
    }

}
