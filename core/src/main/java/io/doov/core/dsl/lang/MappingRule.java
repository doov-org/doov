package io.doov.core.dsl.lang;

import io.doov.core.FieldModel;
import io.doov.core.dsl.meta.SyntaxTree;

/**
 * 1-to-1 mapping rule
 *
 */
public interface MappingRule extends Readable, SyntaxTree {

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

}
