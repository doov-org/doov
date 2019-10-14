/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast.test;

import java.util.ArrayList;
import java.util.List;

import io.doov.ts.ast.writer.FieldSpec;
import io.doov.ts.ast.writer.ImportSpec;

public class JestTestSpec {

    private final String testSuiteName;
    private final List<ImportSpec> imports;
    private final List<FieldSpec> fields;
    private final List<String> testStates;
    private final List<String> beforeEachs;
    private final List<TestCaseSpec> testCases;

    public JestTestSpec(String testSuiteName) {
        this.testSuiteName = testSuiteName;
        this.imports = new ArrayList<>();
        this.fields = new ArrayList<>();
        this.testStates = new ArrayList<>();
        this.testCases = new ArrayList<>();
        this.beforeEachs = new ArrayList<>();
    }

    public String getTestSuiteName() {
        return testSuiteName;
    }

    public List<ImportSpec> getImports() {
        return imports;
    }

    public List<FieldSpec> getFields() {
        return fields;
    }

    public List<String> getTestStates() {
        return testStates;
    }

    public List<String> getBeforeEachs() {
        return beforeEachs;
    }

    public List<TestCaseSpec> getTestCases() {
        return testCases;
    }



}
