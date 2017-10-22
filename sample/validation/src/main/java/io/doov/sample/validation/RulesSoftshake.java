package io.doov.sample.validation;

import static io.doov.core.dsl.LocalDateSuppliers.today;
import static io.doov.sample.field.SampleFieldIdInfo.*;

import io.doov.core.dsl.DSL;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.sample.model.Country;

public class RulesSoftshake {

    public static final ValidationRule EXAMPLE = DSL.when(
                    userBirthdate().ageAt(today()).greaterOrEquals(18L)
                                    .and(accountEmail().length().lesserThan(configurationMaxEmailSize()))
                                    .and(accountCountry().eq(Country.FR))
                                    .and(accountPhoneNumber().startsWith("+33")))
                    .validate();

}
