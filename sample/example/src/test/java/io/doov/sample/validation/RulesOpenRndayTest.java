package io.doov.sample.validation;

import static io.doov.core.dsl.time.LocalDateSuppliers.today;
import static io.doov.sample.field.dsl.DslSampleModel.*;

import java.util.Locale;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.doov.assertions.Assertions;
import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.lang.Result;
import io.doov.sample.field.dsl.DslSampleModel;
import io.doov.sample.field.dsl.DslSampleModel.SampleModelRule;
import io.doov.sample.model.*;

public class RulesOpenRndayTest {
    SampleModel sample = SampleModels.sample();
    /**
     * @see RulesOld#validateAccount(io.doov.sample.model.User, io.doov.sample.model.Account,
     *      io.doov.sample.model.Configuration)
     */
    SampleModelRule demoRule = DslSampleModel
                    .when(DOOV.matchAll(
                                    userBirthdate.ageAt(today()).greaterOrEquals(18),
                                    accountEmail.length().lesserOrEquals(configurationMaxEmailSize),
                                    accountCompany.eq(Company.LES_FURETS),
                                    accountPhoneNumber.startsWith("+33")))
                    .validate();

    @Test
    public void test_account() {
        Result result = demoRule.executeOn(sample);

        Assertions.assertThat(result).isTrue();
        System.out.println(demoRule.readable());
        System.out.println(demoRule.markdown(Locale.FRANCE));
    }

    /**
     * Test is local platform dedpendent, hasFailureCause should manage locale properly
     */
    @Test
    @Disabled
    public void test_account_failure_cause() {
        sample.getAccount().setPhoneNumber("+1 12 34 56 78");

        Result result = demoRule.executeOn(sample);

        Assertions.assertThat(result)
                        .isFalse()
                        .hasFailureCause("account phone number starts with '+33'");
    }

    /**
     * Test is local platform dedpendent, hasFailureCause should manage locale properly
     */
    @Test
    @Disabled
    public void test_account_failure_cause_2() {
        sample.getAccount().setPhoneNumber("+1 12 34 56 78");
        sample.getAccount().setCompany(Company.BLABLACAR);

        Result result = demoRule.withShortCircuit(false).executeOn(sample);

        Assertions.assertThat(result)
                        .isFalse()
                        .hasFailureCause("match all [" +
                                        "account company = LES_FURETS, " +
                                        "account phone number starts with '+33']");
    }
}
