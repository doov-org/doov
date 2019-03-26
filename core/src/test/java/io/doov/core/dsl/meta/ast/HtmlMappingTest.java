/*
 * Copyright 2018 Courtanet
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

import static io.doov.core.dsl.DOOV.map;
import static io.doov.core.dsl.DOOV.mappings;
import static io.doov.core.dsl.DOOV.template;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.mapping.TypeConverters.biConverter;
import static io.doov.core.dsl.mapping.TypeConverters.converter;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.documentOf;
import static io.doov.core.dsl.meta.ast.HtmlAnyMatchTest.format;
import static io.doov.core.dsl.template.ParameterTypes.$String;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.field.types.BooleanFieldInfo;
import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.field.types.LocalDateFieldInfo;
import io.doov.core.dsl.field.types.StringFieldInfo;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.runtime.GenericModel;

public class HtmlMappingTest {
    private GenericModel model;
    private IntegerFieldInfo intField;
    private BooleanFieldInfo booleanField;
    private LocalDateFieldInfo dateField;
    private LocalDateFieldInfo dateField2;
    private StringFieldInfo stringField;
    private StringFieldInfo stringField2;
    private Context ctx;
    private Document doc;

    @BeforeEach
    void beforeEach() {
        this.model = new GenericModel();
        this.intField = model.intField(1, "intField");
        this.booleanField = model.booleanField(false, "booleanField");
        this.dateField = model.localDateField(LocalDate.of(1, 1, 1), "dateField");
        this.dateField2 = model.localDateField(LocalDate.of(2, 2, 2), "dateField2");
        this.stringField = model.stringField("s1", "stringField");
        this.stringField2 = model.stringField("s2", "stringField2");
    }

    @Test
    void map_to_int_field() {
        ctx = map(18).to(intField).executeOn(model, model);
        doc = documentOf(ctx);
        assertThat(doc.select("div.percentage-value")).extracting(Element::text).isEmpty();
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);
        assertThat(doc.select("span.dsl-token-when")).hasSize(0);
        assertThat(doc.select("span.dsl-token-then")).hasSize(0);
        assertThat(doc.select("span.dsl-token-else")).hasSize(0);
        assertThat(doc.select("span.dsl-token-single-mapping")).hasSize(1);
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text).containsExactly("map", "to");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text).containsExactly("18");
        assertThat(doc.select("span.dsl-token-nary")).extracting(Element::text).isEmpty();
    }

    @Test
    void map_to_boolean_field() {
        ctx = map(true).to(booleanField).executeOn(model, model);
        doc = documentOf(ctx);
        assertThat(doc.select("div.percentage-value")).extracting(Element::text).isEmpty();
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);
        assertThat(doc.select("span.dsl-token-when")).hasSize(0);
        assertThat(doc.select("span.dsl-token-then")).hasSize(0);
        assertThat(doc.select("span.dsl-token-else")).hasSize(0);
        assertThat(doc.select("span.dsl-token-single-mapping")).hasSize(1);
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text).containsExactly("map", "to");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text).containsExactly("true");
        assertThat(doc.select("span.dsl-token-nary")).extracting(Element::text).isEmpty();
    }

    @Test
    void map_to_date_field() {
        ctx = map(LocalDate.of(2000, 1, 1)).to(dateField).executeOn(model, model);
        doc = documentOf(ctx);
        assertThat(doc.select("div.percentage-value")).extracting(Element::text).isEmpty();
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);
        assertThat(doc.select("span.dsl-token-when")).hasSize(0);
        assertThat(doc.select("span.dsl-token-then")).hasSize(0);
        assertThat(doc.select("span.dsl-token-else")).hasSize(0);
        assertThat(doc.select("span.dsl-token-single-mapping")).hasSize(1);
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text).containsExactly("map", "to");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text).containsExactly("2000-01-01");
        assertThat(doc.select("span.dsl-token-nary")).extracting(Element::text).isEmpty();
    }

    @Test
    void mappings_to_int_field_and_boolean_field() {
        ctx = mappings(map(18).to(intField),
                map(true).to(booleanField),
                map(LocalDate.of(2000, 1, 1)).to(dateField)).executeOn(model, model);
        doc = documentOf(ctx);
        assertThat(doc.select("div.percentage-value")).extracting(Element::text).isEmpty();
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(3);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);
        assertThat(doc.select("span.dsl-token-when")).hasSize(0);
        assertThat(doc.select("span.dsl-token-then")).hasSize(0);
        assertThat(doc.select("span.dsl-token-else")).hasSize(0);
        assertThat(doc.select("span.dsl-token-single-mapping")).hasSize(3);
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("map", "to", "map", "to", "map", "to");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("18", "true", "2000-01-01");
        assertThat(doc.select("span.dsl-token-nary")).extracting(Element::text).isEmpty();
    }

    @Test
    void mapping_to_date_field_with_converter() {
        ctx = map(LocalDate.of(2000, 1, 1))
                .using(converter(date -> date.toString(), "empty", "date to string"))
                .to(stringField).executeOn(model, model);
        doc = documentOf(ctx);
        assertThat(doc.select("div.percentage-value")).extracting(Element::text).isEmpty();
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);
        assertThat(doc.select("span.dsl-token-when")).hasSize(0);
        assertThat(doc.select("span.dsl-token-then")).hasSize(0);
        assertThat(doc.select("span.dsl-token-else")).hasSize(0);
        assertThat(doc.select("span.dsl-token-single-mapping")).hasSize(1);
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("map", "using", "to");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("2000-01-01", "'date to string'");
        assertThat(doc.select("span.dsl-token-nary")).extracting(Element::text).isEmpty();
    }

    @Test
    void mapping_to_string_field_with_converter() {
        ctx = map(stringField, stringField2)
                .using(biConverter((stringField, stringField2) -> stringField + " " + stringField2, "",
                        "combine names"))
                .to(stringField2).executeOn(model, model);
        doc = documentOf(ctx);
        assertThat(doc.select("div.percentage-value")).extracting(Element::text).isEmpty();
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);
        assertThat(doc.select("span.dsl-token-when")).hasSize(0);
        assertThat(doc.select("span.dsl-token-then")).hasSize(0);
        assertThat(doc.select("span.dsl-token-else")).hasSize(0);
        assertThat(doc.select("span.dsl-token-single-mapping")).hasSize(1);
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("map", "and", "using", "to");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("'combine names'");
        assertThat(doc.select("span.dsl-token-nary")).extracting(Element::text).isEmpty();
    }

    @Test
    void conditional_mapping_to_boolean_field() {
        ctx = when(dateField.ageAt(dateField2).greaterOrEquals(18))
                .then(map(true).to(booleanField))
                .otherwise(map(false).to(booleanField)).executeOn(model, model);
        doc = documentOf(ctx);
        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("0 %");
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(1);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);
        assertThat(doc.select("span.dsl-token-when")).hasSize(1);
        assertThat(doc.select("span.dsl-token-then")).hasSize(1);
        assertThat(doc.select("span.dsl-token-else")).hasSize(1);
        assertThat(doc.select("span.dsl-token-single-mapping")).hasSize(2);
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("age at", ">=", "map", "to", "map", "to");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("18", "true", "false");
        assertThat(doc.select("span.dsl-token-nary")).extracting(Element::text).isEmpty();
    }

    @Test
    void template_conditional_mapping_to_string_field() {
        ctx = template($String, $String)
                .mapping((site, url) -> when(site.eq("Yahoo")).then(map("www.yahou.com").to(url)))
                .bind(stringField, stringField2).executeOn(model, model);
        doc = documentOf(ctx);
        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("0 %");
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(1);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);
        assertThat(doc.select("span.dsl-token-when")).hasSize(1);
        assertThat(doc.select("span.dsl-token-then")).hasSize(1);
        assertThat(doc.select("span.dsl-token-else")).hasSize(0);
        assertThat(doc.select("span.dsl-token-single-mapping")).hasSize(1);
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("|", "=", "map", "to", "|");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("'Yahoo'", "www.yahou.com");
        assertThat(doc.select("span.dsl-token-nary")).extracting(Element::text).isEmpty();
    }

    @Test
    void template_conditional_mappings_to_string_field() {
        ctx = template($String, $String).mappings(
                (site, url) -> mappings(
                        when(site.eq("bing")).then(map("www.bingue.com").to(url)),
                        when(site.eq("Google")).then(map("www.gougeule.com").to(url)),
                        when(site.eq("Yahoo")).then(map("www.yahou.com").to(url))))
                .bind(stringField, stringField2).executeOn(model, model);
        doc = documentOf(ctx);
        assertThat(doc.select("div.percentage-value")).extracting(Element::text)
                .containsExactly("0 %", "0 %", "0 %");
        assertThat(doc.select("ol.dsl-ol-nary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-binary")).hasSize(0);
        assertThat(doc.select("li.dsl-li-nary")).hasSize(3);
        assertThat(doc.select("li.dsl-li-leaf")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-when")).hasSize(3);
        assertThat(doc.select("ul.dsl-ul-binary")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-binary-child")).hasSize(0);
        assertThat(doc.select("ul.dsl-ul-unary")).hasSize(0);
        assertThat(doc.select("span.dsl-token-when")).hasSize(3);
        assertThat(doc.select("span.dsl-token-then")).hasSize(3);
        assertThat(doc.select("span.dsl-token-else")).hasSize(0);
        assertThat(doc.select("span.dsl-token-single-mapping")).hasSize(3);
        assertThat(doc.select("span.dsl-token-operator")).extracting(Element::text)
                .containsExactly("|", "=", "map", "to", "|", "|", "=", "map", "to", "|", "|", "=", "map", "to", "|");
        assertThat(doc.select("span.dsl-token-value")).extracting(Element::text)
                .containsExactly("'bing'", "www.bingue.com", "'Google'", "www.gougeule.com", "'Yahoo'", "www.yahou.com");
        assertThat(doc.select("span.dsl-token-nary")).extracting(Element::text).isEmpty();
    }

    @AfterEach
    void afterEach() {
        System.out.println(format(ctx, doc));
    }
}
