package io.doov.core.dsl.lang;

import io.doov.core.FieldModel;

/**
 * 1-to-1 mapping rule
 *
 * @param <I> in type
 * @param <O> out type
 */
public interface SimpleMappingRule<I, O> extends MappingRule {

    boolean validate(FieldModel inModel, FieldModel outModel);

    void executeOn(FieldModel inModel, FieldModel outModel);

    SimpleMappingRule<I, O> registerOn(MappingRegistry registry);

}
