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
import static io.doov.sample.validation.Rules.REGISTRY_ACCOUNT;
import static io.doov.sample.validation.Rules.REGISTRY_USER;

import java.util.stream.Stream;

import org.junit.Test;

import io.doov.core.dsl.lang.RuleRegistry;
import io.doov.core.dsl.meta.ast.*;

public class RulesVisitorTest {

    @Test
    public void tree() {
        Stream.of(REGISTRY_ACCOUNT, REGISTRY_USER, REGISTRY_DEFAULT)
                        .flatMap(RuleRegistry::stream)
                        .peek(rule -> System.out.println("\n\n" + "- " + rule.toString() + "\n\n"))
                        .forEach(validationRule -> validationRule.accept(new SyntaxTreePrinter()));
    }

    @Test
    public void text() {
        StringBuilder text = new StringBuilder();
        Stream.of(REGISTRY_ACCOUNT, REGISTRY_USER, REGISTRY_DEFAULT)
                        .flatMap(RuleRegistry::stream)
                        .peek(rule -> text.append("\n\n").append("- ").append(rule.toString()).append("\n\n"))
                        .forEach(validationRule -> validationRule.accept(new SyntaxTreeTextBuilder(text)));
        System.out.println(text);
    }

    @Test
    public void markdown() {
        StringBuilder markdown = new StringBuilder();
        Stream.of(REGISTRY_ACCOUNT, REGISTRY_USER, REGISTRY_DEFAULT)
                        .flatMap(RuleRegistry::stream)
                        .peek(rule -> markdown.append("\n\n").append("# ").append(rule.toString()).append("\n\n"))
                        .forEach(validationRule -> validationRule.accept(new SyntaxTreeMarkdownBuilder(markdown)));
        System.out.println(markdown);
    }

}
