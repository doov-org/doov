package io.doov.core.dsl.lang;

import io.doov.core.FieldModel;

public interface SimpleMappingRule<I, O> extends MappingRule<I, O> {

    boolean validate(FieldModel inModel, FieldModel outModel);

    void executeOn(FieldModel inModel, FieldModel outModel);

    SimpleMappingRule<I, O> registerOn(MappingRegistry registry);

}
