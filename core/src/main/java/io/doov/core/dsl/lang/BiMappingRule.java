package io.doov.core.dsl.lang;

import io.doov.core.FieldModel;

public interface BiMappingRule<I, J, O> extends MappingRule {

    boolean validate(FieldModel inModel, FieldModel outModel);

    void executeOn(FieldModel inModel, FieldModel outModel);

    BiMappingRule<I, J, O> registerOn(MappingRegistry registry);
}
