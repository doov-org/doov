package org.modelmap.sample.validation;

import static org.modelmap.sample.field.SampleFieldIdInfo.EMAIL;

import java.util.ArrayList;
import java.util.List;

import org.modelmap.core.dsl.DSL;
import org.modelmap.core.dsl.lang.ValidationRule;

public class Rules {

    public static final ValidationRule EMAIL_VALID = DSL
                    .when(EMAIL.matches("\\w+[@]\\w+\\.com")
                                    .or(EMAIL.matches("\\w+[@]\\w+\\.fr")))
                    .validate()
                    .withMessage("email finishes with .com or .fr");

    public static final List<ValidationRule> rules = new ArrayList<>();

    static {
        rules.add(EMAIL_VALID);
    }

}
