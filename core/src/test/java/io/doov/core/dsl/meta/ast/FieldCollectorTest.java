/*
 * Copyright 2018 Courtanet
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
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.DOOV.map;
import static io.doov.core.dsl.DOOV.template;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.mapping.TypeConverters.converter;
import static io.doov.core.dsl.template.ParameterTypes.$Enum;
import static io.doov.core.dsl.template.ParameterTypes.$Integer;
import static io.doov.core.dsl.template.ParameterTypes.$Iterable;
import static io.doov.core.dsl.template.ParameterTypes.$String;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.field.types.EnumFieldInfo;
import io.doov.core.dsl.field.types.IntegerFieldInfo;
import io.doov.core.dsl.field.types.IterableFieldInfo;
import io.doov.core.dsl.field.types.StringFieldInfo;
import io.doov.core.dsl.lang.MappingRule;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.runtime.GenericModel;

public class FieldCollectorTest {
    private GenericModel model;
    private StringFieldInfo stringField;
    private StringFieldInfo stringField2;
    private IntegerFieldInfo zeroField;
    private IterableFieldInfo<String, Iterable<String>> iterableField;
    private EnumFieldInfo<ChronoUnit> enumField;

    private ValidationRule rule;
    private MappingRule mapping;

    @BeforeEach
    void beforeEach() {
        this.model = new GenericModel();
        this.zeroField = model.intField(0, "zero");
        this.stringField = model.stringField("some string", "string field 1");
        this.stringField2 = model.stringField("other string", "string field 2");
        this.iterableField = model.iterableField(asList("a", "b"), "list");
        this.enumField = model.enumField(DAYS, "enum");
    }

    @Test
    void validationRule_zeroField() {
        rule = when(zeroField.notEq(0)).validate();
        assertTrue(rule.isUsing(zeroField.id()));
        assertFalse(rule.isUsing(enumField.id()));
    }

    @Test
    void validationRule_iterableField() {
        rule = when(iterableField.contains("c")).validate();
        assertTrue(rule.isUsing(iterableField.id()));
        assertFalse(rule.isUsing(enumField.id()));
    }

    @Test
    void validationRule_enumField() {
        rule = when(enumField.isNull()).validate();
        assertTrue(rule.isUsing(enumField.id()));
        assertFalse(rule.isUsing(iterableField.id()));
    }

    @Test
    void validationRule_stringField() {
        rule = when(stringField.matches("^some.*").or(stringField2.matches("^other.*"))).validate();
        assertTrue(rule.isUsing(stringField.id()));
        assertTrue(rule.isUsing(stringField2.id()));
        assertFalse(rule.isUsing(enumField.id()));
    }

    @Test
    void template_validationRule_zeroField() {
        rule = template($Integer).rule(param -> param.notEq(0)).bind(zeroField);
        assertTrue(rule.isUsing(zeroField.id()));
        assertFalse(rule.isUsing(enumField.id()));
    }

    @Test
    void template_validationRule_iterableField() {
        rule = template($Iterable(String.class)).rule(param -> param.contains("c")).bind(iterableField);
        assertTrue(rule.isUsing(iterableField.id()));
        assertFalse(rule.isUsing(enumField.id()));
    }

    @Test
    void template_validationRule_enumField() {
        rule = template($Enum(ChronoUnit.class)).rule(param -> param.isNull()).bind(enumField);
        assertTrue(rule.isUsing(enumField.id()));
        assertFalse(rule.isUsing(iterableField.id()));
    }

    @Test
    void template_validationRule_stringField() {
        rule = template($String, $String)
                        .rule((param1, param2) -> param1.matches("^some.*").or(param2.matches("^other.*")))
                        .bind(stringField, stringField2);
        assertTrue(rule.isUsing(stringField.id()));
        assertTrue(rule.isUsing(stringField2.id()));
        assertFalse(rule.isUsing(enumField.id()));
    }

    @Test
    void mappingRule_stringField() {
        mapping = map(stringField).to(stringField2);
        assertTrue(mapping.isUsing(stringField.id()));
        assertTrue(mapping.isUsing(stringField2.id()));
        assertFalse(mapping.isUsing(enumField.id()));
    }

    @Test
    void mappingRule_enumField() {
        mapping = map(enumField).using(converter(Enum::name, "name")).to(stringField2);
        assertTrue(mapping.isUsing(enumField.id()));
        assertTrue(mapping.isUsing(stringField2.id()));
        assertFalse(mapping.isUsing(stringField.id()));
    }

    @Test
    void template_mappingRule_stringField() {
        mapping = template($String, $String).mapping((param1, param2) -> map(param1).to(param2))
                        .bind(stringField, stringField2);
        assertTrue(mapping.isUsing(stringField.id()));
        assertTrue(mapping.isUsing(stringField2.id()));
        assertFalse(mapping.isUsing(enumField.id()));
    }

    @Test
    void template_mappingRule_enumField() {
        mapping = template($Enum(ChronoUnit.class), $String)
                        .mapping((param1, param2) -> map(param1).using(converter(Enum::name, "name")).to(param2))
                        .bind(enumField, stringField2);
        assertTrue(mapping.isUsing(enumField.id()));
        assertTrue(mapping.isUsing(stringField2.id()));
        assertFalse(mapping.isUsing(stringField.id()));
    }
}
