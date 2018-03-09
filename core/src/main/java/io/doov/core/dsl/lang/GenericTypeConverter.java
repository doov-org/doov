package io.doov.core.dsl.lang;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.meta.SyntaxTree;

/**
 * Generic Type converter
 * @param <O> out type
 */
public interface GenericTypeConverter<O> extends Readable, SyntaxTree {

    /**
     * Convert the given in fields in the model to the value in type {@link O}
     * @param fieldModel in model
     * @param ins in fields
     * @return out value
     */
    O convert(FieldModel fieldModel, DslField... ins);
}
