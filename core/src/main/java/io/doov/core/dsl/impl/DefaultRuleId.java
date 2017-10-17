package io.doov.core.dsl.impl;

import io.doov.core.dsl.lang.RuleId;

public enum DefaultRuleId implements RuleId {

    DEFAULT;

    @Override
    public String getCode() {
        return this.name();
    }

}
