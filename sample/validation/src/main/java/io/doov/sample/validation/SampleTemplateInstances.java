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

import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;
import static io.doov.sample.field.dsl.DslSampleModel.*;
import static io.doov.sample.validation.SampleTemplates.*;
import static java.nio.charset.Charset.defaultCharset;

import java.io.*;
import java.util.*;

import org.apache.commons.io.IOUtils;

import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.ast.*;

public class SampleTemplateInstances {

    public static final ValidationRule RULE_EMAIL = TEMPLATE_EMAIL.bind(accountEmail);

    public static final ValidationRule RULE_ACCOUNT = TEMPLATE_ACCOUNT
            .bind(userBirthdate, accountEmail, configurationMaxEmailSize, accountCountry, accountPhoneNumber);

    public static final ValidationRule RULE_ACCOUNT_2 = TEMPLATE_ACCOUNT_2
            .bind(userBirthdate, accountEmail, configurationMaxEmailSize, accountCountry, accountPhoneNumber);

    public static final ValidationRule RULE_USER = TEMPLATE_USER.bind(userFirstName, userLastName)
            .withShortCircuit(false);

    public static final ValidationRule RULE_USER_2 = TEMPLATE_USER_2
            .bind(accountPhoneNumber, accountPhoneNumber, accountEmail);

    public static final ValidationRule RULE_USER_ADULT = TEMPLATE_USER_ADULT.bind(userBirthdate, accountCreationDate);

    public static final ValidationRule RULE_USER_ADULT_FIRSTDAY = TEMPLATE_USER_ADULT_FIRSTDAY
            .bind(userBirthdate, accountCreationDate);

    public static final ValidationRule RULE_FIRST_NAME = TEMPLATE_FIRST_NAME.bind(userFirstName);

    public static final ValidationRule RULE_ID = TEMPLATE_ID.bind(userId);

    public static final ValidationRule RULE_AGE = TEMPLATE_AGE.bind(userBirthdate);

    public static final ValidationRule RULE_AGE_2 = TEMPLATE_AGE_2.bind(userBirthdate);

    public static final ValidationRule RULE_SUM = TEMPLATE_SUM.bind(configurationMinAge, configurationMaxEmailSize);

    public static final ValidationRule RULE_MIN = TEMPLATE_MIN.bind(configurationMinAge, configurationMaxEmailSize);

    public static final ValidationRule RULE_DOUBLE_LAMBDA = TEMPLATE_DOUBLE_LAMBDA.bind(favoriteSiteName1);

    public static final ValidationRule RULE_BORN_1980 = TEMPLATE_BORN_1980.bind(userBirthdate);

    public static final ValidationRule RULE_ACCOUNT_TIME_CONTAINS = TEMPLATE_ACCOUNT_TIME_CONTAINS
            .bind(accountTimezone);

    public static final ValidationRule RULE_COMPANY_NOT_BLABLA = TEMPLATE_COMPANY_NOT_BLABLA.bind(accountCompany);

    public static List<ValidationRule> rules() {
        return Arrays.asList(
                RULE_EMAIL,
                RULE_ACCOUNT,
                RULE_ACCOUNT_2,
                RULE_USER,
                RULE_USER_2,
                RULE_USER_ADULT,
                RULE_USER_ADULT_FIRSTDAY,
                RULE_FIRST_NAME,
                RULE_ID,
                RULE_AGE,
                RULE_AGE_2,
                RULE_SUM,
                RULE_MIN,
                RULE_DOUBLE_LAMBDA,
                RULE_BORN_1980,
                RULE_ACCOUNT_TIME_CONTAINS,
                RULE_COMPANY_NOT_BLABLA);
    }

    public static void main(String[] args) throws IOException {
        final File output = new File("validation_template_instances.html");
        try (FileOutputStream fos = new FileOutputStream(output)) {
            IOUtils.write("<html><head><meta charset=\"UTF-8\"/><style>"
                    + IOUtils.toString(AstVisitorUtils.class.getResourceAsStream("rules.css"), defaultCharset())
                    + "</style></head><body>", fos, defaultCharset());
            IOUtils.write("<div style='width:1024px; margin-left:20px;'>", fos, defaultCharset());
            for (ValidationRule r : rules()) {
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
