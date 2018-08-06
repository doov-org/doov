package io.doov.core.dsl.mapping;

import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToString;

import java.util.*;
import java.util.function.BiFunction;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.NaryTypeConverter;
import io.doov.core.dsl.meta.ConverterMetadata;
import io.doov.core.dsl.meta.MetadataVisitor;

public class DefaultNaryTypeConverter<O> implements NaryTypeConverter<O> {

    private final ConverterMetadata metadata;
    private BiFunction<DslModel, List<DslField>, O> function;

    public DefaultNaryTypeConverter(BiFunction<DslModel, List<DslField>, O> function, String description) {
        this(function, ConverterMetadata.metadata(description));
    }

    public DefaultNaryTypeConverter(BiFunction<DslModel, List<DslField>, O> function, ConverterMetadata metadata) {
        this.function = function;
        this.metadata = metadata;
    }

    @Override
    public O convert(DslModel dslModel, Context context, DslField... fieldInfos) {
        // TODO hack
        return function.apply(dslModel, Arrays.asList(fieldInfos));
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
