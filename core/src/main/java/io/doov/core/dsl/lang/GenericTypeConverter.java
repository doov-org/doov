package io.doov.core.dsl.lang;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.meta.SyntaxTree;

public interface GenericTypeConverter<O> extends Readable, SyntaxTree {

    O convert(FieldModel fieldModel, DslField... ins);
}
