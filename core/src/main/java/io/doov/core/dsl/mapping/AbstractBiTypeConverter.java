package io.doov.core.dsl.mapping;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.BiTypeConverter;
import io.doov.core.dsl.meta.MetadataVisitor;

public abstract class AbstractBiTypeConverter<I, J, O> implements BiTypeConverter<I, J, O> {

    abstract O convert(I in, J in2);

    public abstract String readable();

    @Override
    public O convert(FieldModel fieldModel, DslField<I> in, DslField<J> in2) {
        return convert(fieldModel.get(in.id()), fieldModel.get(in2.id()));
    }

}
