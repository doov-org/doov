package io.doov.core.dsl.lang;

import io.doov.core.FieldModel;

/**
 * n-to-1 mapping rule
 *
 * @param <O> out type
 */
public interface NaryMappingRule<O> extends MappingRule {

    boolean validate(FieldModel inModel, FieldModel outModel);

    void executeOn(FieldModel inModel, FieldModel outModel);

    NaryMappingRule<O> registerOn(MappingRegistry registry);
}
