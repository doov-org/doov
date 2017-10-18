package io.doov.sample.validation;

import io.doov.core.dsl.lang.RuleId;

public enum UserRulesId implements RuleId {

    VALID_ADULT;

    @Override
    public String getCode() {
        return this.name();
    }

}