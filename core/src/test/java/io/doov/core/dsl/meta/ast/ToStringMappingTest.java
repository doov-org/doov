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
import static io.doov.core.dsl.template.ParameterTypes.$String;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.field.types.BooleanFieldInfo;
import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.field.types.LocalDateFieldInfo;
import io.doov.core.dsl.field.types.StringFieldInfo;
import io.doov.core.dsl.lang.MappingRule;
import io.doov.core.dsl.runtime.GenericModel;

public class ToStringMappingTest {
    private static final Locale LOCALE = Locale.US;
    private GenericModel model;
    private IntegerFieldInfo intField;
    private BooleanFieldInfo booleanField;
    private LocalDateFieldInfo dateField;
    private LocalDateFieldInfo dateField2;
    private StringFieldInfo stringField;
    private StringFieldInfo stringField2;
    private MappingRule rule;

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
        rule = map(18).to(intField);
        assertThat(rule.readable(LOCALE)).isEqualTo("map 18 to intField");
    }

    @Test
    void map_to_boolean_field() {
        rule = map(true).to(booleanField);
        assertThat(rule.readable(LOCALE)).isEqualTo("map true to booleanField");
    }

    @Test
    void map_to_date_field() {
        rule = map(LocalDate.of(2000, 1, 1)).to(dateField);
        assertThat(rule.readable(LOCALE)).isEqualTo("map 2000-01-01 to dateField");
    }

    @Test
    void mappings_to_int_field_and_boolean_field() {
        rule = mappings(map(18).to(intField),
                map(true).to(booleanField),
                map(LocalDate.of(2000, 1, 1)).to(dateField));
        assertThat(rule.readable(LOCALE))
                .isEqualTo("map 18 to intField, map true to booleanField, map 2000-01-01 to dateField");
    }

    @Test
    void mapping_to_date_field_with_converter() {
        rule = map(LocalDate.of(2019, 3, 27))
                .using(converter(date -> date.toString(), "empty", "date to string"))
                .to(stringField);
        assertThat(rule.readable(LOCALE))
                .isEqualTo("map 2019-03-27 using 'date to string' to stringField");
    }

    @Test
    void mapping_to_string_field_with_converter() {
        rule = map(stringField, stringField2)
                .using(biConverter((stringField, stringField2) -> stringField + " " + stringField2, "",
                        "combine names"))
                .to(stringField2);
        assertThat(rule.readable(LOCALE))
                .isEqualTo("map stringField and stringField2 using 'combine names' to stringField2");
    }

    @Test
    void conditional_mapping_to_boolean_field() {
        rule = when(dateField.ageAt(dateField2).greaterOrEquals(18))
                .then(map(true).to(booleanField))
                .otherwise(map(false).to(booleanField));
        assertThat(rule.readable(LOCALE)).isEqualTo(
                "when dateField age at dateField2 >= 18 "
                        + "then map true to booleanField "
                        + "else map false to booleanField");
    }

    @Test
    void template_conditional_mapping_to_string_field() {
        rule = template($String, $String)
                .mapping((site, url) -> when(site.eq("Yahoo")).then(map("www.yahou.com").to(url)))
                .bind(stringField, stringField2);
        assertThat(rule.readable(LOCALE))
                .isEqualTo("when {stringField} = 'Yahoo' "
                        + "then map 'www.yahou.com' to {stringField2}");
    }

    @Test
    void template_conditional_mappings_to_string_field() {
        model.set(stringField, "bing");
        rule = template($String, $String).mappings(
                (site, url) -> mappings(
                        when(site.eq("bing")).then(map("www.bingue.com").to(url)),
                        when(site.eq("Google")).then(map("www.gougeule.com").to(url)),
                        when(site.eq("Yahoo")).then(map("www.yahou.com").to(url))))
                .bind(stringField, stringField2);
        assertThat(rule.readable(LOCALE)).isEqualTo(
                "when {stringField} = 'bing' then map 'www.bingue.com' to {stringField2} "
                        + "when {stringField} = 'Google' then map 'www.gougeule.com' to {stringField2} "
                        + "when {stringField} = 'Yahoo' then map 'www.yahou.com' to {stringField2}");
    }

    @AfterEach
    void afterEach() {
        System.out.println(rule);
    }
}
