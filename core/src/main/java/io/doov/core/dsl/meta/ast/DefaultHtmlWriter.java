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

import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.Operator;
import io.doov.core.dsl.meta.i18n.ResourceProvider;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.ReturnType.BOOLEAN;
import static io.doov.core.dsl.meta.ast.ExclusionBar.BIG;
import static io.doov.core.dsl.meta.ast.ExclusionBar.SMALL;
import static java.lang.Math.floor;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;

public class DefaultHtmlWriter implements HtmlWriter {

    private final Locale locale;
    private final OutputStream os;
    private final ResourceProvider resources;

    public DefaultHtmlWriter(Locale locale, OutputStream os, ResourceProvider resources) {
        this.locale = locale;
        this.os = os;
        this.resources = resources;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public void write(String value) {
        try {
            os.write(value.getBytes(UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String beginElement(String elementType, String... classes) {
        return "<" + elementType + (classes.length > 0 ? " class='" + String.join(" ", classes) + "'" : "") + ">";
    }

    private static String beginElementWithStyle(String elementType, String style, String... classes) {
        return "<" + elementType + (classes.length > 0 ? " class='" + String.join(" ", classes) + "'" : "")
                + (style != null ? " style='" + style + "'" : "") + ">";
    }

    private static String endElement(String elementType) {
        return "</" + elementType + ">";
    }

    private static String beginLi(String... classes) {
        return beginElement("li", classes);
    }

    private static String endLi() {
        return endElement("li");
    }

    private static String beginUl(String... classes) {
        return beginElement("ul", classes);
    }

    private static String endUl() {
        return endElement("ul");
    }

    private static String beginOl(String... classes) {
        return beginElement("ol", classes);
    }

    private static String endOl() {
        return endElement("ol");
    }

    private static String beginDiv(String... classes) {
        return beginElement("div", classes);
    }

    private static String beginSpan(String... classes) {
        return beginElement("span", classes);
    }

    private static String beginDivWithStyle(String style, String... classes) {
        return beginElementWithStyle("div", style, classes);
    }

    private static String endDiv() {
        return endElement("div");
    }

    private static String endSpan() {
        return endElement("span");
    }

    private static String exclusionBar(PredicateMetadata metadata, ExclusionBar cssClass, Locale locale) {
        final int nbTrue = metadata.trueEvalCount();
        final int nbFalse = metadata.falseEvalCount();
        if (nbTrue == 0 && nbFalse == 0) {
            return formatExclusionBar(cssClass);
        }
        final double percentage = floor((nbTrue / ((double) nbTrue + nbFalse)) * 1000) / 10.0;
        return formatExclusionBar(cssClass, percentage, locale);
    }

    private static String formatExclusionBar(ExclusionBar cssClass) {
        return beginDiv(cssClass.getWrapperClass()) + beginDiv("percentage-value") + " n/a" + endDiv()
                + beginDiv(cssClass.getBorderClass())
                + beginDivWithStyle("width:0%;", cssClass.getFillingClass())
                + endDiv() + endDiv() + endDiv();
    }

    private static String formatExclusionBar(ExclusionBar cssClass, double percentage, Locale locale) {
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

    @Override
    public void writeFromBundle(Operator operator) {
        write(resources.get(operator, locale));
    }

    @Override
    public void writeFromBundle(String key) {
        write(resources.get(key, locale));
    }

    @Override
    public void writeBeginLi(String... classes) {
        write(beginLi(classes));
    }

    @Override
    public void writeEndLi() {
        write(endLi());
    }

    @Override
    public void writeBeginUl(String... classes) {
        write(beginUl(classes));
    }

    @Override
    public void writeEndUl() {
        write(endUl());
    }

    @Override
    public void writeBeginOl(String... classes) {
        write(beginOl(classes));
    }

    @Override
    public void writeEndOl() {
        write(endOl());
    }

    @Override
    public void writeBeginDiv(String... classes) {
        write(beginDiv(classes));
    }

    @Override
    public void writeBeginSpan(String... classes) {
        write(beginSpan(classes));
    }

    @Override
    public void writeBeginDivWithStyle(String style, String... classes) {
        write(beginDivWithStyle(style, classes));
    }

    @Override
    public void writeEndDiv() {
        write(endDiv());
    }

    @Override
    public void writeEndSpan() {
        write(endSpan());
    }

    // don't add the 'not' operator, it will add a gauge for the not & the inner predicate
    private static final List<Operator> OP_BOOLEAN_PARAMS = asList(and, or, match_all, match_any, match_none, count,
                    when);
    private static final List<Operator> OP_BIG_BAR = asList(match_all, match_any, match_none);

    @Override
    public void writeExclusionBar(Metadata metadata, ArrayDeque<Metadata> parents) {
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

    @Override
    public String escapeHtml4(String readable) {
        return StringEscapeUtils.escapeHtml4(readable);
    }
}
