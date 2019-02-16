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

import static io.doov.core.dsl.meta.DefaultOperator.and;
import static io.doov.core.dsl.meta.DefaultOperator.count;
import static io.doov.core.dsl.meta.DefaultOperator.match_all;
import static io.doov.core.dsl.meta.DefaultOperator.match_any;
import static io.doov.core.dsl.meta.DefaultOperator.match_none;
import static io.doov.core.dsl.meta.DefaultOperator.or;
import static io.doov.core.dsl.meta.DefaultOperator.when;
import static io.doov.core.dsl.meta.ReturnType.BOOLEAN;
import static io.doov.core.dsl.meta.ast.ExclusionBar.BIG;
import static io.doov.core.dsl.meta.ast.ExclusionBar.SMALL;
import static java.lang.Math.floor;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;

import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.Operator;
import io.doov.core.dsl.meta.i18n.ResourceProvider;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

public class HtmlWriter {
    static final String SPACE = "&nbsp;";
    static final String APOS = "&apos;";
    static final String BR = "<br>";
    static final String CSS_VALIDATION_RULE = "dsl-validation-rule";
    static final String CSS_VALIDATE = "dsl-token-validate";
    static final String CSS_BINARY = "dsl-token-binary";
    static final String CSS_UNARY = "dsl-token-unary";
    static final String CSS_NARY = "dsl-token-nary";
    static final String CSS_WHEN = "dsl-token-when";
    static final String CSS_OPERATOR = "dsl-token-operator";
    static final String CSS_VALUE = "dsl-token-value";
    static final String CSS_FIELD = "dsl-token-field";
    static final String CSS_UNKNOWN = "dsl-token-unknown";

    static final String CSS_LI_LEAF = "dsl-li-leaf";
    static final String CSS_LI_BINARY = "dsl-li-binary";
    static final String CSS_LI_UNARY = "dsl-li-unary";
    static final String CSS_LI_NARY = "dsl-li-nary";

    static final String CSS_UL_WHEN = "dsl-ul-when";
    static final String CSS_UL_BINARY = "dsl-ul-binary";
    static final String CSS_UL_BINARY_CHILD = "dsl-ul-binary-child";
    static final String CSS_UL_UNARY = "dsl-ul-unary";

    static final String CSS_OL_NARY = "dsl-ol-nary";

    private final Locale locale;
    private final OutputStream os;
    private final ResourceProvider resources;

    public HtmlWriter(Locale locale, OutputStream os, ResourceProvider resources) {
        this.locale = locale;
        this.os = os;
        this.resources = resources;
    }

    public Locale getLocale() {
        return locale;
    }

    protected void write(String value) {
        try {
            os.write(value.getBytes(UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void writeFromBundle(Operator operator) {
        write(resources.get(operator, locale));
    }

    protected void writeFromBundle(String key) {
        write(resources.get(key, locale));
    }

    protected void writeBeginLi(String... classes) {
        write(beginLi(classes));
    }

    protected void writeEndLi() {
        write(endLi());
    }

    protected void writeBeginUl(String... classes) {
        write(beginUl(classes));
    }

    protected void writeEndUl() {
        write(endUl());
    }

    protected void writeBeginOl(String... classes) {
        write(beginOl(classes));
    }

    protected void writeEndOl() {
        write(endOl());
    }

    protected void writeBeginDiv(String... classes) {
        write(beginDiv(classes));
    }

    protected void writeBeginSpan(String... classes) {
        write(beginSpan(classes));
    }

    protected void writeBeginDivWithStyle(String style, String... classes) {
        write(beginDivWithStyle(style, classes));
    }

    protected void writeEndDiv() {
        write(endDiv());
    }

    protected void writeEndSpan() {
        write(endSpan());
    }

    // don't add the 'not' operator, it will add a gauge for the not & the inner predicate
    private static final List<Operator> OP_BOOLEAN_PARAMS = asList(and, or, match_all, match_any, match_none, count,
                    when);
    private static final List<Operator> OP_BIG_BAR = asList(match_all, match_any, match_none);

    protected void writeExclusionBar(Metadata metadata, ArrayDeque<Metadata> parents) {
        final Optional<Metadata> pmd = parents.stream().skip(1).findFirst();
        if (metadata.getOperator().returnType() == BOOLEAN
                        || pmd.map(m -> OP_BOOLEAN_PARAMS.contains(m.getOperator())).orElse(false)) {
            // LEAF_PREDICATE don't implements operator :-(
            final ExclusionBar barSize = pmd.map(m -> m.getOperator() == when ||
                            OP_BIG_BAR.contains(metadata.getOperator()) ? BIG : SMALL)
                            .orElse(SMALL);
            write(exclusionBar((PredicateMetadata) metadata, barSize, locale));
        }
    }

    static String beginElement(String elementType, String... classes) {
        return "<" + elementType + (classes.length > 0 ? " class='" + String.join(" ", classes) + "'" : "") + ">";
    }

    static String beginElementWithStyle(String elementType, String style, String... classes) {
        return "<" + elementType + (classes.length > 0 ? " class='" + String.join(" ", classes) + "'" : "")
                        + (style != null ? " style='" + style + "'" : "") + ">";
    }

    static String endElement(String elementType) {
        return "</" + elementType + ">";
    }

    static String beginLi(String... classes) {
        return beginElement("li", classes);
    }

    static String endLi() {
        return endElement("li");
    }

    static String beginUl(String... classes) {
        return beginElement("ul", classes);
    }

    static String endUl() {
        return endElement("ul");
    }

    static String beginOl(String... classes) {
        return beginElement("ol", classes);
    }

    static String endOl() {
        return endElement("ol");
    }

    static String beginDiv(String... classes) {
        return beginElement("div", classes);
    }

    static String beginSpan(String... classes) {
        return beginElement("span", classes);
    }

    static String beginDivWithStyle(String style, String... classes) {
        return beginElementWithStyle("div", style, classes);
    }

    static String endDiv() {
        return endElement("div");
    }

    static String endSpan() {
        return endElement("span");
    }

    static String exclusionBar(PredicateMetadata metadata, ExclusionBar cssClass, Locale locale) {
        final int nbTrue = metadata.trueEvalCount();
        final int nbFalse = metadata.falseEvalCount();
        if (nbTrue == 0 && nbFalse == 0) {
            return formatExclusionBar(cssClass);
        }
        final double percentage = floor((nbTrue / ((double) nbTrue + nbFalse)) * 1000) / 10.0;
        return formatExclusionBar(cssClass, percentage, locale);
    }

    static String formatExclusionBar(ExclusionBar cssClass) {
        return beginDiv(cssClass.getWrapperClass()) + beginDiv("percentage-value") + " n/a" + endDiv()
                        + beginDiv(cssClass.getBorderClass())
                        + beginDivWithStyle("width:0%;", cssClass.getFillingClass())
                        + endDiv() + endDiv() + endDiv();
    }

    static String formatExclusionBar(ExclusionBar cssClass, double percentage, Locale locale) {
        return beginDiv(cssClass.getWrapperClass()) + beginDiv("percentage-value")
                        + NumberFormat.getInstance(locale).format(percentage) + " %" + endDiv()
                        + beginDiv(cssClass.getBorderClass())
                        + beginDivWithStyle("width:" + percentage + "%;", cssClass.getFillingClass()) + endDiv()
                        + endDiv()
                        + endDiv();
    }

    public static String exclusionBar(ValidationRule rule, ExclusionBar cssClass, Locale locale) {
        Metadata conditionMetadata = rule.getStepWhen().stepCondition().metadata();
        if (conditionMetadata instanceof PredicateMetadata) {
            return exclusionBar((PredicateMetadata) conditionMetadata, cssClass, locale);
        } else {
            return "";
        }
    }
}
