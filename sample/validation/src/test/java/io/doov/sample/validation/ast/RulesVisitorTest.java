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
package io.doov.sample.validation.ast;

import static io.doov.core.dsl.impl.DefaultRuleRegistry.REGISTRY_DEFAULT;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import io.doov.core.dsl.meta.ast.*;

public class RulesVisitorTest {

    @Test
    public void print_full_syntax_tree() {
        StringBuilder sb = new StringBuilder();
        REGISTRY_DEFAULT.stream()
                        .peek(rule -> sb.append("--------------------------------").append("\n"))
                        .forEach(rule -> rule.accept(new AstFullVisitor(sb)));
        print(sb.toString());
    }

    @Test
    public void print_line_syntax_tree() {
        StringBuilder sb = new StringBuilder();
        REGISTRY_DEFAULT.stream()
                        .peek(rule -> sb.append("--------------------------------").append("\n"))
                        .forEach(rule -> rule.accept(new AstLineVisitor(sb, Locale.ENGLISH)));
        print(sb.toString());
    }

    @Test
    public void print_text_syntax_tree() {
        StringBuilder sb = new StringBuilder();
        REGISTRY_DEFAULT.stream()
                        .peek(rule -> sb.append("--------------------------------").append("\n"))
                        .forEach(rule -> rule.accept(new AstTextVisitor(sb, Locale.ENGLISH)));
        print(sb.toString());
    }

    @Test
    public void print_markdown_syntax_tree() {
        StringBuilder sb = new StringBuilder();
        REGISTRY_DEFAULT.stream()
                        .peek(rule -> sb.append("--------------------------------").append("\n"))
                        .forEach(rule -> rule.accept(new AstMarkdownVisitor(sb, Locale.ENGLISH)));
        print(sb.toString());
    }

    @Test
    public void print_html_syntax_tree() {
        ByteArrayOutputStream ops = new ByteArrayOutputStream();
        REGISTRY_DEFAULT.stream()
                        .peek(rule -> {
                            try {
                                ops.write("--------------------------------\n".getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        })
                        .forEach(rule -> rule.accept(new AstHtmlVisitor(ops, Locale.ENGLISH)));
        print(new String(ops.toByteArray(), Charset.forName("UTF-8")));
    }

    private static void print(String string) {
        if (System.getProperty("activateSystemOut") != null) {
            System.out.println(string);
        }
    }

}
