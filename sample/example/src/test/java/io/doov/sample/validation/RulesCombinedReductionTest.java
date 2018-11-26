package io.doov.sample.validation;

import static io.doov.assertions.Assertions.assertThat;
import static io.doov.core.dsl.DOOV.matchAll;
import static io.doov.core.dsl.DOOV.matchNone;
import static io.doov.core.dsl.DOOV.not;
import static io.doov.core.dsl.lang.ReduceType.FAILURE;
import static io.doov.core.dsl.lang.ReduceType.SUCCESS;
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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.lang.Result;
import io.doov.sample.field.dsl.DslSampleModel.SampleModelRule;
import io.doov.sample.model.SampleModel;
import io.doov.sample.model.SampleModels;

public class RulesCombinedReductionTest {
    private SampleModelRule rule;
    private final SampleModel sample = SampleModels.sample();
    private Result result;

    @Test
    public void test_account() {
        rule = when(matchNone(
                accountCompany.anyMatch(DAILYMOTION, BLABLACAR),
                accountCountry.anyMatch(UK, US))).validate();

        result = rule.withShortCircuit(false).executeOn(sample);
        assertThat(result).isTrue();
    }

    @Test
    public void test_account_KO() {
        rule = when(matchNone(
                accountCompany.anyMatch(DAILYMOTION, BLABLACAR),
                accountCountry.anyMatch(UK, US))).validate();

        sample.getAccount().setCompany(DAILYMOTION);
        result = rule.withShortCircuit(false).executeOn(sample);
        assertThat(result).isFalse();
    }

    @Test
    public void test_account_2() {
        rule = when(matchNone(
                accountCompany.eq(DAILYMOTION).or(accountCompany.eq(BLABLACAR)),
                accountCountry.eq(UK).or(accountCountry.eq(US)))).validate();

        result = rule.withShortCircuit(false).executeOn(sample);
        assertThat(result).isTrue();
    }

    @Test
    public void test_account_2_KO() {
        rule = when(matchNone(
                accountCompany.eq(DAILYMOTION).or(accountCompany.eq(BLABLACAR)),
                accountCountry.eq(UK).or(accountCountry.eq(US)))).validate();

        sample.getAccount().setCompany(DAILYMOTION);
        result = rule.withShortCircuit(false).executeOn(sample);
        assertThat(result).isFalse();
    }

    @Test
    public void test_account_3() {
        rule = when(not(accountCompany.eq(DAILYMOTION).or(accountCompany.eq(BLABLACAR))
                .or(accountCountry.eq(UK).or(accountCountry.eq(US))))).validate();

        result = rule.withShortCircuit(false).executeOn(sample);
        assertThat(result).isTrue();
    }

    @Test
    public void test_account_3_KO() {
        sample.getAccount().setCompany(DAILYMOTION);
        rule = when(not(accountCompany.eq(DAILYMOTION).or(accountCompany.eq(BLABLACAR))
                .or(accountCountry.eq(UK).or(accountCountry.eq(US))))).validate();

        result = rule.withShortCircuit(false).executeOn(sample);
        assertThat(result).isFalse();
    }

    @Test
    public void test_account_4() {
        rule = when(matchAll(accountCompany.anyMatch(LES_FURETS, CANAL_PLUS, MEETIC, OODRIVE),
                accountCountry.anyMatch(UK, US).not())).validate();

        result = rule.withShortCircuit(false).executeOn(sample);
        assertThat(result).isTrue();
    }

    @Test
    public void test_account_4_KO() {
        rule = when(matchAll(accountCompany.anyMatch(LES_FURETS, CANAL_PLUS, MEETIC, OODRIVE),
                accountCountry.anyMatch(UK, US).not())).validate();

        sample.getAccount().setCompany(DAILYMOTION);
        result = rule.withShortCircuit(false).executeOn(sample);
        assertThat(result).isFalse();
    }

    @Test
    public void test_account_5() {
        rule = when(matchAll(accountCompany.eq(LES_FURETS).or(accountCompany.eq(CANAL_PLUS)
                .or(accountCompany.eq(MEETIC).or(accountCompany.eq(OODRIVE)))),
                not(accountCountry.eq(UK).or(accountCountry.eq(US))))).validate();

        result = rule.withShortCircuit(false).executeOn(sample);
        assertThat(result).isTrue();
    }

    @Test
    public void test_account_5_KO() {
        rule = when(matchAll(accountCompany.eq(LES_FURETS).or(accountCompany.eq(CANAL_PLUS)
                .or(accountCompany.eq(MEETIC).or(accountCompany.eq(OODRIVE)))),
                DOOV.not(accountCountry.eq(UK).or(accountCountry.eq(US))))).validate();

        sample.getAccount().setCompany(DAILYMOTION);
        result = rule.withShortCircuit(false).executeOn(sample);
        assertThat(result).isFalse();
    }

    @AfterEach
    void afterEach() {
        System.out.println(rule + " is " + result.value());
        System.out.println("SUCCESS> " + result.reduce(SUCCESS));
        System.out.println("FAILURE> " + result.reduce(FAILURE));
    }
}
