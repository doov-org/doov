/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast.test;

import java.util.ArrayList;
import java.util.List;

public class TestCaseSpec {

    private final String description;
    private final List<String> testStates;
    private final List<AssertionSpec> ruleAssertions;
    private final List<FieldAssertionSpec> fieldAssertions;

    public TestCaseSpec(String description) {
        this.description = description;
        this.testStates = new ArrayList<>();
        this.ruleAssertions = new ArrayList<>();
        this.fieldAssertions = new ArrayList<>();
    }

    public String getDescription() {
        return description;
    }

    public List<String> getTestStates() {
        return testStates;
    }

    public List<AssertionSpec> getRuleAssertions() {
        return ruleAssertions;
    }

    public List<FieldAssertionSpec> getFieldAssertions() {
        return fieldAssertions;
    }
}
