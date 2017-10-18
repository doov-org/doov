package io.doov.sample.validation;

import static io.doov.core.dsl.DSL.matchAny;
import static io.doov.core.dsl.LocalDateSuppliers.todayMinusYears;
import static io.doov.sample.field.SampleFieldIdInfo.*;
import static io.doov.sample.validation.AccountRulesId.VALID_COUNTRY;
import static io.doov.sample.validation.AccountRulesId.VALID_EMAIL;
import static io.doov.sample.validation.AccountRulesId.VALID_EMAIL_SIZE;
import static io.doov.sample.validation.UserRulesId.VALID_ADULT;

import io.doov.core.dsl.DSL;
import io.doov.core.dsl.impl.DefaultRuleRegistry;
import io.doov.core.dsl.lang.RuleRegistry;
import io.doov.sample.model.Country;
import io.doov.sample.model.Language;

public class Rules extends DefaultRuleRegistry {

    public static final RuleRegistry REGISTRY_ACCOUNT = new DefaultRuleRegistry();
    public static final RuleRegistry REGISTRY_USER = new DefaultRuleRegistry();

    static {
        DSL.when(accountEmail().length().lesserOrEquals(accountMaxEmailSize()))
                        .validate()
                        .registerOn(REGISTRY_ACCOUNT, VALID_EMAIL_SIZE);

        DSL.when(accountEmail().matches("\\w+[@]\\w+\\.com")
                        .or(accountEmail().matches("\\w+[@]\\w+\\.fr")))
                        .validate()
                        .withMessage("email finishes with .com or .fr")
                        .registerOn(REGISTRY_ACCOUNT, VALID_EMAIL);

        DSL.when(matchAny(accountCountry().eq(Country.FR)
                                        .and(accountLanguage().eq(Language.FR))
                                        .and(accountPhoneNumber().startsWith("+33")),
                        accountCountry().eq(Country.UK)
                                        .and(accountLanguage().eq(Language.EN))
                                        .and(accountPhoneNumber().startsWith("+45"))))
                        .validate()
                        .registerOn(REGISTRY_ACCOUNT, VALID_COUNTRY);

    }

    static {
        DSL.when(userId().isNotNull())
                        .validate()
                        .registerOn(REGISTRY_USER);

        DSL.when(userBirthdate().before(todayMinusYears(18)))
                        .validate()
                        .registerOn(REGISTRY_USER, VALID_ADULT);
    }

}
