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
package io.doov.ts.ast.test;

import static io.doov.assertions.ts.Assertions.assertThat;
import static io.doov.core.dsl.DOOV.*;
import static io.doov.core.dsl.mapping.TypeConverters.biConverter;
import static io.doov.core.dsl.mapping.TypeConverters.converter;
import static io.doov.core.dsl.template.ParameterTypes.$String;
import static io.doov.ts.ast.test.JestExtension.parseAs;

import java.io.IOException;
import java.time.LocalDate;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.doov.assertions.ts.TypeScriptAssertionContext;
import io.doov.core.dsl.field.types.*;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.runtime.GenericModel;
import io.doov.ts.ast.writer.ImportSpec;
import io.doov.tsparser.TypeScriptParser;

class TypeScriptMappingTest {

    private GenericModel model;
    private IntegerFieldInfo intField;
    private BooleanFieldInfo booleanField;
    private LocalDateFieldInfo dateField;
    private LocalDateFieldInfo dateField2;
    private StringFieldInfo stringField;
    private StringFieldInfo stringField2;
    private Context ctx;
    private String ruleTs;
    
    @RegisterExtension
    static JestExtension jestExtension = new JestExtension("build/jest");

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
    void map_to_int_field() throws IOException {
        ctx = map(18).to(intField).executeOn(model, model);
        ruleTs = jestExtension.toTS(ctx);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("map", "to");
        assertThat(script).identifierReferencesText().containsExactly("DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("intField");
        assertThat(script).literalsText().containsExactly("18");
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void map_to_boolean_field() throws IOException {
        ctx = map(true).to(booleanField).executeOn(model, model);
        ruleTs = jestExtension.toTS(ctx);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("map", "to");
        assertThat(script).identifierReferencesText().containsExactly("DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("booleanField");
        assertThat(script).literalsText().containsExactly("true");
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void map_to_date_field() throws IOException {
        ctx = map(LocalDate.of(2000, 1, 1)).to(dateField).executeOn(model, model);
        ruleTs = jestExtension.toTS(ctx);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("map", "to");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "Date");
        assertThat(script).identifierExpressionsText().containsExactly("dateField");
        assertThat(script).literalsText().containsExactly("'2000-01-01'");
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void mappings_to_int_field_and_boolean_field() throws IOException {
        ctx = mappings(map(18).to(intField), map(true).to(booleanField), map(LocalDate.of(2000, 1, 1)).to(dateField))
                .executeOn(model, model);
        ruleTs = jestExtension.toTS(ctx);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("mappings", "map", "to", "map", "to", "map", "to");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "DOOV", "DOOV", "DOOV", "Date");
        assertThat(script).identifierExpressionsText().containsExactly("intField", "booleanField", "dateField");
        assertThat(script).literalsText().containsExactly("18", "true", "'2000-01-01'");
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void mapping_to_date_field_with_converter() throws IOException {
        ctx = map(LocalDate.of(2000, 1, 1)).using(converter(date -> date.toString(), "empty", "date to string"))
                .to(stringField).executeOn(model, model);
        ruleTs = jestExtension.toTS(ctx);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("map", "using", "to");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "Date");
        assertThat(script).identifierExpressionsText().containsExactly("dateToString", "stringField");
        assertThat(script).literalsText().containsExactly("'2000-01-01'");
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void mapping_to_string_field_with_converter() throws IOException {
        ctx = map(stringField, stringField2)
                .using(biConverter((stringField, stringField2) -> stringField + " " + stringField2, "",
                        "combine names"))
                .to(stringField2).executeOn(model, model);
        ruleTs = jestExtension.toTS(ctx);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("map", "using", "to");
        assertThat(script).identifierReferencesText().containsExactly("DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("stringField", "stringField2", "combineNames", "stringField2");
        assertThat(script).literalsText().isEmpty();
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void conditional_mapping_to_boolean_field() throws IOException {
        ctx = when(dateField.ageAt(dateField2).greaterOrEquals(18)).then(map(true).to(booleanField))
                .otherwise(map(false).to(booleanField)).executeOn(model, model);
        ruleTs = jestExtension.toTS(ctx);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "ageAt", "greaterOrEquals", "then", "map",
                "to", "otherwise", "map", "to");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "dateField", "DOOV", "DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("dateField2", "booleanField", "booleanField");
        assertThat(script).literalsText().containsExactly("18", "true", "false");
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void template_conditional_mapping_to_string_field() throws IOException {
        ctx = template($String, $String)
                .mapping((site, url) -> when(site.eq("Yahoo")).then(map("www.yahou.com").to(url)))
                .bind(stringField, stringField2).executeOn(model, model);
        ruleTs = jestExtension.toTS(ctx);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "eq", "then", "map", "to");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "stringField", "DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("stringField2");
        assertThat(script).literalsText().containsExactly("'Yahoo'", "'www.yahou.com'");
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void template_conditional_mappings_to_string_field() throws IOException {
        ctx = template($String, $String)
                .mappings((site, url) -> mappings(when(site.eq("bing")).then(map("www.bingue.com").to(url)),
                        when(site.eq("Google")).then(map("www.gougeule.com").to(url)),
                        when(site.eq("Yahoo")).then(map("www.yahou.com").to(url))))
                .bind(stringField, stringField2).executeOn(model, model);
        ruleTs = jestExtension.toTS(ctx);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("mappings", "when", "eq", "then", "map", "to", "when"
                , "eq", "then", "map", "to", "when", "eq", "then", "map", "to");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "DOOV", "stringField", "DOOV", "DOOV",
                "stringField", "DOOV", "DOOV", "stringField", "DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("stringField2", "stringField2", "stringField2");
        assertThat(script).literalsText().containsExactly("'bing'", "'www.bingue.com'", "'Google'", "'www.gougeule" +
                ".com'", "'Yahoo'", "'www.yahou.com'");
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void map_null() throws IOException {
        ctx = mapNull(stringField).executeOn(model);
        ruleTs = jestExtension.toTS(ctx);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("mapNull");
        assertThat(script).identifierReferencesText().containsExactly("DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("stringField");
        assertThat(script).literalsText().isEmpty();
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @AfterAll
    static void tearDown() {
        jestExtension.getJestTestSpec().getImports().add(new ImportSpec("Function", "doov"));
        jestExtension.getJestTestSpec().getTestStates().add("const combineNames = DOOV.biConverter((obj, input: " +
                "Function<string>, input2: Function<string>, ctx) => {\n" +
                "  return input.get(obj, ctx) + \" \" + input2.get(obj, ctx);\n" +
                "}, 'combine names');");
        jestExtension.getJestTestSpec().getTestStates().add("const dateToString = DOOV.converter((obj, input: " +
                "Function<Date>, ctx) => {\n" +
                "  let d = input.get(obj, ctx);\n" +
                "  return d ? d.toISOString().substr(0, 10) : null;\n" +
                "}, 'date to string');");
        LocalDate date1 = LocalDate.of(1, 1, 1);
        LocalDate date2 = LocalDate.of(2, 2, 2);
        jestExtension.getJestTestSpec().getBeforeEachs().add("model = {stringField: 's1', stringField2: 's2', " +
                "intField: 1, booleanField: false, dateField: new Date('"+ date1 +"'), dateField2: new Date('" + date2 + "') }");
        jestExtension.getJestTestSpec().getImports().add(ImportSpec.starImport("DOOV", "doov"));
    }
}
