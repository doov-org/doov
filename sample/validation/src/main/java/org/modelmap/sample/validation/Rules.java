package org.modelmap.sample.validation;

import static org.modelmap.sample.field.SampleFieldIdInfo.EMAIL;

import org.modelmap.core.dsl.DSL;
import org.modelmap.core.dsl.lang.StepValidate;

public class Rules {

    public static final StepValidate EMAIL_INVALID = DSL
                    .when(EMAIL.matches("\\w+[@]\\w+\\.com").not()
                                    .and(EMAIL.matches("\\w+[@]\\w+\\.fr").not()))
                    .validate()
                    .withMessage("invalid email");

}
