package io.doov.core.dsl.lang;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.meta.SyntaxTree;

/**
 * Generic Type converter
 *
 * @param <O> out type
 */
public interface NaryTypeConverter<O> extends SyntaxTree {
    /**
     * Convert the given in fields in the model to the value in type {@link O}
     *
     * @param fieldModel in model
     * @param ins in fields
     * @return out value
     */
    O convert(DslModel fieldModel, Context context, DslField... ins);
}
