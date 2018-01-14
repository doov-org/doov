package io.doov.core.dsl.mapping;

import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.dsl.lang.BiTypeConverter;

public class DefaultBiTypeConverter<I, J, O> extends AbstractBiTypeConverter<I, J, O> {

    private BiFunction<Optional<I>, Optional<J>, O> converter;
    private String description;

    public static <I, J, O> BiTypeConverter<I, J, O> biConverter(BiFunction<Optional<I>, Optional<J>, O> converter,
                                                               String description) {
        return new DefaultBiTypeConverter<>(converter, description);
    }

    public static <I, J, O> BiTypeConverter<I, J, O> biConverter(BiFunction<I, J, O> converter, O nullCase,
                                                               String description) {
        return new DefaultBiTypeConverter<>(
                (i, j) -> (i.isPresent() && j.isPresent()) ? converter.apply(i.get(), j.get()) : nullCase, description);
    }

    public static <I, J, O> BiTypeConverter<I, J, O> biConverter(BiFunction<I, J, O> converter, I nullIn, J nullIn2,
                                                               String description) {
        return new DefaultBiTypeConverter<>((i, j) -> converter.apply(i.orElse(nullIn), j.orElse(nullIn2)), description);
    }

    public DefaultBiTypeConverter(BiFunction<Optional<I>, Optional<J>, O> converter, String description) {
        this.converter = converter;
        this.description = description;
    }

    @Override
    O convert(I in, J in2) {
        return converter.apply(Optional.ofNullable(in), Optional.ofNullable(in2));
    }

    @Override
    public String readable() {
        return description;
    }
}
