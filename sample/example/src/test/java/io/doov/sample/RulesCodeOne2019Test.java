package io.doov.sample;

import static io.doov.core.dsl.DOOV.matchAll;
import static io.doov.core.dsl.time.LocalDateSuppliers.today;
import static io.doov.sample.field.dsl.DslSampleModel.*;

import org.junit.jupiter.api.Test;

import io.doov.assertions.Assertions;
import io.doov.core.dsl.lang.Result;
import io.doov.sample.field.dsl.DslSampleModel.*;
import io.doov.sample.model.*;
import io.doov.sample.validation.RulesOld;

/**
 * @see RulesOld
 */
public class RulesCodeOne2019Test {
    SampleModel sample = SampleModels.sample();
    /**
     * @see RulesOld#validateAccount(io.doov.sample.model.User, io.doov.sample.model.Account,
     *      io.doov.sample.model.Configuration)
     */
    @Test
    void validate_email() {
        SampleModelRule rule = when(matchAll(
                userBirthdate.ageAt(today()).greaterOrEquals(18),
                accountEmail.length().lesserOrEquals(configurationMaxEmailSize),
                accountCompany.eq(Company.LES_FURETS),
                accountPhoneNumber.startsWith("+33")
        )).validate();
        sample.getAccount().setCompany(Company.CANAL_PLUS);
        Result result = rule.executeOn(sample);

        System.out.println(rule.markdown());

        Assertions.assertThat(result).isTrue();
    }
}
