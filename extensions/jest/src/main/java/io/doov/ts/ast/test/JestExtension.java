/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast.test;

import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;
import static io.doov.ts.ast.test.JestTemplate.toTemplateParameters;
import static io.doov.tsparser.util.TypeScriptParserFactory.parseUsing;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.*;
import java.time.LocalDate;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.extension.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.doov.assertions.ts.TypeScriptAssertionContext;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.Result;
import io.doov.ts.ast.AstTSRenderer;
import io.doov.ts.ast.writer.*;
import io.doov.tsparser.TypeScriptParser;

public class JestExtension implements BeforeAllCallback, AfterAllCallback, AfterEachCallback {

    private JestTestSpec jestTestSpec;
    private TypeScriptWriter writer;

    private Result result;
    private Context executionContext;

    private final String testGenerateDir;
    private final Gson gson;

    public JestExtension() {
        this("./");
    }

    public JestExtension(String testGenerateDir) {
        this.testGenerateDir = testGenerateDir;
        this.gson = new GsonBuilder().create();
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        jestTestSpec = new JestTestSpec(context.getTestClass().get().getSimpleName());
        jestTestSpec.getTestStates().add("let model = {};");
    }

    @Override
    public void afterEach(ExtensionContext context) {
        String output = new String(((ByteArrayOutputStream) writer.getOutput()).toByteArray());
        TestCaseSpec testCaseSpec = new TestCaseSpec(getTestName(context));
        if (result != null) {
            testCaseSpec.getTestStates().add("const rule = DOOV.when(" + output + ").validate();");
            testCaseSpec.getTestStates().add("const result = rule.execute(model);");
            testCaseSpec.getRuleAssertions().add(new AssertionSpec("result.value", String.valueOf(result.value())));
        } else {
            testCaseSpec.getTestStates().add("const rule = " + output + ";");
            testCaseSpec.getTestStates().add("model = rule.execute(model);");
        }
        testCaseSpec.getFieldAssertions().addAll(
                writer.getFields().stream()
                        .distinct()
                        .filter(f -> executionContext.getEvalValues().containsKey(f.field().id()))
                        .map(f -> new FieldAssertionSpec(f.field(), f.name(), getExpectedValue(f)))
                        .collect(Collectors.toList())
        );
        jestTestSpec.getTestCases().add(testCaseSpec);
        jestTestSpec.getImports().addAll(writer.getImports());
        jestTestSpec.getFields().addAll(writer.getFields());
    }

    protected String getTestName(ExtensionContext context) {
        if (!context.getDisplayName().contains(context.getTestMethod().get().getName())) {
            return context.getTestMethod().get().getName() + "(" + context.getDisplayName() + ")";
        } else {
            return context.getDisplayName();
        }
    }

    protected String getExpectedValue(FieldSpec f) {
        Object evalValue = executionContext.getEvalValue(f.field().id());
        if (evalValue != null) {
            Class<?> valueType = evalValue.getClass();
            if (valueType.isEnum()) {
                return valueType.getSimpleName() + "." + evalValue;
            } else if (LocalDate.class.equals(valueType)) {
                return "new Date('" + evalValue + "')";
            }
        }
        return gson.toJson(evalValue);
    }

    @Override
    public void afterAll(ExtensionContext context) {
        JestTemplate.writeToFile(toTemplateParameters(jestTestSpec),
                new File(testGenerateDir, jestTestSpec.getTestSuiteName() + ".test.ts"));
    }

    public JestTestSpec getJestTestSpec() {
        return jestTestSpec;
    }

    public Gson getGson() {
        return gson;
    }

    public String toTS(Result result) {
        this.result = result;
        return toTS(result.getContext());
    }

    public String toTS(Context context) {
        this.executionContext = context;
        final ByteArrayOutputStream ops = new ByteArrayOutputStream();
        writer = new DefaultTypeScriptWriter(Locale.US, ops, BUNDLE,
                field -> field.id().code().replace(" ", ""));
        new AstTSRenderer(writer, true).toTS(context.getRootMetadata());
        return new String(ops.toByteArray(), UTF_8);
    }

    public static TypeScriptAssertionContext parseAs(String ruleTs, Function<TypeScriptParser, ParseTree> contextGetter)
            throws IOException {
        TypeScriptAssertionContext context = parseUsing(ruleTs, TypeScriptAssertionContext::new);
        new ParseTreeWalker().walk(context, contextGetter.apply(context.getParser()));
        return context;
    }
}
