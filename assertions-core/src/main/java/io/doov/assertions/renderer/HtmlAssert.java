/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.assertions.renderer;

import static java.util.stream.Collectors.toList;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.ListAssert;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HtmlAssert extends AbstractAssert<HtmlAssert, Document> {

    public HtmlAssert(Document actual, Class<?> selfType) {
        super(actual, selfType);
    }

    public ListAssert<Element> nary_OL() {
        return new ListAssert<>(actual.select("ol.dsl-ol-nary").stream().collect(toList()));
    }

    public ListAssert<Element> binary_LI() {
        return new ListAssert<>(actual.select("li.dsl-li-binary").stream().collect(toList()));
    }

    public ListAssert<Element> nary_LI() {
        return new ListAssert<>(actual.select("li.dsl-li-nary").stream().collect(toList()));
    }

    public ListAssert<Element> leaf_LI() {
        return new ListAssert<>(actual.select("li.dsl-li-leaf").stream().collect(toList()));
    }

    public ListAssert<Element> when_UL() {
        return new ListAssert<>(actual.select("ul.dsl-ul-when").stream().collect(toList()));
    }

    public ListAssert<Element> binary_UL() {
        return new ListAssert<>(actual.select("ul.dsl-ul-binary").stream().collect(toList()));
    }

    public ListAssert<Element> binaryChild_UL() {
        return new ListAssert<>(actual.select("ul.dsl-ul-binary-child").stream().collect(toList()));
    }

    public ListAssert<Element> unary_UL() {
        return new ListAssert<>(actual.select("ul.dsl-ul-unary").stream().collect(toList()));
    }

    public ListAssert<String> nary_UL() {
        return new ListAssert<>(actual.select("ul.dsl-ul-unary").stream().map(Element::text).collect(toList()));
    }

    public ListAssert<Element> unary_LI() {
        return new ListAssert<>(actual.select("ul.dsl-li-unary").stream().collect(toList()));
    }

    public ListAssert<Element> iterable_UL() {
        return new ListAssert<>(actual.select("ul.dsl-ul-iterable").stream().collect(toList()));
    }

    public ListAssert<String> percentageValue_DIV() {
        return new ListAssert<>(actual.select("div.percentage-value").stream().map(Element::text).collect(toList()));
    }

    public ListAssert<String> tokenOperator_SPAN() {
        return new ListAssert<>(actual.select("span.dsl-token-operator").stream().map(Element::text).collect(toList()));
    }

    public ListAssert<String> tokenField_SPAN() {
        return new ListAssert<>(actual.select("span.dsl-token-field").stream().map(Element::text).collect(toList()));
    }

    public ListAssert<String> tokenUnknown_SPAN() {
        return new ListAssert<>(actual.select("span.dsl-token-unknown").stream().map(Element::text).collect(toList()));
    }

    public ListAssert<String> tokenValue_SPAN() {
        return new ListAssert<>(actual.select("span.dsl-token-value").stream().map(Element::text).collect(toList()));
    }

    public ListAssert<String> tokenBinary_SPAN() {
        return new ListAssert<>(actual.select("span.dsl-token-binary").stream().map(Element::text).collect(toList()));
    }
    
    public ListAssert<String> tokenWhen_SPAN() {
        return new ListAssert<>(actual.select("span.dsl-token-when").stream().map(Element::text).collect(toList()));
    }
    
    public ListAssert<String> tokenThen_SPAN() {
        return new ListAssert<>(actual.select("span.dsl-token-then").stream().map(Element::text).collect(toList()));
    }
    
    public ListAssert<String> tokenElse_SPAN() {
        return new ListAssert<>(actual.select("span.dsl-token-else").stream().map(Element::text).collect(toList()));
    }
    
    public ListAssert<String> tokenSingleMapping_SPAN() {
        return new ListAssert<>(actual.select("span.dsl-token-single-mapping").stream().map(Element::text).collect(toList()));
    }

    public ListAssert<String> tokenNary_SPAN() {
        return new ListAssert<>(actual.select("span.dsl-token-nary").stream().map(Element::text).collect(toList()));
    }

    public ListAssert<String> BODY() {
        return new ListAssert<>(actual.select("body").stream().map(Element::text).collect(toList()));
    }

    public ListAssert<String> tokenUnary_SPAN() {
        return new ListAssert<>(actual.select("span.dsl-token-unary").stream().map(Element::text).collect(toList()));
    }
}
