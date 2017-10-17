package io.doov.sample.field;

import io.doov.core.PathConstraint;

public enum SampleConstraint implements PathConstraint {
    NONE(""), //
    USER("getUser()"), //
    ACCOUNT("getAccount()"), //
    ;

    private final String includePath;

    SampleConstraint(String includePath) {
        this.includePath = includePath;
    }

    public String includePath() {
        return includePath;
    }
}
