package io.doov.sample.validation;

import static io.doov.core.dsl.DSL.matchAll;
import static io.doov.core.dsl.LocalDateSuppliers.today;
import static io.doov.sample.field.SampleFieldIdInfo.*;
import static io.doov.sample.validation.id.AccountRulesId.VALID_ACCOUNT;
import static io.doov.sample.validation.id.AccountRulesId.VALID_EMAIL;
import static java.time.temporal.ChronoUnit.DAYS;

import io.doov.core.dsl.DSL;
import io.doov.core.dsl.impl.DefaultRuleRegistry;
import io.doov.core.dsl.lang.RuleRegistry;
import io.doov.sample.model.Country;

public class Rules extends DefaultRuleRegistry {

    public static final RuleRegistry REGISTRY_ACCOUNT = new DefaultRuleRegistry();
    public static final RuleRegistry REGISTRY_USER = new DefaultRuleRegistry();

    static {
        DSL.when(accountEmail().matches("\\w+[@]\\w+\\.com")
                        .or(accountEmail().matches("\\w+[@]\\w+\\.fr")))
                        .validate()
                        .withMessage("email finishes with .com or .fr")
                        .registerOn(REGISTRY_ACCOUNT, VALID_EMAIL);

        DSL.when(matchAll(userBirthdate().ageAt(today()).greaterOrEquals(18L),
                        accountEmail().length().lesserOrEquals(configurationMaxEmailSize()),
                        accountCountry().eq(Country.FR).and(accountPhoneNumber().startsWith("+33"))))
                        .validate()
                        .registerOn(REGISTRY_ACCOUNT, VALID_ACCOUNT);

        DSL.when(userBirthdate().ageAt(today()).greaterOrEquals(18L)
                        .and(accountEmail().length().lesserOrEquals(configurationMaxEmailSize()))
                        .and(accountCountry().eq(Country.FR))
                        .and(accountPhoneNumber().startsWith("+33")))
                        .validate()
                        .registerOn(REGISTRY_ACCOUNT, VALID_ACCOUNT);
    }

    static {
        DSL.when(userId().isNotNull())
                        .validate()
                        .registerOn(REGISTRY_USER);

        DSL.when(userBirthdate().ageAt(today()).greaterOrEquals(18L)).validate()
                        .registerOn(REGISTRY_USER);

        DSL.when(userBirthdate().before(userBirthdate().minus(1, DAYS))).validate()
                        .registerOn(REGISTRY_USER);
    }

}
