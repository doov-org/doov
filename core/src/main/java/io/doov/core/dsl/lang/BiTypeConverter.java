package io.doov.core.dsl.lang;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.meta.SyntaxTree;

public interface BiTypeConverter<I, J, O> extends Readable, SyntaxTree {

    O convert(FieldModel fieldModel, DslField<I> in, DslField<J> in2);
}
