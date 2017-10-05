package org.modelmap.sample.validation;

import static org.modelmap.sample.field.SampleFieldIdInfo.EMAIL;

import org.modelmap.core.dsl.DSL;
import org.modelmap.core.dsl.lang.StepThrowMessage;

public class Rules {

    public static final StepThrowMessage EMAIL_INVALID = DSL
                    .when(EMAIL.matches("\\w+[@]\\w+\\.com").not()
                                    .and(EMAIL.matches("\\w+[@]\\w+\\.fr").not()))
                    .throwMessage("invalid email");

}
