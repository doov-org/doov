/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast.test;

public class RuleAssertionSpec {
    private final String rule;
    private final String expected;

    public RuleAssertionSpec(String rule, String expected) {
        this.rule = rule;
        this.expected = expected;
    }

    public String getRule() {
        return rule;
    }

    public String getExpected() {
        return expected;
    }
}
