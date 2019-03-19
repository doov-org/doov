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

import static io.doov.core.dsl.DOOV.*;
import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;
import static io.doov.core.dsl.template.ParameterTypes.*;
import static io.doov.core.dsl.time.LocalDateSuppliers.today;
import static io.doov.core.dsl.time.TemporalAdjuster.firstDayOfYear;
import static io.doov.sample.model.Company.BLABLACAR;
import static java.nio.charset.Charset.defaultCharset;
import static java.time.temporal.ChronoUnit.DAYS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.IOUtils;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.field.types.*;
import io.doov.core.dsl.lang.DSLBuilder;
import io.doov.core.dsl.meta.ast.*;
import io.doov.core.dsl.template.TemplateRule.Rule1;
import io.doov.core.dsl.template.TemplateRule.Rule2;
import io.doov.core.dsl.template.TemplateRule.Rule3;
import io.doov.core.dsl.template.TemplateRule.Rule5;
import io.doov.sample.model.Company;
import io.doov.sample.model.Country;
import io.doov.sample.model.Timezone;

public class SampleTemplates {

    public static final Rule1<StringFieldInfo> TEMPLATE_EMAIL = DOOV
            .template($String)
            .rule(email -> email.matches("\\w+[@]\\w+\\.com")
                    .or(email.matches("\\w+[@]\\w+\\.fr")));

    public static final Rule5<LocalDateFieldInfo, StringFieldInfo, IntegerFieldInfo, EnumFieldInfo<Country>, StringFieldInfo> TEMPLATE_ACCOUNT = DOOV
            .template($LocalDate, $String, $Integer, $Enum(Country.class), $String)
            .rule((birthdate, email, maxEmailSize, country, phoneNumber) -> matchAll(
                    birthdate.ageAt(today()).greaterOrEquals(18),
                    email.length().lesserOrEquals(maxEmailSize),
                    country.eq(Country.FR).and(phoneNumber.startsWith("+33"))));

    public static final Rule5<LocalDateFieldInfo, StringFieldInfo, IntegerFieldInfo, EnumFieldInfo<Country>, StringFieldInfo> TEMPLATE_ACCOUNT_2 = DOOV
            .template($LocalDate, $String, $Integer, $Enum(Country.class), $String)
            .rule((birthdate, email, maxEmailSize, country, phoneNumber) -> birthdate.ageAt(today())
                    .greaterOrEquals(18)
                    .and(email.length().lesserOrEquals(maxEmailSize))
                    .and(country.eq(Country.FR))
                    .and(phoneNumber.startsWith("+33")));

    public static final Rule2<StringFieldInfo, StringFieldInfo> TEMPLATE_USER = DOOV
            .template($String, $String)
            .rule((firstName, lastName) -> count(firstName.isNotNull(),
                    lastName.isNotNull().and(lastName.matches("[A-Z]+"))).greaterOrEquals(0));

    public static final Rule3<StringFieldInfo, StringFieldInfo, StringFieldInfo> TEMPLATE_USER_2 = DOOV
            .template($String, $String, $String)
            .rule((userLastName, accountPhoneNumber, accountEmail) -> userLastName.isNotNull()
                    .and(userLastName.matches("[A-Z]+").and(count(accountPhoneNumber.isNotNull(),
                            accountEmail.isNotNull()).greaterThan(0))));

    public static final Rule2<LocalDateFieldInfo, LocalDateFieldInfo> TEMPLATE_USER_ADULT = template($LocalDate,
            $LocalDate)
                    .rule((birthdate, creationDate) -> birthdate.ageAt(creationDate).greaterOrEquals(18));

    public static final Rule2<LocalDateFieldInfo, LocalDateFieldInfo> TEMPLATE_USER_ADULT_FIRSTDAY = template(
            $LocalDate,
            $LocalDate)
                    .rule((birthdate, creationDate) -> birthdate.ageAt(creationDate.with(firstDayOfYear()))
                            .greaterOrEquals(18));

    public static final Rule1<StringFieldInfo> TEMPLATE_FIRST_NAME = template($String)
            .rule((firstName) -> matchAll(firstName.mapToInt(name -> 1).eq(1)));

    public static final Rule1<LongFieldInfo> TEMPLATE_ID = template($Long).rule((userId) -> userId.isNotNull());

    public static final Rule1<LocalDateFieldInfo> TEMPLATE_AGE = template($LocalDate)
            .rule((birthdate) -> birthdate.ageAt(today()).greaterOrEquals(18));

    public static final Rule1<LocalDateFieldInfo> TEMPLATE_AGE_2 = template($LocalDate)
            .rule((birthdate) -> birthdate.after(birthdate.minus(1, DAYS)));

    public static final Rule2<IntegerFieldInfo, IntegerFieldInfo> TEMPLATE_SUM = template($Integer, $Integer)
            .rule((minAge, maxEmailSize) -> sum(minAge.times(0), maxEmailSize.times(1)).greaterOrEquals(0));

    public static final Rule2<IntegerFieldInfo, IntegerFieldInfo> TEMPLATE_MIN = template($Integer, $Integer)
            .rule((minAge, maxEmailSize) -> min(minAge, maxEmailSize).greaterOrEquals(0));

    public static final Rule1<StringFieldInfo> TEMPLATE_DOUBLE_LAMBDA = template($String)
            .rule((siteName) -> siteName.anyMatch(s -> !s.contains("dunno")));

    public static final Rule1<LocalDateFieldInfo> TEMPLATE_BORN_1980 = template($LocalDate)
            .rule((birthdate) -> birthdate.mapToInt(LocalDate::getYear).eq(1980));

    public static final Rule1<EnumFieldInfo<Timezone>> TEMPLATE_ACCOUNT_TIME_CONTAINS = template($Enum(Timezone.class))
            .rule((timezone) -> timezone.mapToString(Timezone::getDescription).contains("00:00"));

    public static final Rule1<EnumFieldInfo<Company>> TEMPLATE_COMPANY_NOT_BLABLA = template($Enum(Company.class))
            .rule((company) -> company.eq(BLABLACAR).not());

    public static List<DSLBuilder> rules() {
        return Arrays.asList(
                TEMPLATE_EMAIL,
                TEMPLATE_ACCOUNT,
                TEMPLATE_ACCOUNT_2,
                TEMPLATE_USER,
                TEMPLATE_USER_2,
                TEMPLATE_USER_ADULT,
                TEMPLATE_USER_ADULT_FIRSTDAY,
                TEMPLATE_FIRST_NAME,
                TEMPLATE_ID,
                TEMPLATE_AGE,
                TEMPLATE_AGE_2,
                TEMPLATE_SUM,
                TEMPLATE_MIN,
                TEMPLATE_DOUBLE_LAMBDA,
                TEMPLATE_BORN_1980,
                TEMPLATE_ACCOUNT_TIME_CONTAINS,
                TEMPLATE_COMPANY_NOT_BLABLA);
    }

    public static void main(String[] args) throws IOException {
        final File output = new File("validation_template.html");
        try (FileOutputStream fos = new FileOutputStream(output)) {
            IOUtils.write("<html><head><meta charset=\"UTF-8\"/><style>"
                    + IOUtils.toString(AstVisitorUtils.class.getResourceAsStream("rules.css"), defaultCharset())
                    + "</style></head><body>", fos, defaultCharset());
            IOUtils.write("<div style='width:1024px; margin-left:20px;'>", fos, defaultCharset());
            for (DSLBuilder r : rules()) {
                IOUtils.write("<div>", fos, defaultCharset());
                IOUtils.write(r.readable(Locale.FRANCE), fos, defaultCharset());
                IOUtils.write("</div>", fos, defaultCharset());
                new AstHtmlRenderer(new DefaultHtmlWriter(Locale.FRANCE, fos, BUNDLE)).toHtml(r.metadata());
                IOUtils.write("<hr/>", fos, defaultCharset());
            }
            IOUtils.write("</div>", fos, defaultCharset());
            IOUtils.write("</body></html>", fos, defaultCharset());
        }
        System.out.println("written " + output.getAbsolutePath());
        System.exit(1);
    }
}