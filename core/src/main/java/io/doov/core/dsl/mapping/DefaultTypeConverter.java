package io.doov.core.dsl.mapping;

import java.util.Optional;
import java.util.function.Function;

import io.doov.core.dsl.lang.TypeConverter;

public class DefaultTypeConverter<I, O> extends AbstractTypeConverter<I, O> {

    private Function<Optional<I>, O> converter;
    private String description;

    public static <T> TypeConverter<T, T> identity() {
        return converter(Function.identity(), null, "identity");
    }

    public static <I, O> TypeConverter<I, O> converter(Function<Optional<I>, O> converter, String description) {
        return new DefaultTypeConverter<>(converter, description);
    }

    public static <I, O> TypeConverter<I, O> converter(Function<I, O> converter, O nullCase, String description) {
        return new DefaultTypeConverter<>(i -> i.map(converter).orElse(nullCase), description);
    }

    public DefaultTypeConverter(Function<Optional<I>, O> converter, String description) {
        this.converter = converter;
        this.description = description;
    }

    @Override
    O convert(I in) {
        return converter.apply(Optional.ofNullable(in));
    }

    @Override
    public String readable() {
        return description;
    }

}
