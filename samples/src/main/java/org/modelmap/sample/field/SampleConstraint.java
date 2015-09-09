package org.modelmap.sample.field;

import org.modelmap.core.PathConstraint;

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
