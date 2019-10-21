package io.doov.core.dsl.meta;

import java.util.function.Supplier;

public class StaticMetadata<T> extends LeafMetadata<StaticMetadata<T>> {

    private final T value;

    private final Class<T> valueClass;

    private StaticMetadata(MetadataType type, T value) {
        super(type);
        this.value = value;
        this.valueClass = value != null ? (Class<T>) value.getClass() : null;
    }

    public static <U> StaticMetadata<U> mappingLeaf(Supplier<U> valueSupplier) {
        return new StaticMetadata<>(MetadataType.MAPPING_LEAF, valueSupplier.get()).valueObject(valueSupplier.get());
    }

    public static <U> StaticMetadata<U> leaf(U value) {
        return new StaticMetadata<>(MetadataType.LEAF_VALUE, value).valueObject(value);
    }

    public T value() {
        return value;
    }

    public Class<T> valueClass() {
        return valueClass;
    }

}
