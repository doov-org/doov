/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast.test;

import java.util.*;

import io.doov.ts.ast.writer.FieldSpec;
import io.doov.ts.ast.writer.ImportSpec;

public class JestTestSpec {

    private final String testSuiteName;
    private final Set<ImportSpec> imports;
    private final Set<FieldSpec> fields;
    private final List<String> testStates;
    private final List<String> beforeEachs;
    private final List<TestCaseSpec> testCases;

    public JestTestSpec(String testSuiteName) {
        this.testSuiteName = testSuiteName;
        this.imports = new HashSet<>();
        this.fields = new HashSet<>();
        this.testStates = new ArrayList<>();
        this.testCases = new ArrayList<>();
        this.beforeEachs = new ArrayList<>();
    }

    public String getTestSuiteName() {
        return testSuiteName;
    }

    public Set<ImportSpec> getImports() {
        return imports;
    }

    public Set<FieldSpec> getFields() {
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
