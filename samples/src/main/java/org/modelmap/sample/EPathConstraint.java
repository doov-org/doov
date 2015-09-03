/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.sample;

public enum EPathConstraint {
    NO(""), //
    FOO_BAR("foo().bar()"), //
    ;

    private final String includePath;

    private EPathConstraint(String includePath) {
        this.includePath = includePath;
    }

    public String includePath() {
        return includePath;
    }
}
