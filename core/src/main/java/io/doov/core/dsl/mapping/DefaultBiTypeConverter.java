package io.doov.core.dsl.mapping;

import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToString;

import java.util.Locale;
import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.BiTypeConverter;
import io.doov.core.dsl.meta.ConverterMetadata;
import io.doov.core.dsl.meta.MetadataVisitor;

public class DefaultBiTypeConverter<I, J, O> implements BiTypeConverter<I, J, O> {

    private BiFunction<Optional<I>, Optional<J>, O> converter;
    private ConverterMetadata metadata;

    public DefaultBiTypeConverter(BiFunction<Optional<I>, Optional<J>, O> converter, String description) {
        this(converter, ConverterMetadata.metadata(description));
    }

    public DefaultBiTypeConverter(BiFunction<Optional<I>, Optional<J>, O> converter, ConverterMetadata metadata) {
        this.converter = converter;
        this.metadata = metadata;
    }

    @Override
    public O convert(FieldModel fieldModel, DslField<I> in, DslField<J> in2) {
        return converter.apply(
                Optional.ofNullable(fieldModel.get(in.id())),
                Optional.ofNullable(fieldModel.get(in2.id())));
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
