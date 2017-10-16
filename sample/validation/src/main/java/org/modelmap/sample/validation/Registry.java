package org.modelmap.sample.validation;

import static org.modelmap.core.dsl.DSL.matchAll;
import static org.modelmap.core.dsl.DSL.matchAny;
import static org.modelmap.sample.field.SampleFieldIdInfo.accountCountry;
import static org.modelmap.sample.field.SampleFieldIdInfo.accountEmail;
import static org.modelmap.sample.field.SampleFieldIdInfo.accountLanguage;
import static org.modelmap.sample.field.SampleFieldIdInfo.accountPhoneNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;

import org.modelmap.core.dsl.DSL;
import org.modelmap.core.dsl.lang.*;
import org.modelmap.sample.model.Country;
import org.modelmap.sample.model.Language;

public enum Registry implements RuleRegistry {

    ACCOUNT;

    public static final ValidationRule ACCOUNT_VALID_EMAIL;
    public static final ValidationRule ACCOUNT_VALID_EMAIL_SIZE;
    public static final ValidationRule ACCOUNT_VALID_COUNTRY;


    static {
        ACCOUNT_VALID_EMAIL = DSL.when(accountEmail().matches("\\w+[@]\\w+\\.com")
                        .or(accountEmail().matches("\\w+[@]\\w+\\.fr")))
                        .validate()
                        .withMessage("email finishes with .com or .fr")
                        .registerOn(ACCOUNT);

        ACCOUNT_VALID_EMAIL_SIZE = DSL.when(accountEmail().length().between(5, 200))
                        .validate()
                        .registerOn(ACCOUNT);

        ACCOUNT_VALID_COUNTRY = DSL.when(matchAny(accountCountry().eq(Country.FR)
                                        .and(accountLanguage().eq(Language.FR))
                                        .and(accountPhoneNumber().startsWith("+33")),
                        accountCountry().eq(Country.UK)
                                        .and(accountLanguage().eq(Language.EN))
                                        .and(accountPhoneNumber().startsWith("+45"))))
                        .validate()
                        .registerOn(ACCOUNT);


    }

    private List<ValidationRule> rules = new ArrayList<>();

    @Override
    public void register(ValidationRule rule) {
        rules.add(rule);
    }

    @Override
    public Stream<ValidationRule> stream() {
        return rules.stream();
    }

}
