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

import static io.doov.core.dsl.meta.ast.AstVisitorUtils.toHtml;
import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Locale.ENGLISH;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.ast.*;
import io.doov.sample.validation.SampleRules;

public class RulesVisitorTest {

  private static final List<ValidationRule> rules = SampleRules.rules();

  @Test
  public void print_full_syntax_tree() {
    StringBuilder sb = new StringBuilder();
    rules.stream()
        .peek(rule -> sb.append("--------------------------------").append("\n"))
        .forEach(rule -> new AstFullVisitor(sb).browse(rule.metadata(), 0));
    System.out.println(sb.toString());
  }

  @Test
  public void print_line_syntax_tree() {
    StringBuilder sb = new StringBuilder();
    rules.stream()
        .peek(rule -> sb.append("--------------------------------").append("\n"))
        .forEach(rule -> new AstLineVisitor(sb, BUNDLE, ENGLISH).browse(rule.metadata(), 0));
    System.out.println(sb.toString());
  }

  @Test
  public void print_text_syntax_tree() {
    StringBuilder sb = new StringBuilder();
    rules.stream()
        .peek(rule -> sb.append("--------------------------------").append("\n"))
        .forEach(rule -> new AstTextVisitor(sb, BUNDLE, ENGLISH).browse(rule.metadata(), 0));
    System.out.println(sb.toString());
  }

  @Test
  public void print_markdown_syntax_tree() {
    StringBuilder sb = new StringBuilder();
    rules.stream()
        .peek(rule -> sb.append("--------------------------------").append("\n"))
        .forEach(rule -> new AstMarkdownRenderer(sb, BUNDLE, ENGLISH).toMarkdown(rule.metadata()));
    System.out.println(sb.toString());
  }

  @Test
  public void print_html_syntax_tree() {
    ByteArrayOutputStream ops = new ByteArrayOutputStream();
    rules.stream()
        .peek(rule ->
        {
          try {
            ops.write("--------------------------------\n".getBytes());
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        })
        .forEach(rule -> System.out.println(toHtml(rule.metadata(), ENGLISH)));
    System.out.println(new String(ops.toByteArray(), UTF_8));
  }

}
