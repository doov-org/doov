package io.doov.core.dsl.lang;

import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.meta.SyntaxTree;

/**
 * Bi type converter
 *
 * @param <I> 1st in type
 * @param <J> 2nd in type
 * @param <O> out type
 */
public interface BiTypeConverter<I, J, O> extends SyntaxTree {
    /**
     * Convert the given fields in with type {@link O} {@link J}, the model to the value in type {@link O}
     *
     * @param fieldModel field model
     * @param context context
     * @param in 1st input
     * @param in2 2nd input
     * @return output value
     */
    O convert(DslModel fieldModel, Context context, I in, J in2);
}
