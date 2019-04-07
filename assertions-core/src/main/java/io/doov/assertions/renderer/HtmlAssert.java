/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.assertions.renderer;

import static java.util.stream.Collectors.toList;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.IntegerAssert;
import org.assertj.core.api.ListAssert;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HtmlAssert extends AbstractAssert<HtmlAssert, Document> {

    public HtmlAssert(Document actual, Class<?> selfType) {
        super(actual, selfType);
    }

    public IntegerAssert countOlNary() {
        return new IntegerAssert(actual.select("ol.dsl-ol-nary").size());
    }

    public IntegerAssert countLiBinary() {
        return new IntegerAssert(actual.select("li.dsl-li-binary").size());
    }

    public IntegerAssert countLiNary() {
        return new IntegerAssert(actual.select("li.dsl-li-nary").size());
    }

    public IntegerAssert countLiLeaf() {
        return new IntegerAssert(actual.select("li.dsl-li-leaf").size());
    }

    public IntegerAssert countUlWhen() {
        return new IntegerAssert(actual.select("ul.dsl-ul-when").size());
    }

    public IntegerAssert countUlBinary() {
        return new IntegerAssert(actual.select("ul.dsl-ul-binary").size());
    }

    public IntegerAssert countUlUnary() {
        return new IntegerAssert(actual.select("ul.dsl-ul-unary").size());
    }

    public ListAssert<String> percentageValues() {
        return new ListAssert<String>(actual.select("div.percentage-value")
                .stream().map(Element::text).collect(toList()));
    }

    public ListAssert<String> tokenOperators() {
        return new ListAssert<String>(actual.select("span.dsl-token-operator")
                .stream().map(Element::text).collect(toList()));
    }

    public ListAssert<String> tokenValues() {
        return new ListAssert<String>(actual.select("span.dsl-token-value")
                .stream().map(Element::text).collect(toList()));
    }

    public ListAssert<String> tokenBinaries() {
        return new ListAssert<String>(actual.select("span.dsl-token-binary")
                .stream().map(Element::text).collect(toList()));
    }
}
