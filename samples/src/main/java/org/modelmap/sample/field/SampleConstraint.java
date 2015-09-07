package org.modelmap.sample.field;

import org.modelmap.core.PathConstraint;

public enum SampleConstraint implements PathConstraint {
    NONE(""), //
    FOO_BAR("foo().bar()"), //
    ;

    private final String includePath;

    private SampleConstraint(String includePath) {
        this.includePath = includePath;
    }

    public String includePath() {
        return includePath;
    }
}
