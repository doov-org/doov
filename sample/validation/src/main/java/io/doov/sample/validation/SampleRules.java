/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package io.doov.sample.validation;

import static io.doov.core.dsl.DOOV.count;
import static io.doov.core.dsl.DOOV.matchAll;
import static io.doov.core.dsl.DOOV.min;
import static io.doov.core.dsl.DOOV.sum;
import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;
import static io.doov.core.dsl.time.LocalDateSuppliers.today;
import static io.doov.core.dsl.time.TemporalAdjuster.firstDayOfYear;
import static io.doov.sample.field.dsl.DslSampleModel.*;
import static java.nio.charset.Charset.defaultCharset;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.IOUtils;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.impl.DefaultRuleRegistry;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.ast.AstHtmlVisitor;
import io.doov.core.dsl.meta.ast.AstVisitorUtils;
import io.doov.sample.field.dsl.DslSampleModel;
import io.doov.sample.model.Country;
import io.doov.sample.model.Timezone;

public class SampleRules extends DefaultRuleRegistry {

    public static final ValidationRule RULE_EMAIL = DOOV
                    .when(accountEmail.matches("\\w+[@]\\w+\\.com")
                                    .or(accountEmail.matches("\\w+[@]\\w+\\.fr")))
                    .validate()
                    .registerOn(REGISTRY_DEFAULT);

    public static final ValidationRule RULE_ACCOUNT = DOOV
                    .when(matchAll(
                                    userBirthdate.ageAt(today()).greaterOrEquals(18),
                                    accountEmail.length().lesserOrEquals(configurationMaxEmailSize),
                                    accountCountry.eq(Country.FR).and(accountPhoneNumber.startsWith("+33"))))
                    .validate()
                    .registerOn(REGISTRY_DEFAULT);

    public static final ValidationRule RULE_ACCOUNT_2 = DOOV
                    .when(userBirthdate.ageAt(today()).greaterOrEquals(18)
                                    .and(accountEmail.length().lesserOrEquals(configurationMaxEmailSize))
                                    .and(accountCountry.eq(Country.FR))
                                    .and(accountPhoneNumber.startsWith("+33")))
                    .validate()
                    .registerOn(REGISTRY_DEFAULT);

    public static final ValidationRule RULE_USER = DOOV
                    .when(count(userFirstName.isNotNull(),
                                    userLastName.isNotNull().and(userLastName.matches("[A-Z]+")))
                                                    .greaterOrEquals(0))
                    .validate()
                    .withShortCircuit(false)
                    .registerOn(REGISTRY_DEFAULT);

    public static final ValidationRule RULE_USER_2 = DOOV
                    .when(userLastName.isNotNull().and(userLastName.matches("[A-Z]+")
                                    .and(count(accountPhoneNumber.isNotNull(),
                                                    accountEmail.isNotNull())
                                                                    .greaterThan(0))))
                    .validate()
                    .registerOn(REGISTRY_DEFAULT);

    public static final ValidationRule RULE_USER_ADULT = DOOV
                    .when(userBirthdate.ageAt(accountCreationDate).greaterOrEquals(18))
                    .validate()
                    .registerOn(REGISTRY_DEFAULT);

    public static final ValidationRule RULE_USER_ADULT_FIRSTDAY = DOOV
                    .when(userBirthdate.ageAt(accountCreationDate.with(firstDayOfYear())).greaterOrEquals(18))
                    .validate()
                    .registerOn(REGISTRY_DEFAULT);

    public static final ValidationRule RULE_FIRST_NAME = DOOV
                    .when(matchAll(userFirstName.mapToInt(name -> 1).eq(1)))
                    .validate()
                    .registerOn(REGISTRY_DEFAULT);

    public static final ValidationRule RULE_ID = DOOV
                    .when(userId.isNotNull())
                    .validate()
                    .registerOn(REGISTRY_DEFAULT);

    public static final ValidationRule RULE_AGE = DOOV
                    .when(userBirthdate.ageAt(today()).greaterOrEquals(18))
                    .validate()
                    .registerOn(REGISTRY_DEFAULT);

    public static final ValidationRule RULE_AGE_2 = DOOV
                    .when(userBirthdate.after(userBirthdate.minus(1, DAYS)))
                    .validate()
                    .registerOn(REGISTRY_DEFAULT);

    public static final ValidationRule RULE_SUM = DOOV
                    .when(sum(configurationMinAge.times(0), configurationMaxEmailSize.times(1)).greaterOrEquals(0))
                    .validate()
                    .registerOn(REGISTRY_DEFAULT);

    public static final ValidationRule RULE_MIN = DOOV
                    .when(min(configurationMinAge, configurationMaxEmailSize).greaterOrEquals(0))
                    .validate()
                    .registerOn(REGISTRY_DEFAULT);

    public static final ValidationRule RULE_DOUBLE_LAMBDA = DOOV
                    .when(favoriteSiteName1.anyMatch(s -> !s.contains("dunno")))
                    .validate()
                    .registerOn(REGISTRY_DEFAULT);

    public static final ValidationRule RULE_BORN_1980 = DOOV
                    .when(userBirthdate.mapToInt(LocalDate::getYear).eq(1980))
                    .validate()
                    .registerOn(REGISTRY_DEFAULT);

    public static final ValidationRule RULE_BORN = DOOV
                    .when(accountTimezone.mapToString(Timezone::getDescription).contains("00:00"))
                    .validate()
                    .registerOn(REGISTRY_DEFAULT);

    public static void main(String[] args) throws FileNotFoundException, IOException {
        final File output = new File("validation_rule.html");
        final List<ValidationRule> rules = REGISTRY_DEFAULT.stream().collect(toList());
        try (FileOutputStream fos = new FileOutputStream(output)) {
            IOUtils.write("<html><head><meta charset=\"UTF-8\"><style>"
                            + IOUtils.toString(AstVisitorUtils.class.getResourceAsStream("rules.css"), defaultCharset())
                            + "</style></head><body>", fos, defaultCharset());
            IOUtils.write("<div style='width:1024px; margin-left:20px;'>", fos, defaultCharset());;
            for (ValidationRule r : rules) {
                r.accept(new AstHtmlVisitor(fos, BUNDLE, Locale.FRANCE), 0);
                IOUtils.write("<hr/>", fos, Charset.defaultCharset());
            }
            IOUtils.write("</div>", fos, Charset.defaultCharset());
            IOUtils.write("</body></html>", fos, Charset.defaultCharset());
        }
        System.out.println("written " + output.getAbsolutePath());
        System.exit(1);
    }
}
