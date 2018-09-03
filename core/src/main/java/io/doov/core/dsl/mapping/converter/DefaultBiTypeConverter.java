package io.doov.core.dsl.mapping.converter;

import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToString;

import java.util.Locale;
import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.ConverterMetadata;
import io.doov.core.dsl.meta.MetadataVisitor;

public class DefaultBiTypeConverter<I, J, O> implements BiTypeConverter<I, J, O> {

    private TriFunction<Context, Optional<I>, Optional<J>, O> function;
    private ConverterMetadata metadata;

    public DefaultBiTypeConverter(TriFunction<Context, Optional<I>, Optional<J>, O> function,
            ConverterMetadata metadata) {
        this.function = function;
        this.metadata = metadata;
    }

    public DefaultBiTypeConverter(TriFunction<Context, Optional<I>, Optional<J>, O> converter, String description) {
        this(converter, ConverterMetadata.metadata(description));
    }

    public DefaultBiTypeConverter(BiFunction<Optional<I>, Optional<J>, O> function, String description) {
        this((c, i, j) -> function.apply(i, j), description);
    }

    @Override
    public O convert(DslModel fieldModel, Context context, I in, J in2) {
        return function.apply(context, Optional.ofNullable(in), Optional.ofNullable((in2)));
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
