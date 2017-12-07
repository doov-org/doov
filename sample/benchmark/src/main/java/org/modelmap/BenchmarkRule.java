/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.modelmap;

import static io.doov.core.dsl.DOOV.matchAll;
import static io.doov.core.dsl.time.LocalDateSuppliers.today;
import static io.doov.sample.field.SampleFieldIdInfo.*;
import static java.util.stream.Collectors.toList;

import java.util.stream.IntStream;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.logic.BlackHole;

import io.doov.core.FieldModel;
import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.sample.model.*;

public class BenchmarkRule {

    private static final FieldModel MODEL = SampleModels.wrapper();

    private static final ValidationRule EMAIL = DOOV
            .when(accountEmail().matches("\\w+[@]\\w+\\.com")
                    .or(accountEmail().matches("\\w+[@]\\w+\\.fr")))
            .validate();

    private static final ValidationRule COUNTY = DOOV
            .when(userBirthdate().ageAt(today()).greaterOrEquals(18)
                    .and(accountEmail().length().lesserOrEquals(configurationMaxEmailSize()))
                    .and(accountCountry().eq(Country.FR))
                    .and(accountPhoneNumber().startsWith("+33")))
            .validate();

    private static final ValidationRule ACCOUNT_VALID_COUNTRY_20 = DOOV.when(matchAll(conditions(20))).validate();
    private static final ValidationRule ACCOUNT_VALID_COUNTRY_40 = DOOV.when(matchAll(conditions(40))).validate();
    private static final ValidationRule ACCOUNT_VALID_COUNTRY_60 = DOOV.when(matchAll(conditions(60))).validate();
    private static final ValidationRule ACCOUNT_VALID_COUNTRY_80 = DOOV.when(matchAll(conditions(80))).validate();
    private static final ValidationRule ACCOUNT_VALID_COUNTRY_100 = DOOV.when(matchAll(conditions(100))).validate();

    @GenerateMicroBenchmark
    public void valid_email(BlackHole blackhole) {
        boolean valid = EMAIL.executeOn(MODEL).isTrue();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    @GenerateMicroBenchmark
    public void valid_country(BlackHole blackhole) {
        boolean valid = COUNTY.executeOn(MODEL).isTrue();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    @SuppressWarnings("unused")
    public void valid_country_20(BlackHole blackhole) {
        boolean valid = ACCOUNT_VALID_COUNTRY_20.executeOn(MODEL).isTrue();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    @SuppressWarnings("unused")
    public void valid_country_40(BlackHole blackhole) {
        boolean valid = ACCOUNT_VALID_COUNTRY_40.executeOn(MODEL).isTrue();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    @SuppressWarnings("unused")
    public void valid_country_60(BlackHole blackhole) {
        boolean valid = ACCOUNT_VALID_COUNTRY_60.executeOn(MODEL).isTrue();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    @SuppressWarnings("unused")
    public void valid_country_80(BlackHole blackhole) {
        boolean valid = ACCOUNT_VALID_COUNTRY_80.executeOn(MODEL).isTrue();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    @SuppressWarnings("unused")
    public void valid_country_100(BlackHole blackhole) {
        boolean valid = ACCOUNT_VALID_COUNTRY_100.executeOn(MODEL).isTrue();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    private static StepCondition[] conditions(int count) {
        return IntStream.range(0, count)
                .mapToObj(operand -> accountCountry().eq(Country.FR)
                        .and(accountLanguage().eq(Language.FR))
                        .and(accountPhoneNumber().startsWith("+33")))
                .collect(toList())
                .toArray(new StepCondition[] {});
    }

}
