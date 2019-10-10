package io.doov.core.dsl.meta;

import static io.doov.core.dsl.meta.DefaultOperator.any_match_predicates;
import static io.doov.core.dsl.meta.DefaultOperator.any_match_values;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE_MATCH_ANY;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

public class IterableMetadata<E, T extends Iterable<E>> extends NaryMetadata {

    private final MetadataType type;

    public IterableMetadata(MetadataType type, Operator operator, T values) {
        super(operator, StreamSupport.stream(values.spliterator(), false)
                .map(v -> StaticMetadata.leaf(() -> v))
                .collect(toList()));
        this.type = type;
    }

    public static <T> IterableMetadata<T, Collection<T>> anyMatchMetadata(Collection<T> values) {
        return new IterableMetadata<>(FIELD_PREDICATE_MATCH_ANY, any_match_values, values);
    }

    public static IterableMetadata<String, Collection<String>> anyMatchMetadata(String... readables) {
        return new IterableMetadata<>(FIELD_PREDICATE_MATCH_ANY, any_match_predicates, Arrays.asList(readables));
    }

    public static <V,U extends Iterable<V>> IterableMetadata<V,U> mappingIterableMetadata(Supplier<U> valueSupplier) {
        return new IterableMetadata<>(MetadataType.MAPPING_LEAF_ITERABLE, MappingOperator.map, valueSupplier.get());
    }

    @Override
    public MetadataType type() {
        return type;
    }

}
