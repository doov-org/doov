package io.doov.sample.validation;

import io.doov.core.dsl.lang.RuleId;

public enum AccountRulesId implements RuleId {

    VALID_EMAIL,
    VALID_EMAIL_SIZE,
    VALID_COUNTRY;

    @Override
    public String getCode() {
        return this.name();
    }

}