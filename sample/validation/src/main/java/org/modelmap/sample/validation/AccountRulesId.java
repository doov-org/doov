package org.modelmap.sample.validation;

import org.modelmap.core.dsl.lang.RuleId;

public enum AccountRulesId implements RuleId {

    VALID_EMAIL,
    VALID_EMAIL_SIZE,
    VALID_COUNTRY;

    @Override
    public String getCode() {
        return this.name();
    }

}