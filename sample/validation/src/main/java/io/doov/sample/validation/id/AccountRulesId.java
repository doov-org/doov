package io.doov.sample.validation.id;

import io.doov.core.dsl.lang.RuleId;

public enum AccountRulesId implements RuleId {

    VALID_EMAIL,
    VALID_ACCOUNT;

    @Override
    public String getCode() {
        return this.name();
    }

}