package io.doov.core.dsl.mapping.converter;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.ConverterMetadata;
import io.doov.core.dsl.meta.Metadata;

public class DefaultTypeConverter<I, O> extends AbstractDSLBuilder implements TypeConverter<I, O> {

    private static final TypeConverter<?, ?> IDENTITY = new DefaultTypeConverter<>((context, i) -> i.orElse(null),
                    ConverterMetadata.identity());

    private final BiFunction<Context, Optional<I>, O> function;
    private final ConverterMetadata metadata;

    @SuppressWarnings("unchecked")
    public static <T> TypeConverter<T, T> identity() {
        return (TypeConverter<T, T>) IDENTITY;
    }

    public DefaultTypeConverter(BiFunction<Context, Optional<I>, O> function, ConverterMetadata metadata) {
        this.function = function;
        this.metadata = metadata;
    }

    public DefaultTypeConverter(BiFunction<Context, Optional<I>, O> function, String description) {
        this(function, ConverterMetadata.metadata(description));
    }

    public DefaultTypeConverter(Function<Optional<I>, O> function, String description) {
        this((context, i) -> function.apply(i), description);
    }

    @Override
    public Metadata metadata() {
        return metadata;
    }

    @Override
    public O convert(DslModel fieldModel, Context context, I input) {
        return function.apply(context, Optional.ofNullable(input));
    }
}
