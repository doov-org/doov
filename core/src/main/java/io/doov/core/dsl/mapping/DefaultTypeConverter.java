package io.doov.core.dsl.mapping;

import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToString;

import java.util.Locale;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.TypeConverter;
import io.doov.core.dsl.meta.ConverterMetadata;
import io.doov.core.dsl.meta.MetadataVisitor;

public class DefaultTypeConverter<I, O> implements TypeConverter<I, O> {

    private static final TypeConverter<?, ?> IDENTITY = new DefaultTypeConverter<>(
            (context, i) -> i.orElse(null), ConverterMetadata.identity());

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
    public O convert(DslModel fieldModel, Context context, DslField<I> in) {
        return function.apply(context, Optional.ofNullable(fieldModel.get(in.id())));
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        metadata.accept(visitor, depth);
    }

    @Override
    public String readable(Locale locale) {
        return astToString(this, locale);
    }

}
