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
package io.doov.core.dsl.mapping;

import static io.doov.core.dsl.DOOV.*;
import static io.doov.core.dsl.mapping.TypeConverters.biConverter;
import static io.doov.core.dsl.mapping.TypeConverters.converter;
import static io.doov.core.dsl.template.ParameterTypes.$String;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.field.types.*;
import io.doov.core.dsl.runtime.GenericModel;

public class MappingTest {
    private GenericModel model;
    private IntegerFieldInfo intField;
    private BooleanFieldInfo booleanField;
    private LocalDateFieldInfo dateField;
    private LocalDateFieldInfo dateField2;
    private StringFieldInfo stringField;
    private StringFieldInfo stringField2;
    private IterableFieldInfo<String, List<String>> stringListField;

    @BeforeEach
    void beforeEach() {
        this.model = new GenericModel();
        this.intField = model.intField(1, "intField");
        this.booleanField = model.booleanField(false, "booleanField");
        this.dateField = model.localDateField(LocalDate.of(1, 1, 1), "dateField");
        this.dateField2 = model.localDateField(LocalDate.of(2, 2, 2), "dateField2");
        this.stringField = model.stringField("s1", "stringField");
        this.stringField2 = model.stringField("s2", "stringField2");
        this.stringListField = model.iterableField(null, "stringListField");
    }

    @Test
    void map_to_int_field() {
        map(18).to(intField).executeOn(model, model);
        assertThat(model.get(intField)).isEqualTo(18);
    }

    @Test
    void map_to_boolean_field() {
        map(true).to(booleanField).executeOn(model, model);
        assertThat(model.get(booleanField)).isTrue();
    }

    @Test
    void map_to_date_field() {
        map(LocalDate.of(2000, 1, 1)).to(dateField).executeOn(model, model);
        assertThat(model.get(dateField)).isEqualTo(LocalDate.of(2000, 1, 1));
    }

    @Test
    void mappings_to_int_field_and_boolean_field() {
        mappings(map(18).to(intField),
                map(true).to(booleanField),
                map(LocalDate.now()).to(dateField)).executeOn(model, model);
        assertThat(model.get(dateField)).isEqualTo(LocalDate.now());
        assertThat(model.get(booleanField)).isTrue();
    }

    @Test
    void mapping_to_date_field_with_converter() {
        map(LocalDate.of(2000, 1, 1))
                .using(converter(date -> date.toString(), "empty", "date to string"))
                .to(stringField).executeOn(model, model);
        assertThat(model.get(stringField)).isEqualTo("2000-01-01");
    }

    @Test
    void mapping_to_string_field_with_converter() {
        map(stringField, stringField2)
                .using(biConverter((stringField, stringField2) -> stringField + " " + stringField2, "",
                        "combine names"))
                .to(stringField2).executeOn(model, model);
        assertThat(model.get(stringField2)).isEqualTo("s1 s2");
    }

    @Test
    void conditional_mapping_to_boolean_field() {
        when(dateField.ageAt(dateField2).greaterOrEquals(18))
                .then(map(true).to(booleanField))
                .otherwise(map(false).to(booleanField)).executeOn(model, model);
        assertThat(model.get(booleanField)).isFalse();
    }

    @Test
    void template_conditional_mapping_to_string_field() {
        model.set(stringField, "Yahoo");
        template($String, $String)
                .mapping((site, url) -> when(site.eq("Yahoo")).then(map("www.yahou.com").to(url)))
                .bind(stringField, stringField2).executeOn(model, model);
        assertThat(model.get(stringField2)).isEqualTo("www.yahou.com");
    }

    @Test
    void template_conditional_mappings_to_string_field() {
        model.set(stringField, "bing");
        template($String, $String).mappings(
                (site, url) -> mappings(
                        when(site.eq("bing")).then(map("www.bingue.com").to(url)),
                        when(site.eq("Google")).then(map("www.gougeule.com").to(url)),
                        when(site.eq("Yahoo")).then(map("www.yahou.com").to(url))))
                .bind(stringField, stringField2).executeOn(model, model);
        assertThat(model.get(stringField2)).isEqualTo("www.bingue.com");
    }

    @Test
    void context_aware_mapping() {
        map((m, c) -> 3).to(intField)
                .executeOn(model, model);
        assertThat(model.get(intField)).isEqualTo(3);
    }

    @Test
    void context_aware_bimapping() {
        map((m, c) -> 1, (m, c) -> 2)
                .using(biConverter(Integer::sum, 0,  "sum"))
                .to(intField)
                .executeOn(model, model);
        assertThat(model.get(intField)).isEqualTo(3);
    }

    @Test
    void map_iterable_functions() {
        mapIter("0", "1", "2", "3")
                .map(Integer::parseInt, "to int")
                .filter(Objects::nonNull, "non null")
                .reduce(s -> s.findFirst().orElse(0), "find first")
                .to(intField)
                .executeOn(model);
        assertThat(model.get(intField)).isEqualTo(0);
    }

    @Test
    void map_to_iterable_functions() {
        mapIter("0", "1", "2", "3")
                .map(s -> "s" + s, "concat s")
                .collect(Collectors.toList())
                .to(stringListField)
                .executeOn(model);
        assertThat(model.get(stringListField)).containsExactly("s0", "s1", "s2", "s3");
    }

    @Test
    void map_to_iterable_functions_condition() {
        mapIter("0", "1", "2", "3")
                .map(Integer::parseInt, "parse int")
                .filter(f -> f.eq(intField))
                .map(String::valueOf, "String value of")
                .reduce(s -> s.findFirst().orElse(null), "take first")
                .to(stringField)
                .executeOn(model);
        assertThat(model.get(stringField)).isEqualTo("1");
    }

}
