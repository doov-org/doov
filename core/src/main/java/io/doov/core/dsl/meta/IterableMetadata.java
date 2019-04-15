package io.doov.core.dsl.meta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class IterableMetadata<E, T extends Iterable<E>> extends LeafMetadata<IterableMetadata<E,T>> {

    private final T value;
    private final List<StaticMetadata<E>> items;

    private IterableMetadata(MetadataType type, T value) {
        super(type);
        this.value = value;
        this.items = new ArrayList<>();
        value.forEach(e -> items.add(StaticMetadata.create(() -> e)));
    }

    public static <V,U extends Iterable<V>> IterableMetadata<V,U> create(Supplier<U> valueSupplier) {
        return new IterableMetadata<>(MetadataType.MAPPING_LEAF_ITERABLE, valueSupplier.get())
                .valueSupplier(valueSupplier);
    }

    public T value() {
        return value;
    }

    public List<StaticMetadata<E>> items() {
        return items;
    }

}
