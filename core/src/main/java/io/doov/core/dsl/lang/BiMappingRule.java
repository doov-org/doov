package io.doov.core.dsl.lang;

import io.doov.core.FieldModel;

/**
 * 2-to-1 mapping rule
 * @param <I> 1st in type
 * @param <J> 2nd in type
 * @param <O> out type
 */
public interface BiMappingRule<I, J, O> extends MappingRule {

    boolean validate(FieldModel inModel, FieldModel outModel);

    void executeOn(FieldModel inModel, FieldModel outModel);

    BiMappingRule<I, J, O> registerOn(MappingRegistry registry);
}
