package io.doov.core.dsl.lang;

import java.util.Locale;
import java.util.stream.Stream;

import io.doov.core.FieldModel;
import io.doov.core.dsl.meta.SyntaxTree;

/**
 * Mapping rule
 */
public interface MappingRule extends Readable, SyntaxTree {
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
     * Verifies the mapping rule for given in/out models
     *
     * @param inModel  in model
     * @param outModel out model
     * @return true if this rule can execute on the in/out models
     */
    boolean validate(FieldModel inModel, FieldModel outModel);

    /**
     * Execute the mapping rule on in/out models
     *
     * @param inModel  in model
     * @param outModel out model
     */
    void executeOn(FieldModel inModel, FieldModel outModel);

    /**
     * Stream over mapping rules contained in this rule
     * Default implementation returns a stream of itself.
     *
     * @return mapping rule stream
     */
    default Stream<MappingRule> stream() {
        return Stream.of(this);
    }
}
