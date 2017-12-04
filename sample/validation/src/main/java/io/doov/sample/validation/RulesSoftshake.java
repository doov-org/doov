package io.doov.sample.validation;

import static io.doov.core.dsl.time.LocalDateSuppliers.today;
import static io.doov.sample.field.SampleFieldIdInfo.*;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.sample.model.Country;

public class RulesSoftshake {

    public static final ValidationRule EXAMPLE = DOOV.when(
                    userBirthdate().ageAt(today()).greaterOrEquals(18)
                                    .and(accountEmail().length().lesserThan(configurationMaxEmailSize()))
                                    .and(accountCountry().eq(Country.FR))
                                    .and(accountPhoneNumber().startsWith("+33")))
                    .validate();

}
