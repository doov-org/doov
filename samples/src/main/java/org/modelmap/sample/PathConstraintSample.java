package org.modelmap.sample;

public enum PathConstraintSample {
    NO(""), //
    FOO_BAR("foo().bar()"), //
    ;

    private final String includePath;

    private PathConstraintSample(String includePath) {
        this.includePath = includePath;
    }

    public String includePath() {
        return includePath;
    }
}
