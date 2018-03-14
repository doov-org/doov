package io.doov.core.dsl.mapping;

import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToString;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.TypeConverter;
import io.doov.core.dsl.meta.ConverterMetadata;
import io.doov.core.dsl.meta.MetadataVisitor;

public class DefaultTypeConverter<I, O> implements TypeConverter<I, O> {

    private final ConverterMetadata metadata;
    private Function<Optional<I>, O> converter;

    public static <T> TypeConverter<T, T> identity() {
        return new DefaultTypeConverter<>(i -> i.orElse(null), ConverterMetadata.identity());
    }

    public static <I, O> TypeConverter<I, O> converter(Function<Optional<I>, O> converter, String description) {
        return new DefaultTypeConverter<>(converter, description);
    }

    public static <I, O> TypeConverter<I, O> converter(Function<I, O> converter, O nullCase, String description) {
        return converter(i -> i.map(converter).orElse(nullCase), description);
    }

    public DefaultTypeConverter(Function<Optional<I>, O> converter, String description) {
        this(converter, ConverterMetadata.metadata(description));
    }

    public DefaultTypeConverter(Function<Optional<I>, O> converter, ConverterMetadata metadata) {
        this.converter = converter;
        this.metadata = metadata;
    }

    @Override
    public O convert(FieldModel fieldModel, DslField<I> in) {
        return converter.apply(Optional.ofNullable(fieldModel.get(in.id())));
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        metadata.accept(visitor, depth);
    }

    @Override
    public String readable() {
        return astToString(this, Locale.getDefault());
    }

}
