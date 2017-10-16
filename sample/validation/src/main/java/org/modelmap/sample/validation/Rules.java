package org.modelmap.sample.validation;

import static org.modelmap.core.dsl.DSL.matchAny;
import static org.modelmap.sample.field.SampleFieldIdInfo.*;
import static org.modelmap.sample.validation.AccountRulesId.VALID_COUNTRY;
import static org.modelmap.sample.validation.AccountRulesId.VALID_EMAIL;
import static org.modelmap.sample.validation.AccountRulesId.VALID_EMAIL_SIZE;

import org.modelmap.core.dsl.DSL;
import org.modelmap.core.dsl.impl.DefaultRuleRegistry;
import org.modelmap.core.dsl.lang.RuleRegistry;
import org.modelmap.sample.model.Country;
import org.modelmap.sample.model.Language;

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

        DSL.when(userId().isNotNull()).validate().registerOn(REGISTRY_USER);
    }

}
