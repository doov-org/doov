package io.doov.core.dsl.mapping.converter;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.ConverterMetadata;
import io.doov.core.dsl.meta.Metadata;

public class DefaultNaryTypeConverter<O> extends AbstractDSLBuilder implements NaryTypeConverter<O> {

    private final TriFunction<DslModel, Context, List<DslField>, O> function;
    private final ConverterMetadata metadata;

    public DefaultNaryTypeConverter(TriFunction<DslModel, Context, List<DslField>, O> function,
                    ConverterMetadata metadata) {
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
    public Metadata metadata() {
        return metadata;
    }

    @Override
    public O convert(DslModel dslModel, Context context, DslField... fieldInfos) {
        return function.apply(dslModel, context, Arrays.asList(fieldInfos));
    }
}
