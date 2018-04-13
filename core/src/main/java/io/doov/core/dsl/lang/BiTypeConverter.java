package io.doov.core.dsl.lang;

import java.util.Locale;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.meta.SyntaxTree;

/**
 * Bi type converter
 *
 * @param <I> 1st in type
 * @param <J> 2nd in type
 * @param <O> out type
 */
public interface BiTypeConverter<I, J, O> extends Readable, SyntaxTree {
    @Override
    default String readable() {
        return readable(Locale.getDefault());
    }

    /**
     * Returns the human readable version of this object.
     *
     * @return the readable string
     */
    String readable(Locale locale);

    /**
     * Convert the given fields in with type {@link O} {@link J}, the model to the value in type {@link O}
     *
     * @param fieldModel field model
     * @param in         1st in field
     * @param in2        2nd in field
     * @return output value
     */
    O convert(FieldModel fieldModel, DslField<I> in, DslField<J> in2);
}
