package org.modelmap.core.dsl.impl;

import org.modelmap.core.dsl.lang.RuleId;

public enum DefaultRuleId implements RuleId {

    DEFAULT;

    @Override
    public String getCode() {
        return this.name();
    }

}
