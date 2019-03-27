package io.doov.core.dsl.meta;

import java.util.function.Supplier;

public class StaticMetadata<T> extends LeafMetadata<StaticMetadata<T>> {

    private final T value;

    private final Class<T> valueClass;

    private StaticMetadata(T value) {
        super(MetadataType.MAPPING_LEAF);
        this.value = value;
        this.valueClass = value != null ? (Class<T>) value.getClass() : null;
    }

    public static <U> StaticMetadata<U> create(Supplier<U> valueSupplier) {
        return new StaticMetadata<>(valueSupplier.get())
                .valueSupplier(valueSupplier);
    }

    public T value() {
        return value;
    }

    public Class<T> valueClass() {
        return valueClass;
    }

}
