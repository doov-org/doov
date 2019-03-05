package io.doov.sample.validation;

import static io.doov.core.dsl.DOOV.matchAll;
import static io.doov.core.dsl.time.LocalDateSuppliers.today;
import static io.doov.sample.field.dsl.DslSampleModel.*;

import io.doov.sample.field.dsl.DslSampleModel;
import io.doov.sample.field.dsl.DslSampleModel.SampleModelRule;
import io.doov.sample.model.Country;

public interface RulesConference {

    SampleModelRule userAccount = DslSampleModel
            // Entry point is when
            .when(userBirthdate.ageAt(today()).greaterOrEquals(18)
                    .and(accountEmail.length().lesserOrEquals(configurationMaxEmailSize))
                    .and(accountCountry.eq(Country.FR)).and(accountPhoneNumber.startsWith("+33")))
            // Terminal operation is validate
            .validate();

    SampleModelRule userAccountMatch = DslSampleModel.when(matchAll(userBirthdate.ageAt(today()).greaterOrEquals(18),
            accountEmail.length().lesserOrEquals(configurationMaxEmailSize), accountCountry.eq(Country.FR),
            accountPhoneNumber.startsWith("+33"))).validate();

}
