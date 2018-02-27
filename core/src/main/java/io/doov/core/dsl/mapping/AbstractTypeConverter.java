package io.doov.core.dsl.mapping;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.lang.TypeConverter;
import io.doov.core.dsl.meta.MetadataVisitor;

public abstract class AbstractTypeConverter<I, O> implements TypeConverter<I, O> {

    abstract O convert(I in);

    public abstract String readable();

    @Override
    public O convert(FieldModel fieldModel, DslField<I> in) {
        return convert(fieldModel.get(in.id()));
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        // TODO
    }
}
