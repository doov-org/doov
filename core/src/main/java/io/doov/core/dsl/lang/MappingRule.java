package io.doov.core.dsl.lang;

import io.doov.core.FieldModel;
import io.doov.core.dsl.meta.SyntaxTree;

public interface MappingRule<I, O> extends Readable, SyntaxTree  {

    boolean validate(FieldModel inModel, FieldModel outModel);

    void executeOn(FieldModel inModel, FieldModel outModel);

    MappingRule<I, O> registerOn(MappingRegistry registry);

}
