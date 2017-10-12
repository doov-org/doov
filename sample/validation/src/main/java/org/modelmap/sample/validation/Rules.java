package org.modelmap.sample.validation;

import static org.modelmap.sample.field.SampleFieldIdInfo.EMAIL;

import org.modelmap.core.dsl.DSL;
import org.modelmap.core.dsl.lang.StepValidate;

public class Rules {

    public static final StepValidate EMAIL_VALID = DSL
                    .when(EMAIL.matches("\\w+[@]\\w+\\.com")
                                    .or(EMAIL.matches("\\w+[@]\\w+\\.fr")))
                    .validate()
                    .withMessage("email finishes with .com or .fr");

}
