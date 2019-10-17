/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast.test;

import static io.doov.assertions.ts.Assertions.assertThat;
import static io.doov.ts.ast.test.JestExtension.parseAs;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.doov.assertions.ts.TypeScriptAssertionContext;
import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.field.types.IterableFieldInfo;
import io.doov.core.dsl.lang.MappingRule;
import io.doov.core.dsl.runtime.GenericModel;
import io.doov.ts.ast.writer.ImportSpec;
import io.doov.tsparser.TypeScriptParser;

class TypeScriptIterableTest {

    private MappingRule rule;
    private String ruleTs;
    
    @RegisterExtension
    static JestExtension jestExtension = new JestExtension("build/jest");

    @Test
    void test_iterable() throws IOException {

        List<String> content = Arrays.asList("1", "2", "3");

        GenericModel model = new GenericModel();
        IterableFieldInfo<String, List<String>> items = model.iterableField(content, "items", String.class);

        rule = DOOV.mapIter(content).to(items);

        ruleTs = jestExtension.toTS(rule.executeOn(model, model));
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("map", "to");
        assertThat(script).identifierReferencesText().containsExactly("DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("items");
        assertThat(script).literalsText().containsExactly("'1'", "'2'", "'3'");
        assertThat(script).arrayLiteralsText().containsExactly("['1','2','3']");
    }

    @Test
    void test_iterable_array() throws IOException {

        GenericModel model = new GenericModel();
        IterableFieldInfo<String, List<String>> items = model.iterableField(null, "items", String.class);

        rule = DOOV.mapIter("1", "2", "3").to(items);

        ruleTs = jestExtension.toTS(rule.executeOn(model, model));
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("map", "to");
        assertThat(script).identifierReferencesText().containsExactly("DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("items");
        assertThat(script).literalsText().containsExactly("'1'", "'2'", "'3'");
        assertThat(script).arrayLiteralsText().containsExactly("['1','2','3']");
    }

    @Test
    void test_iterable_int_array() throws IOException {

        GenericModel model = new GenericModel();
        IterableFieldInfo<Integer, List<Integer>> items = model.iterableField(Arrays.asList(), "integerItems", Integer.class);

        rule = DOOV.mapIter(1, 2, 3).to(items);

        ruleTs = jestExtension.toTS(rule.executeOn(model, model));
        TypeScriptAssertionContext script = parseAs(ruleTs, TypeScriptParser::script);

        assertThat(script).numberOfSyntaxErrors().isEqualTo(0);
        assertThat(script).identifierNamesText().containsExactly("map", "to");
        assertThat(script).identifierReferencesText().containsExactly("DOOV");
        assertThat(script).identifierExpressionsText().containsExactly("integerItems");
        assertThat(script).literalsText().containsExactly("1", "2", "3");
        assertThat(script).arrayLiteralsText().containsExactly("[1,2,3]");
    }

}
