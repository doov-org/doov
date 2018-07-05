package io.doov.sample.validation;

import static io.doov.assertions.Assertions.assertThat;
import static io.doov.core.dsl.DOOV.matchAll;
import static io.doov.core.dsl.DOOV.matchNone;
import static io.doov.core.dsl.DOOV.not;
import static io.doov.sample.field.dsl.DslSampleModel.accountCompany;
import static io.doov.sample.field.dsl.DslSampleModel.accountCountry;
import static io.doov.sample.field.dsl.DslSampleModel.when;
import static io.doov.sample.model.Company.BLABLACAR;
import static io.doov.sample.model.Company.CANAL_PLUS;
import static io.doov.sample.model.Company.DAILYMOTION;
import static io.doov.sample.model.Company.LES_FURETS;
import static io.doov.sample.model.Company.MEETIC;
import static io.doov.sample.model.Company.OODRIVE;
import static io.doov.sample.model.Country.UK;
import static io.doov.sample.model.Country.US;

import org.junit.jupiter.api.Test;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.lang.Result;
import io.doov.sample.field.dsl.DslSampleModel.SampleModelRule;
import io.doov.sample.model.SampleModel;
import io.doov.sample.model.SampleModels;

public class RulesCombinedReductionTest {
    SampleModel sample = SampleModels.sample();

    @Test
    public void test_account() {
        SampleModelRule demoRule = when(matchNone(
                        accountCompany.anyMatch(DAILYMOTION, BLABLACAR),
                        accountCountry.anyMatch(UK, US))).validate();
        Result result = demoRule.withShortCircuit(false).executeOn(sample);
        assertThat(result).isTrue();
    }

    @Test
    public void test_account_KO() {
        sample.getAccount().setCompany(DAILYMOTION);
        SampleModelRule demoRule = when(matchNone(
                        accountCompany.anyMatch(DAILYMOTION, BLABLACAR),
                        accountCountry.anyMatch(UK, US))).validate();
        Result result = demoRule.withShortCircuit(false).executeOn(sample);
        System.out.println(result.getFailureCause());
        assertThat(result).isFalse();
    }

    @Test
    public void test_account_2() {
        SampleModelRule demoRule = when(matchNone(
                        accountCompany.eq(DAILYMOTION).or(accountCompany.eq(BLABLACAR)),
                        accountCountry.eq(UK).or(accountCountry.eq(US)))).validate();
        Result result = demoRule.withShortCircuit(false).executeOn(sample);
        assertThat(result).isTrue();
    }

    @Test
    public void test_account_2_KO() {
        sample.getAccount().setCompany(DAILYMOTION);
        SampleModelRule demoRule = when(matchNone(
                        accountCompany.eq(DAILYMOTION).or(accountCompany.eq(BLABLACAR)),
                        accountCountry.eq(UK).or(accountCountry.eq(US)))).validate();
        Result result = demoRule.withShortCircuit(false).executeOn(sample);
        System.out.println(result.getFailureCause());
        assertThat(result).isFalse();
    }

    @Test
    public void test_account_3() {
        SampleModelRule demoRule = when(not(accountCompany.eq(DAILYMOTION).or(accountCompany.eq(BLABLACAR))
                        .or(accountCountry.eq(UK).or(accountCountry.eq(US))))).validate();
        Result result = demoRule.withShortCircuit(false).executeOn(sample);
        assertThat(result).isTrue();
    }

    @Test
    public void test_account_3_KO() {
        sample.getAccount().setCompany(DAILYMOTION);
        SampleModelRule demoRule = when(not(accountCompany.eq(DAILYMOTION).or(accountCompany.eq(BLABLACAR))
                        .or(accountCountry.eq(UK).or(accountCountry.eq(US))))).validate();
        Result result = demoRule.withShortCircuit(false).executeOn(sample);
        System.out.println(result.getFailureCause());
        assertThat(result).isFalse();
    }

    @Test
    public void test_account_4() {
        SampleModelRule demoRule = when(matchAll(accountCompany.anyMatch(LES_FURETS, CANAL_PLUS, MEETIC, OODRIVE),
                        accountCountry.anyMatch(UK, US).not())).validate();
        Result result = demoRule.withShortCircuit(false).executeOn(sample);
        assertThat(result).isTrue();
    }

    @Test
    public void test_account_4_KO() {
        sample.getAccount().setCompany(DAILYMOTION);
        SampleModelRule demoRule = when(matchAll(accountCompany.anyMatch(LES_FURETS, CANAL_PLUS, MEETIC, OODRIVE),
                        accountCountry.anyMatch(UK, US).not())).validate();
        Result result = demoRule.withShortCircuit(false).executeOn(sample);
        System.out.println(result.getFailureCause());
        assertThat(result).isFalse();
    }

    @Test
    public void test_account_5() {
        SampleModelRule demoRule = when(matchAll(accountCompany.eq(LES_FURETS).or(accountCompany.eq(CANAL_PLUS)
                        .or(accountCompany.eq(MEETIC).or(accountCompany.eq(OODRIVE)))),
                        not(accountCountry.eq(UK).or(accountCountry.eq(US))))).validate();
        Result result = demoRule.withShortCircuit(false).executeOn(sample);
        assertThat(result).isTrue();
    }

    @Test
    public void test_account_5_KO() {
        sample.getAccount().setCompany(DAILYMOTION);
        SampleModelRule demoRule = when(matchAll(accountCompany.eq(LES_FURETS).or(accountCompany.eq(CANAL_PLUS)
                        .or(accountCompany.eq(MEETIC).or(accountCompany.eq(OODRIVE)))),
                        DOOV.not(accountCountry.eq(UK).or(accountCountry.eq(US))))).validate();
        Result result = demoRule.withShortCircuit(false).executeOn(sample);
        System.out.println(result.getFailureCause());
        assertThat(result).isFalse();
    }
}
