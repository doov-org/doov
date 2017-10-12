package org.modelmap.core.dsl.impl;

import org.modelmap.core.dsl.lang.Result;

public class DefaultResult implements Result {

    private final boolean validity;
    private final String message;

    public DefaultResult(boolean validity, String message) {
        this.validity = validity;
        this.message = message;
    }

    @Override
    public boolean isValid() {
        return validity;
    }

    @Override
    public boolean isInvalid() {
        return !validity;
    }

    @Override
    public String message() {
        return message;
    }

}
