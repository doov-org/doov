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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.doov.assertions.ts.TypeScriptAssertionContext;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.meta.RuleMetadata;
import io.doov.core.dsl.meta.WhenMetadata;
import io.doov.ts.ast.AstTSRenderer;
import io.doov.ts.ast.writer.*;
import io.doov.tsparser.TypeScriptParser;

public class JestExtension implements BeforeAllCallback, AfterAllCallback, AfterEachCallback {

    public static final Function<TypeScriptWriter, AstTSRenderer> DEFAULT_RENDERER_FUNCTION =
            w -> new AstTSRenderer(w, true);

    protected ThreadLocal<TypeScriptWriter> writer;
    protected ThreadLocal<Result> result;
    protected ThreadLocal<Context> executionContext;

    protected JestTestSpec jestTestSpec;
    protected final String testGenerateDir;
    protected final Gson gson;

    protected final Function<TypeScriptWriter, AstTSRenderer> tsRendererFunction;

    public JestExtension() {
        this("./", DEFAULT_RENDERER_FUNCTION);
    }

    public JestExtension(String testGenerateDir) {
        this(testGenerateDir, DEFAULT_RENDERER_FUNCTION);
    }

    public JestExtension(String testGenerateDir, Function<TypeScriptWriter, AstTSRenderer> tsRendererFunction) {
        this.testGenerateDir = testGenerateDir;
        this.tsRendererFunction = tsRendererFunction;
        this.gson = new GsonBuilder().create();
        this.writer = new ThreadLocal<>();
        this.result = new ThreadLocal<>();
        this.executionContext = new ThreadLocal<>();
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        jestTestSpec = new JestTestSpec(context.getTestClass().get().getSimpleName());
        jestTestSpec.getTestStates().add("let model = {};");
    }

    @Override
    public void afterEach(ExtensionContext context) {
        if (!context.getElement().get().isAnnotationPresent(Disabled.class)) {
            String output = new String(((ByteArrayOutputStream) getWriter().getOutput()).toByteArray());
            TestCaseSpec testCaseSpec = new TestCaseSpec(getTestName(context));
            testCaseSpec.getTestStates().add("const rule = " + output + ";");
            if (getResult() != null) {
                testCaseSpec.getTestStates().add("const result = rule.execute(model);");
                testCaseSpec.getRuleAssertions().add(new AssertionSpec("result.value",
                        String.valueOf(getResult().value())));
            } else {
                testCaseSpec.getTestStates().add("model = rule.execute(model);");
            }
            testCaseSpec.getFieldAssertions().addAll(
                    getWriter().getFields().stream()
                            .distinct()
                            .filter(f -> getContext().getEvalValues().containsKey(f.field().id()))
                            .map(f -> new FieldAssertionSpec(f.field(), f.name(), getExpectedValue(f)))
                            .collect(Collectors.toList())
            );
            jestTestSpec.getTestCases().add(testCaseSpec);
            jestTestSpec.getImports().addAll(getWriter().getImports());
            jestTestSpec.getFields().addAll(getWriter().getFields());
        }
    }

    protected String getTestName(ExtensionContext context) {
        if (!context.getDisplayName().contains(context.getTestMethod().get().getName())) {
            return context.getTestMethod().get().getName() + "(" + context.getDisplayName() + ")";
        } else {
            return context.getDisplayName();
        }
    }

    protected String getExpectedValue(FieldSpec f) {
        Object evalValue = getContext().getEvalValue(f.field().id());
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

    public TypeScriptWriter getWriter() {
        return writer.get();
    }

    public Result getResult() {
        return result.get();
    }

    public Context getContext() {
        return executionContext.get();
    }

    public String toTS(Result result) {
        this.result.set(result);
        this.executionContext.set(result.getContext());
        final ByteArrayOutputStream ops = new ByteArrayOutputStream();
        writer.set(new DefaultTypeScriptWriter(Locale.US, ops, BUNDLE,
                field -> field.id().code().replace(" ", "")));
        RuleMetadata rule = RuleMetadata.rule(WhenMetadata.when(result.getContext().getRootMetadata()));
        tsRendererFunction.apply(getWriter()).toTS(rule);
        return new String(ops.toByteArray(), UTF_8);
    }

    public String toTS(Context context) {
        this.executionContext.set(context);
        final ByteArrayOutputStream ops = new ByteArrayOutputStream();
        writer.set(new DefaultTypeScriptWriter(Locale.US, ops, BUNDLE,
                field -> field.id().code().replace(" ", "")));
        tsRendererFunction.apply(getWriter()).toTS(context.getRootMetadata());
        return new String(ops.toByteArray(), UTF_8);
    }

    public static TypeScriptAssertionContext parseAs(String ruleTs, Function<TypeScriptParser, ParseTree> contextGetter)
            throws IOException {
        TypeScriptAssertionContext context = parseUsing(ruleTs, TypeScriptAssertionContext::new);
        new ParseTreeWalker().walk(context, contextGetter.apply(context.getParser()));
        return context;
    }
}
