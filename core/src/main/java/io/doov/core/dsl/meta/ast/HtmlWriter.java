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
package io.doov.core.dsl.meta.ast;

import java.util.ArrayDeque;
import java.util.Locale;

import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.Operator;

public interface HtmlWriter {
    String SPACE = "&nbsp;";
    String APOS = "&apos;";
    String BR = "<br>";
    String CSS_VALIDATION_RULE = "dsl-validation-rule";
    String CSS_VALIDATE = "dsl-token-validate";
    String CSS_NARY = "dsl-token-nary";
    String CSS_WHEN = "dsl-token-when";
    String CSS_SINGLE_MAPPING = "dsl-token-single-mapping";
    String CSS_OPERATOR = "dsl-token-operator";
    String CSS_VALUE = "dsl-token-value";
    String CSS_FIELD = "dsl-token-field";
    String CSS_UNKNOWN = "dsl-token-unknown";

    String CSS_LI_LEAF = "dsl-li-leaf";
    String CSS_LI_BINARY = "dsl-li-binary";
    String CSS_LI_UNARY = "dsl-li-unary";
    String CSS_LI_NARY = "dsl-li-nary";

    String CSS_UL_WHEN = "dsl-ul-when";
    String CSS_UL_BINARY = "dsl-ul-binary";
    String CSS_UL_UNARY = "dsl-ul-unary";

    String CSS_OL_NARY = "dsl-ol-nary";

    Locale getLocale();

    void write(String value);

    void writeFromBundle(Operator operator);

    void writeFromBundle(String key);

    void writeBeginLi(String... classes);

    void writeEndLi();

    void writeBeginUl(String... classes);

    void writeEndUl();

    void writeBeginOl(String... classes);

    void writeEndOl();

    void writeBeginDiv(String... classes);

    void writeBeginSpan(String... classes);

    void writeBeginDivWithStyle(String style, String... classes);

    void writeEndDiv();

    void writeEndSpan();

    void writeExclusionBar(Metadata metadata, ArrayDeque<Metadata> parents);

    String escapeHtml4(String readable);
}
