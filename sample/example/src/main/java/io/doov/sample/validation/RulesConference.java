package io.doov.sample.validation;

import static io.doov.core.dsl.DOOV.matchAll;
import static io.doov.core.dsl.impl.DefaultRuleRegistry.REGISTRY_DEFAULT;
import static io.doov.core.dsl.time.LocalDateSuppliers.today;
import static io.doov.sample.field.dsl.DslSampleModel.*;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.sample.model.Country;

public interface RulesConference {

    ValidationRule userAccount = DOOV
        // Entry point is when
        .when(userBirthdate.ageAt(today()).greaterOrEquals(18)
            .and(accountEmail.length().lesserOrEquals(configurationMaxEmailSize))
            .and(accountCountry.eq(Country.FR))
            .and(accountPhoneNumber.startsWith("+33")))
        // Terminal operation is validate
        .validate()
        // Optional: add to registry
        .registerOn(REGISTRY_DEFAULT);

    ValidationRule userAccountMatch = DOOV
        .when(matchAll(
            userBirthdate.ageAt(today()).greaterOrEquals(18),
            accountEmail.length().lesserOrEquals(configurationMaxEmailSize),
            accountCountry.eq(Country.FR),
            accountPhoneNumber.startsWith("+33")))
        .validate();

}
