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
package io.doov.ts.ast.test;

import static io.doov.assertions.ts.Assertions.assertParenthesis;
import static io.doov.assertions.ts.Assertions.assertThat;
import static io.doov.core.dsl.DOOV.alwaysFalse;
import static io.doov.core.dsl.DOOV.alwaysTrue;
import static io.doov.core.dsl.DOOV.matchAll;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.ts.ast.test.JestExtension.parseAs;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.doov.assertions.ts.TypeScriptAssertionContext;
import io.doov.core.dsl.field.types.*;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.runtime.GenericModel;
import io.doov.ts.ast.writer.ImportSpec;
import io.doov.tsparser.TypeScriptParser;

class TypeScriptCombinedTest {
    private StepCondition A, B, C;
    private Result result;
    private String ruleTs;

    private GenericModel model;
    private StringFieldInfo stringField;
    private StringFieldInfo stringField2;
    private IntegerFieldInfo zeroField;
    private IterableFieldInfo<String, List<String>> iterableField;
    private EnumFieldInfo<?> enumField;
    
    @RegisterExtension
    static JestExtension jestExtension = new JestExtension("build/jest");

    @BeforeEach
    void beforeEach() {
        this.model = new GenericModel();
        this.zeroField = model.intField(0, "zero");
        this.stringField = model.stringField("some string", "string field 1");
        this.stringField2 = model.stringField("other string", "string field 2");
        this.iterableField = model.iterableField(asList("a", "b"), "list");
        this.enumField = model.enumField(null, "enumField");
    }

    @Test
    void reduce_matchAll() throws IOException {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        C = alwaysFalse("C");
        result = when(matchAll(A, B, C)).validate().withShortCircuit(false).execute();
        ruleTs = jestExtension.toTS(result);

        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertFalse(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "matchAll", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("alwaysTrueA", "alwaysFalseB", "alwaysFalseC");
        assertThat(script).literalsText().isEmpty();
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void reduce_and() throws IOException {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        result = when(A.and(B)).validate().withShortCircuit(false).execute();
        ruleTs = jestExtension.toTS(result);
        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertFalse(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "and", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "alwaysTrueA");
        assertThat(script).identifierExpressionsText().containsExactly("alwaysFalseB");
        assertThat(script).literalsText().isEmpty();
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void reduce_zeroInt() throws IOException {
        result = when(zeroField.notEq(0)).validate().withShortCircuit(false).executeOn(model);
        ruleTs = jestExtension.toTS(result);
        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertFalse(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "notEq", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "zero");
        assertThat(script).identifierExpressionsText().isEmpty();
        assertThat(script).literalsText().containsExactly("0");
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void reduce_list() throws IOException {
        result = when(iterableField.contains("c")).validate().withShortCircuit(false).executeOn(model);
        ruleTs = jestExtension.toTS(result);
        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertFalse(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "contains", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "list");
        assertThat(script).identifierExpressionsText().isEmpty();
        assertThat(script).literalsText().containsExactly("'c'");
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void reduce_null() throws IOException {
        result = when(enumField.isNull()).validate().withShortCircuit(false).executeOn(model);
        ruleTs = jestExtension.toTS(result);

        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertTrue(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "isNull", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "enumField");
        assertThat(script).identifierExpressionsText().isEmpty();
        assertThat(script).literalsText().isEmpty();
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void matches_regexp() throws IOException {
        result = when(stringField.matches("^some.*")
                .or(stringField2.matches("^other.*"))).validate().withShortCircuit(false).executeOn(model);
        ruleTs = jestExtension.toTS(result);

        assertParenthesis(ruleTs);
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertTrue(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "matches", "or", "matches", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "stringfield1", "stringfield2");
        assertThat(script).identifierExpressionsText().isEmpty();
        assertThat(script).literalsText().containsExactly("'^some.*'", "'^other.*'");
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @AfterAll
    static void tearDown() {
        jestExtension.getJestTestSpec().getImports().add(new ImportSpec("BooleanFunction", "doov"));
        jestExtension.getJestTestSpec().getTestStates().add("const alwaysTrueA = DOOV.lift(BooleanFunction, true);");
        jestExtension.getJestTestSpec().getTestStates().add("const alwaysFalseB = DOOV.lift(BooleanFunction, false);");
        jestExtension.getJestTestSpec().getTestStates().add("const alwaysFalseC = DOOV.lift(BooleanFunction, false);");
        jestExtension.getJestTestSpec().getBeforeEachs().add("model = { zero: 0, stringfield1: 'some string', " +
                "stringfield2: 'other string', list: ['a', 'b'], enumField: null}");
    }

}
