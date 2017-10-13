package org.modelmap.sample.validation;

import static org.modelmap.core.dsl.DSL.matchAny;
import static org.modelmap.sample.field.SampleFieldIdInfo.EMAIL;
import static org.modelmap.sample.field.SampleFieldIdInfo.accountCountry;
import static org.modelmap.sample.field.SampleFieldIdInfo.accountLanguage;
import static org.modelmap.sample.field.SampleFieldIdInfo.accountPhoneNumber;

import java.util.ArrayList;
import java.util.List;

import org.modelmap.core.dsl.DSL;
import org.modelmap.core.dsl.lang.ValidationRule;
import org.modelmap.sample.model.Country;
import org.modelmap.sample.model.Language;

public class Rules {

    public static final List<ValidationRule> rules = new ArrayList<>();

    public static final ValidationRule VALID_EMAIL = DSL
                    .when(EMAIL.matches("\\w+[@]\\w+\\.com")
                                    .or(EMAIL.matches("\\w+[@]\\w+\\.fr")))
                    .validate()
                    .withMessage("email finishes with .com or .fr");

    public static final ValidationRule VALID_COUNTRY = DSL
                    .when(matchAny(accountCountry().eq(Country.FR)
                                                    .and(accountLanguage().eq(Language.FR))
                                                    .and(accountPhoneNumber().startsWith("+33")),
                                    accountCountry().eq(Country.UK)
                                                    .and(accountLanguage().eq(Language.EN))
                                                    .and(accountPhoneNumber().startsWith("+45"))))
                    .validate();

    static {
        rules.add(VALID_EMAIL);
        rules.add(VALID_COUNTRY);
    }

}
