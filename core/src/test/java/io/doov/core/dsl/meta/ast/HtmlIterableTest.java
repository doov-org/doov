/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.meta.ast.AstVisitorUtils.toHtml;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.documentOf;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.field.types.IterableFieldInfo;
import io.doov.core.dsl.lang.MappingRule;
import io.doov.core.dsl.runtime.GenericModel;

public class HtmlIterableTest {

    @Test
    void test_iterable() {

        List<String> content = Arrays.asList(
                "1","2","3"
        );

        GenericModel model =  new GenericModel();
        IterableFieldInfo<String, List<String>> items = model.iterableField(content,"items");

        MappingRule rule = DOOV.mapIter(content).to(items);

        Document document = documentOf(rule.executeOn(model,model));

        assertThat(document.select("." + HtmlWriter.CSS_UL_ITERABLE).text())
                .isEqualTo("1 2 3");

    }

    @Test
    void test_iterable_array() {

        GenericModel model =  new GenericModel();
        IterableFieldInfo<String, List<String>> items = model.iterableField(null,"items");

        MappingRule rule = DOOV.mapIter("1","2","3").to(items);

        Document document = documentOf(rule.executeOn(model,model));

        assertThat(document.select("." + HtmlWriter.CSS_UL_ITERABLE).text())
                .isEqualTo("1 2 3");

    }
}
