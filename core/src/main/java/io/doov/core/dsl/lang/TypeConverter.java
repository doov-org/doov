package io.doov.core.dsl.lang;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.meta.SyntaxTree;

/**
 * Type converter
 *
 * @param <I> in type
 * @param <O> out type
 */
public interface TypeConverter<I, O> extends SyntaxTree {
    /**
     * Convert the given field in with type {@link O}, the model to the value in type {@link O}
     *
     * @param fieldModel field model
     * @param in in field
     * @return converted output value
     */
    O convert(FieldModel fieldModel, DslField<I> in);

}
