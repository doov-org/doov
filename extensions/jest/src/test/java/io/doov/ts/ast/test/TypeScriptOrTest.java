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

import static io.doov.assertions.ts.Assertions.assertThat;
import static io.doov.core.dsl.DOOV.alwaysFalse;
import static io.doov.core.dsl.DOOV.alwaysTrue;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.ts.ast.test.JestExtension.parseAs;
import static io.doov.ts.ast.writer.DefaultImportSpec.newImport;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.doov.assertions.ts.TypeScriptAssertionContext;
import io.doov.core.dsl.field.types.*;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.runtime.GenericModel;
import io.doov.core.dsl.time.LocalDateSuppliers;
import io.doov.tsparser.TypeScriptParser;

class TypeScriptOrTest {
    private StepCondition A, B, C, D;
    private Result result;
    private String ruleTs;
    
    @RegisterExtension
    static JestExtension jestExtension = new JestExtension("build/jest");

    @Test
    void or_true_false_complex() throws IOException {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        C = alwaysTrue("C");
        result = when(A.or(B.or(C))).validate().withShortCircuit(false).execute();
        ruleTs = jestExtension.toTS(result);

        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertTrue(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "or", "or", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "alwaysTrueA", "alwaysFalseB");
        assertThat(script).identifierExpressionsText().containsExactly("alwaysTrueC");
        assertThat(script).literalsText().isEmpty();
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void or_false_true_complex() throws IOException {
        A = alwaysFalse("A");
        B = alwaysTrue("B");
        C = alwaysTrue("C");
        result = when(A.or(B.and(C))).validate().withShortCircuit(false).execute();
        ruleTs = jestExtension.toTS(result);

        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertTrue(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "or", "and", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "alwaysFalseA", "alwaysTrueB");
        assertThat(script).identifierExpressionsText().containsExactly("alwaysTrueC");
        assertThat(script).literalsText().isEmpty();
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"true", "false"})
    void or_false(boolean isFalse) throws IOException {
        A = alwaysFalse("A");
        B = isFalse ? alwaysFalse("B") : alwaysTrue("B");
        result = when(A.or(B)).validate().withShortCircuit(false).execute();
        ruleTs = jestExtension.toTS(result);

        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertThat(result.value()).isEqualTo(!isFalse);
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "or", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "alwaysFalseA");
        assertThat(script).identifierExpressionsText().containsExactly(isFalse ? "alwaysFalseB" : "alwaysTrueB");
        assertThat(script).literalsText().isEmpty();
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void or_false_false_complex() throws IOException {
        A = alwaysFalse("A");
        B = alwaysFalse("B");
        C = alwaysTrue("C");
        result = when(A.or(B.and(C))).validate().withShortCircuit(false).execute();
        ruleTs = jestExtension.toTS(result);

        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertFalse(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "or", "and", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "alwaysFalseA", "alwaysFalseB");
        assertThat(script).identifierExpressionsText().containsExactly("alwaysTrueC");
        assertThat(script).literalsText().isEmpty();
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void or_true_false() throws IOException {
        A = alwaysTrue("A");
        B = alwaysFalse("B");
        result = when(A.or(B)).validate().withShortCircuit(false).execute();
        ruleTs = jestExtension.toTS(result);

        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertTrue(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "or", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "alwaysTrueA");
        assertThat(script).identifierExpressionsText().containsExactly("alwaysFalseB");
        assertThat(script).literalsText().isEmpty();
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void or_false_true() throws IOException {
        A = alwaysFalse("A");
        B = alwaysTrue("B");
        result = when(A.or(B)).validate().withShortCircuit(false).execute();
        ruleTs = jestExtension.toTS(result);

        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertTrue(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "or", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "alwaysFalseA");
        assertThat(script).identifierExpressionsText().containsExactly("alwaysTrueB");
        assertThat(script).literalsText().isEmpty();
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void or_true_true() throws IOException {
        A = alwaysTrue("A");
        B = alwaysTrue("B");
        result = when(A.or(B)).validate().withShortCircuit(false).execute();
        ruleTs = jestExtension.toTS(result);

        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertTrue(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "or", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "alwaysTrueA");
        assertThat(script).identifierExpressionsText().containsExactly("alwaysTrueB");
        assertThat(script).literalsText().isEmpty();
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void or_field_true_true() throws IOException {
        GenericModel model = new GenericModel();
        IntegerFieldInfo zero = model.intField(0, "zero");
        LocalDateFieldInfo yesterday = model.localDateField(LocalDate.now().minusDays(1), "yesterday");
        A = zero.lesserThan(4);
        B = yesterday.before(LocalDateSuppliers.today());
        result = when(A.or(B)).validate().withShortCircuit(false).executeOn(model);
        ruleTs = jestExtension.toTS(result);

        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertTrue(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "lesserThan", "or", "before", "today", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "zero", "yesterday", "DateFunction");
        assertThat(script).identifierExpressionsText().isEmpty();
        assertThat(script).literalsText().containsExactly("4");
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @Test
    void or_or_or() throws IOException {
        GenericModel model = new GenericModel();
        IntegerFieldInfo zero = model.intField(0, "zero");
        LocalDateFieldInfo yesterday = model.localDateField(LocalDate.now().minusDays(1), "yesterday");
        StringFieldInfo bob = model.stringField("Bob", "name");
        BooleanFieldInfo is_true = model.booleanField(false, "is True");
        A = zero.lesserThan(4);
        B = yesterday.before(LocalDateSuppliers.today());
        C = bob.startsWith("B");
        D = is_true.isFalse();

        result = when(A.or(B)
                .or(C)
                .or(D)).validate().withShortCircuit(false).executeOn(model);
        ruleTs = jestExtension.toTS(result);

        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertTrue(result.value());
        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("when", "lesserThan", "or", "before", "today", "or", "startsWith", "or", "eq", "validate");
        assertThat(script).identifierReferencesText().containsExactly("DOOV", "zero", "yesterday", "DateFunction", "name", "isTrue");
        assertThat(script).identifierExpressionsText().isEmpty();
        assertThat(script).literalsText().containsExactly("4", "'B'", "false");
        assertThat(script).arrayLiteralsText().isEmpty();
    }

    @AfterAll
    static void tearDown() {
        jestExtension.getJestTestSpec().getImports().add(newImport("doov", "BooleanFunction"));
        jestExtension.getJestTestSpec().getTestStates().add("const alwaysTrueA = DOOV.lift(BooleanFunction, true);");
        jestExtension.getJestTestSpec().getTestStates().add("const alwaysFalseA = DOOV.lift(BooleanFunction, false);");
        jestExtension.getJestTestSpec().getTestStates().add("const alwaysTrueB = DOOV.lift(BooleanFunction, true);");
        jestExtension.getJestTestSpec().getTestStates().add("const alwaysFalseB = DOOV.lift(BooleanFunction, false);");
        jestExtension.getJestTestSpec().getTestStates().add("const alwaysTrueC = DOOV.lift(BooleanFunction, true);");
        String now = LocalDate.now().minus(1, ChronoUnit.DAYS).toString();
        jestExtension.getJestTestSpec().getBeforeEachs().add("model = { zero: 0, yesterday: new Date('" + now + "'), name: 'Bob', isTrue: false };");
    }
}
