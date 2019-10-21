/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast.test;

public class AssertionSpec {
    private final String value;
    private final String expected;

    public AssertionSpec(String value, String expected) {
        this.value = value;
        this.expected = expected;
    }

    public String getValue() {
        return value;
    }

    public String getExpected() {
        return expected;
    }
}
