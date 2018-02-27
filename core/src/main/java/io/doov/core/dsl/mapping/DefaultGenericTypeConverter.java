package io.doov.core.dsl.mapping;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.GenericTypeConverter;
import io.doov.core.dsl.meta.MetadataVisitor;

public class DefaultGenericTypeConverter<O> implements GenericTypeConverter<O> {

    private BiFunction<FieldModel, List<DslField>, O> function;
    private String description;

    public static <O> GenericTypeConverter<O> nConverter(BiFunction<FieldModel, List<DslField>, O> function,
                                                 String description) {
        return new DefaultGenericTypeConverter<>(function, description);
    }

    public DefaultGenericTypeConverter(BiFunction<FieldModel, List<DslField>, O> function, String description) {
        this.function = function;
        this.description = description;
    }

    @Override
    public O convert(FieldModel fieldModel, DslField... fieldInfos) {
        // TODO hack
        return function.apply(fieldModel, Arrays.asList(fieldInfos));
    }

    @Override
    public String readable() {
        return description;
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        // TODO
    }
}
