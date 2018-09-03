package io.doov.core.dsl.mapping.converter;

import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToString;

import java.util.*;
import java.util.function.BiFunction;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.ConverterMetadata;
import io.doov.core.dsl.meta.MetadataVisitor;

public class DefaultNaryTypeConverter<O> implements NaryTypeConverter<O> {

    private final TriFunction<DslModel, Context, List<DslField>, O> function;
    private final ConverterMetadata metadata;

    public DefaultNaryTypeConverter(TriFunction<DslModel, Context, List<DslField>, O> function, ConverterMetadata metadata) {
        this.function = function;
        this.metadata = metadata;
    }

    public DefaultNaryTypeConverter(TriFunction<DslModel, Context, List<DslField>, O> function, String description) {
        this(function, ConverterMetadata.metadata(description));
    }

    public DefaultNaryTypeConverter(BiFunction<DslModel, List<DslField>, O> function, String description) {
        this((m, c, f) -> function.apply(m, f), description);
    }

    @Override
    public O convert(DslModel dslModel, Context context, DslField... fieldInfos) {
        return function.apply(dslModel, context, Arrays.asList(fieldInfos));
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
