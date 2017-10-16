package org.modelmap;

import static org.modelmap.core.dsl.DSL.matchAll;
import static org.modelmap.sample.field.SampleFieldIdInfo.accountCountry;
import static org.modelmap.sample.field.SampleFieldIdInfo.accountLanguage;
import static org.modelmap.sample.field.SampleFieldIdInfo.accountPhoneNumber;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.modelmap.core.dsl.DSL;
import org.modelmap.core.dsl.lang.StepCondition;
import org.modelmap.core.dsl.lang.ValidationRule;
import org.modelmap.sample.model.*;
import org.modelmap.sample.validation.Registry;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.logic.BlackHole;

public class BenchmarkRule {

    private static final ValidationRule ACCOUNT_VALID_COUNTRY_20 = DSL.when(matchAll(conditions(20))).validate();
    private static final ValidationRule ACCOUNT_VALID_COUNTRY_40 = DSL.when(matchAll(conditions(40))).validate();
    private static final ValidationRule ACCOUNT_VALID_COUNTRY_60 = DSL.when(matchAll(conditions(60))).validate();
    private static final ValidationRule ACCOUNT_VALID_COUNTRY_80 = DSL.when(matchAll(conditions(80))).validate();
    private static final ValidationRule ACCOUNT_VALID_COUNTRY_100 = DSL.when(matchAll(conditions(100))).validate();

    @GenerateMicroBenchmark
    public void valid_email(BlackHole blackhole) {
        boolean valid = Registry.ACCOUNT_VALID_EMAIL.executeOn(SampleModels.wrapper()).isValid();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    @GenerateMicroBenchmark
    public void valid_country(BlackHole blackhole) {
        boolean valid = Registry.ACCOUNT_VALID_COUNTRY.executeOn(SampleModels.wrapper()).isValid();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    public void valid_country_20(BlackHole blackhole) {
        boolean valid = ACCOUNT_VALID_COUNTRY_20.executeOn(SampleModels.wrapper()).isValid();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    public void valid_country_40(BlackHole blackhole) {
        boolean valid = ACCOUNT_VALID_COUNTRY_40.executeOn(SampleModels.wrapper()).isValid();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    public void valid_country_60(BlackHole blackhole) {
        boolean valid = ACCOUNT_VALID_COUNTRY_60.executeOn(SampleModels.wrapper()).isValid();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    public void valid_country_80(BlackHole blackhole) {
        boolean valid = ACCOUNT_VALID_COUNTRY_80.executeOn(SampleModels.wrapper()).isValid();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    public void valid_country_100(BlackHole blackhole) {
        boolean valid = ACCOUNT_VALID_COUNTRY_100.executeOn(SampleModels.wrapper()).isValid();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    private static StepCondition[] conditions(int count) {
        return IntStream.range(0, count)
                        .mapToObj(operand -> accountCountry().eq(Country.FR)
                                        .and(accountLanguage().eq(Language.FR))
                                        .and(accountPhoneNumber().startsWith("+33")))
                        .collect(Collectors.toList())
                        .toArray(new StepCondition[] {});
    }

}
