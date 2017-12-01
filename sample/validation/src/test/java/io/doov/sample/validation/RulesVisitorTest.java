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
package io.doov.sample.validation;

import static io.doov.core.dsl.impl.DefaultRuleRegistry.REGISTRY_DEFAULT;
import static io.doov.sample.field.SampleFieldIdInfo.userBirthdate;
import static io.doov.sample.field.SampleFieldIdInfo.userFirstName;
import static io.doov.sample.validation.Rules.REGISTRY_ACCOUNT;
import static io.doov.sample.validation.Rules.REGISTRY_USER;
import static java.util.Arrays.asList;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.Ignore;
import org.junit.Test;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.impl.TemporalCondition;
import io.doov.core.dsl.lang.RuleRegistry;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.ast.*;

public class RulesVisitorTest {

    @Test
    public void full() {
        StringBuilder sb = new StringBuilder();
        Stream.of(REGISTRY_ACCOUNT, REGISTRY_USER, REGISTRY_DEFAULT)
                        .flatMap(RuleRegistry::stream)
                        .peek(rule -> sb.append("--------------------------------").append("\n"))
                        .forEach(rule -> rule.accept(new AstFullVisitor(sb)));
        System.out.println(sb);
    }

    @Test
    public void line() {
        StringBuilder sb = new StringBuilder();
        Stream.of(REGISTRY_ACCOUNT, REGISTRY_USER, REGISTRY_DEFAULT)
                        .flatMap(RuleRegistry::stream)
                        .peek(rule -> sb.append("--------------------------------").append("\n"))
                        .forEach(rule -> rule.accept(new AstLineVisitor(sb)));
        System.out.println(sb);
    }

    @Test
    public void text() {
        StringBuilder sb = new StringBuilder();
        Stream.of(REGISTRY_ACCOUNT, REGISTRY_USER, REGISTRY_DEFAULT)
                        .flatMap(RuleRegistry::stream)
                        .peek(rule -> sb.append("--------------------------------").append("\n"))
                        .forEach(rule -> rule.accept(new AstTextVisitor(sb)));
        System.out.println(sb);
    }

    @Test
    public void markdown() {
        StringBuilder sb = new StringBuilder();
        Stream.of(REGISTRY_ACCOUNT, REGISTRY_USER, REGISTRY_DEFAULT)
                        .flatMap(RuleRegistry::stream)
                        .peek(rule -> sb.append("--------------------------------").append("\n"))
                        .forEach(rule -> rule.accept(new AstMarkdownVisitor(sb)));
        System.out.println(sb);
    }

    @Test
    public void html() {
        StringBuilder sb = new StringBuilder();
        Stream.of(REGISTRY_ACCOUNT, REGISTRY_USER, REGISTRY_DEFAULT)
                        .flatMap(RuleRegistry::stream)
                        .peek(rule -> sb.append("--------------------------------").append("\n"))
                        .forEach(rule -> rule.accept(new AstHtmlVisitor(sb)));

        System.out.println(sb);

        String[] split = sb.toString().split("--------------------------------");
        String path = System.getProperty("user.home") + "/Desktop/testHTML";
        for (int i = 1; i < split.length; i++) {
            String s = split[i];
            try {
                FileWriter fileWriter = new FileWriter(path + "/test" + i);
                fileWriter.write("<html><body>");
                fileWriter.write("<head>\n" +
                                "        <meta charset=\"utf-8\" />\n" +
                                "<link href=\"https://fonts.googleapis.com/css?family=Source+Code+Pro\" " +
                                "rel=\"stylesheet\">" +
                                "        <link rel=\"stylesheet\" href=\"style.css\" />\n" +
                                "    </head>\n");
                fileWriter.write(s);
                fileWriter.write("</body></html>");
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Test
    @Ignore
    public void oneHtml() {
        StringBuilder sb = new StringBuilder();
        ValidationRule vr = DOOV
                        .when(DOOV.count(
                                        userFirstName().isNotNull(),
                                        userFirstName().isNotNull(),
                                        userFirstName().isNotNull()).greaterOrEquals(3))
                        .validate();

        ValidationRule vr1 = DOOV
                        .when(userFirstName().isNotNull().and(userFirstName().isNotNull()
                                        .or(userFirstName().isNotNull().and(userFirstName().isNotNull())))
                                        .and(userFirstName().isNotNull()))
                        .validate();

        ValidationRule vr2 = DOOV.when(userBirthdate().after(LocalDate::now)).validate();

        vr2.accept(new AstHtmlVisitor(sb));
        System.out.println(sb);
    }
}
