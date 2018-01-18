package io.doov.sample.validation;

import static io.doov.core.dsl.time.LocalDateSuppliers.today;
import static io.doov.sample.field.SampleFieldIdInfo.*;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.sample.model.Country;

public class RulesSnowcamp {

    public static ValidationRule rule = DOOV
                    .when(userBirthdate().ageAt(today()).greaterOrEquals(18)
                                    .and(accountEmail().length().lesserOrEquals(configurationMaxEmailSize()))
                                    .and(accountCountry().eq(Country.FR))
                                    .and(accountPhoneNumber().startsWith("+33")))
                    .validate();

}
