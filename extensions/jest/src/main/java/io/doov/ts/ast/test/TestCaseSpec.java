/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast.test;

import java.util.ArrayList;
import java.util.List;

public class TestCaseSpec {

    private final String description;
    private final RuleAssertionSpec ruleAssertion;
    private final List<FieldAssertionSpec> fieldAssertions;

    public TestCaseSpec(String description, RuleAssertionSpec ruleAssertion) {
        this.description = description;
        this.ruleAssertion = ruleAssertion;
        this.fieldAssertions = new ArrayList<>();
    }

    public String getDescription() {
        return description;
    }

    public RuleAssertionSpec getRuleAssertion() {
        return ruleAssertion;
    }

    public List<FieldAssertionSpec> getFieldAssertions() {
        return fieldAssertions;
    }
}
