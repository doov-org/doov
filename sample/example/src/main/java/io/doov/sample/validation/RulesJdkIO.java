package io.doov.sample.validation;

import static io.doov.core.dsl.time.LocalDateSuppliers.today;
import static io.doov.sample.field.dsl.DslSampleModel.*;

import io.doov.sample.field.dsl.DslSampleModel;
import io.doov.sample.field.dsl.DslSampleModel.*;
import io.doov.sample.model.Country;

public class RulesJdkIO {

    static SampleModelRule account = DslSampleModel
            .when(userBirthdate.ageAt(today()).greaterOrEquals(18)
                    .and(accountEmail.length().lesserOrEquals(configurationMaxEmailSize))
                    .and(accountCountry.eq(Country.FR))
                    .and(accountPhoneNumber.startsWith("+33")))
            .validate();

}
