/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.template;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.field.types.StringFieldInfo;
import io.doov.core.dsl.lang.BiTypeConverter;
import io.doov.core.dsl.runtime.GenericModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.doov.core.dsl.mapping.TypeConverters.biConverter;
import static io.doov.core.dsl.template.ParameterTypes.$String;

public class TemplateMappingTest {

    @Test
    void test1Mapping() {
        GenericModel model = new GenericModel();
        StringFieldInfo favoriteFruit = model.stringField("apple", "favorite fruit");

        TemplateMapping.Map1<StringFieldInfo> mapping = DOOV.template($String).mapping(
                fruit -> DOOV.map("banana").to(fruit));

        mapping.bind(favoriteFruit).executeOn(model, model);

        Assertions.assertEquals("banana", model.get(favoriteFruit.id()));
    }

    @Test
    void test2Mapping() {
        GenericModel model = new GenericModel();
        StringFieldInfo favoriteFruit = model.stringField("apple", "favorite fruit");
        StringFieldInfo someFruit = model.stringField("banana", "some fruit");

        TemplateMapping.Map2<StringFieldInfo, StringFieldInfo> mapping = DOOV.template($String, $String).mapping(
                (source, dest) -> DOOV.map(source).to(dest));

        mapping.bind(someFruit, favoriteFruit).executeOn(model, model);

        Assertions.assertEquals("banana", model.get(favoriteFruit.id()));
    }

    @Test
    void test3Mapping() {
        GenericModel model = new GenericModel();
        StringFieldInfo favoriteFruit = model.stringField("apple", "favorite fruit");
        StringFieldInfo someFruit = model.stringField("banana", "some fruit");
        StringFieldInfo anotherFruit = model.stringField("pineapple", "another fruit");

        BiTypeConverter<String, String, String> concat =
                biConverter((o, o2) -> o + ":" + o2, null, null, "join with :");

        TemplateMapping.Map3<StringFieldInfo, StringFieldInfo, StringFieldInfo> mapping =
                DOOV.template($String, $String, $String).mapping(
                        (some, another, dest) -> DOOV.map(some, another).using(concat).to(dest));

        mapping.bind(someFruit, anotherFruit, favoriteFruit).executeOn(model, model);

        Assertions.assertEquals("banana:pineapple", model.get(favoriteFruit.id()));
    }
}
